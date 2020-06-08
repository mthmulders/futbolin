package it.mulders.futbolin.webapp.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Collections.emptyMap;

/**
 * Simple CDI-factory for RabbitMQ stuff. Might want to look into the Java Connector Architecture (JCA) one day.
 *
 * For every injection, it creates a new RabbitMQ {@link Channel}. It also keeps an administration of them so that the
 * channels and connections can be closed when the factory is shut down.
 *
 * The beans do not have a scope qualifier. As a result, produced beans have the default / dependent scope. This means
 * a new instance gets created at every injection point, and that instance lives as long as the object where it is
 * injected lives. This may not be the most efficient in terms of connection and resource management, but it prevents
 * {@link Channel} instances bein used over threads.
 */
@ApplicationScoped
@Slf4j
public class MessagingFactory {
    private static final boolean DURABLE = true;
    private static final boolean NO_AUTO_DELETE = false;
    private static final boolean NOT_EXCLUSIVE = false;

    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    /**
     * Counter to generate unique connection identifiers.
     */
    private final AtomicLong connectionId = new AtomicLong(0);

    @Inject
    private MessagingConfig messagingConfig;

    @PostConstruct
    public void configure() {
        log.info("Configuring RabbitMQ connection using {}", messagingConfig);
        connectionFactory.setHost(messagingConfig.getHost());
        connectionFactory.setPort(messagingConfig.getPort());
        connectionFactory.setUsername(messagingConfig.getUsername());
        connectionFactory.setPassword(messagingConfig.getPassword());
    }

    /**
     * Helper method to open a RabbitMQ {@link Channel} using a new RabbitMQ {@link Connection}.
     * @return A fresh {@link Channel}.
     * @throws IOException When a RabbitMQ connection or {@link Channel} could not be opened.
     * @throws TimeoutException When a RabbitMQ connection or {@link Channel} could not be opened.
     */
    @SuppressWarnings("java:S2095") // connections are properly closed when the injection point is destroyed.
    private Channel createChannel() throws IOException, TimeoutException {
        try {
            var connection = connectionFactory.newConnection();
            connection.setId(Long.toString(connectionId.incrementAndGet()));
            connection.addShutdownListener(
                cause -> log.debug("Closing connection {} due to {}", connection.getId(), cause.getLocalizedMessage())
            );
            log.debug("Opened RabbitMQ connection {}....", connection.getId());
            return connection.createChannel();
        } catch (TimeoutException | IOException e) {
            log.error("Could not open connection to RabbitMQ due to {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Injects a {@link MessageSender} instance at the given {@code InjectionPoint}.
     * @param injectionPoint The injection point where the {@link MessageSender} is requested.
     * @return The {@link MessageSender} instance.
     * @throws MessagingConfigurationException When a RabbitMQ connection or {@link Channel} could not be opened, or
     * when the queue could not be declared.
     */
    @NamedQueue(queueName = "")
    @Produces
    public MessageSender messageSender(final InjectionPoint injectionPoint) throws MessagingConfigurationException {
        return new DefaultMessageSender(queue(injectionPoint));
    }

    /**
     * Declare a named, non-exclusive, durable, permanent queue.
     * Useful for referring to request queues that may already exist and are potentially shared with other consumers.
     * @param injectionPoint The point where the {@link Queue} is going to be injected.
     * @return A {@link Queue} instance for the declared queue.
     * @throws MessagingConfigurationException When a RabbitMQ connection or {@link Channel} could not be opened, or
     * when the queue could not be declared.
     */
    @NamedQueue(queueName = "")
    @Produces
    public Queue queue(final InjectionPoint injectionPoint) throws MessagingConfigurationException {
        var annotated = injectionPoint.getAnnotated();
        var usesQueue = annotated.getAnnotation(NamedQueue.class);
        var queueName = usesQueue.queueName();
        try {
            var channel = createChannel();
            // Declare the queue we want to send to.
            channel.queueDeclare(queueName, DURABLE, NOT_EXCLUSIVE, NO_AUTO_DELETE, emptyMap());

            return new Queue(channel, queueName);
        } catch (TimeoutException | IOException e) {
            log.error("Could not construct an instance of Queue for queue {}", queueName, e);
            throw new MessagingConfigurationException(e);
        }
    }

    /**
     * Declare a temporary (exclusive for this consumer, auto-deleted when the consumer disconnects, non-durable) queue.
     * Useful for creating response queues that are guaranteed to not conflict with other consumers.
     * @return A {@link Queue} instance for the declared queue.
     * @throws MessagingConfigurationException When a RabbitMQ connection or {@link Channel} could not be opened, or
     * when the queue could not be declared.
     */
    @Produces
    @TemporaryQueue
    public Queue temporaryQueue() throws MessagingConfigurationException {
        try {
            var channel = createChannel();
            // Declare the temporary queue.
            var result = channel.queueDeclare();
            return new Queue(channel, result.getQueue());
        } catch (TimeoutException | IOException e) {
            log.error("Could not declare a temporary queue", e);
            throw new MessagingConfigurationException(e);
        }
    }
}
