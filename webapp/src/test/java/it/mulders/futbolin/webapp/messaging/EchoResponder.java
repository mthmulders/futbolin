package it.mulders.futbolin.webapp.messaging;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.ShutdownSignalException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

import static it.mulders.futbolin.webapp.messaging.EchoService.QUEUE_NAME;
import static java.nio.charset.StandardCharsets.UTF_8;

@ApplicationScoped
@Slf4j
public class EchoResponder {
    private static final int PERSISTENT = 2;

    @Getter
    @Inject
    @NamedQueue(queueName = QUEUE_NAME)
    private Queue queue;

    public EchoResponder() {
        log.debug("EchoResponder created");
    }

    private final CancelCallback cancelCallback = consumerTag -> {
        // Nothing to do
    };

    private String consumerTag;

    @PostConstruct
    public void configure() throws IOException {
        log.debug("Configuring responder on queue {}", queue.name);
        consumerTag = queue.channel.basicConsume(queue.name, (consumerTag, message) -> {
            var correlationId = message.getProperties().getCorrelationId();
            var replyTo = message.getProperties().getReplyTo();
            log.debug("Incoming message with correlationId {} on queue {}", correlationId, replyTo);

            var response = "Response to " + new String(message.getBody(), UTF_8);

            final var properties = new AMQP.BasicProperties.Builder()
                    .correlationId(correlationId)
                    .deliveryMode(PERSISTENT)
                    .build();

            queue.channel.basicPublish("", replyTo, properties, response.getBytes(UTF_8));
            log.debug("Response message sent");

        }, cancelCallback);

        queue.channel.addShutdownListener(this::silentlyCloseConsumer);
    }

    private void silentlyCloseConsumer(final ShutdownSignalException exception) {
        var channel = queue.channel.getChannelNumber();
        var cause = exception.getLocalizedMessage();
        try {
            log.debug("Channel {} is closing due to {}, cancelling consumer {}", channel, cause, consumerTag);
            queue.channel.basicCancel(consumerTag);
        } catch (IOException e) {
            log.error("Could not properly cancel consumer {}", consumerTag, e);
        }
    }
}
