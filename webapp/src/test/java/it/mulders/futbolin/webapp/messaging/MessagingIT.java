package it.mulders.futbolin.webapp.messaging;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.WithAssertions;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.CreationException;
import javax.enterprise.inject.Produces;

@Testcontainers
@Slf4j
class MessagingIT implements WithAssertions {
    private static final Weld weld = new Weld()
            .disableDiscovery()
            .addBeanClass(DefaultMessageReceiver.class)
            .addBeanClass(DefaultMessageSender.class)
            .addBeanClass(MessagingFactory.class)
            .addBeanClass(Queue.class)
            .addBeanClass(ValidMessagingConfigFactory.class)
            .addBeanClass(PingService.class)
            .addBeanClass(PingReceiver.class)
            .addBeanClass(EchoResponder.class)
            .addBeanClass(EchoService.class);
    private static final WeldContainer container = weld.initialize();

    private static final String RABBITMQ = "rabbitmq:3.7-management-alpine";
    private static final String RABBITMQ_USER = "guest";
    private static final String RABBITMQ_PASSWORD = "guest";

    @Container
    public static GenericContainer<?> rabbitmq = new GenericContainer<>(RABBITMQ).withExposedPorts(5672);

    @ApplicationScoped
    static class ValidMessagingConfigFactory {
        @Produces
        public MessagingConfig messagingConfig() {
            var host = rabbitmq.getHost();
            var port = rabbitmq.getFirstMappedPort();
            log.debug("RabbitMQ exposed at {}:{}", host, port);
            return new MessagingConfig(host, port, RABBITMQ_USER, RABBITMQ_PASSWORD);
        }
    }

    @ApplicationScoped
    static class InvalidMessagingConfigFactory {
        @Produces
        public MessagingConfig messagingConfig() {
            return new MessagingConfig("foobarbaz", 5627, RABBITMQ_USER, RABBITMQ_PASSWORD);
        }
    }

    private final EchoService echoService = container.select(EchoService.class).get();
    private final EchoResponder echoResponder = container.select(EchoResponder.class).get();
    private final PingService pingService = container.select(PingService.class).get();
    private final PingReceiver pingReceiver = container.select(PingReceiver.class).get();

    @Test
    void should_work_in_rpc_style() throws MessagingException {
        // Make sure the responder is invoked, otherwise this.echoResponder will only be the Weld proxy and the actual
        // instance will not have been started.
        var queue = echoResponder.getQueue();
        log.debug("Echo responder listening at {} with channel {}", queue.name, queue.channel.getChannelNumber());

        var request = "Hello, world";
        var response = echoService.echo(request);

        log.info("Requests sent, awaiting responses...");

        response.thenApply(r -> assertThat(r).isEqualTo("Response to " + request))
                .toCompletableFuture()
                .join();
    }

    @Test
    void should_work_in_fire_forget_style() throws InterruptedException, MessagingException {
        // Make sure the receiver is invoked, otherwise this.pingReceiver will only be the Weld proxy and the actual
        // instance will not have been started.
        var queue = pingReceiver.getQueue();
        log.debug("Ping receiver listening at {} with channel {}", queue.name, queue.channel.getChannelNumber());

        var request = "Hello, world";
        pingService.ping(request);

        var delivery = pingReceiver.getReceivedMessages().take();

        assertThat(delivery).isNotNull();
        assertThat(delivery.getProperties().getCorrelationId()).isNull();
        assertThat(delivery.getProperties().getReplyTo()).isNull();
    }

    @Test
    void should_not_work_with_invalid_configuration() {
        var weld = new Weld()
                .disableDiscovery()
                .addBeanClass(DefaultMessageReceiver.class)
                .addBeanClass(DefaultMessageSender.class)
                .addBeanClass(MessagingFactory.class)
                .addBeanClass(Queue.class)
                .addBeanClass(InvalidMessagingConfigFactory.class)
                .addBeanClass(PingService.class)
                .addBeanClass(PingReceiver.class)
                .addBeanClass(EchoResponder.class)
                .addBeanClass(EchoService.class);
        var container = weld.initialize();

        assertThatExceptionOfType(CreationException.class).isThrownBy(() -> {
            var responder = container.select(EchoResponder.class).get();
            assertThat(responder).isNotNull();
            assertThat(responder.getQueue()).isNotNull();
        });

        assertThatExceptionOfType(CreationException.class).isThrownBy(() -> {
            var service = container.select(EchoService.class).get();
            assertThat(service).isNotNull();
            assertThat(service.getResponseQueue()).isNotNull();
        });
    }
}
