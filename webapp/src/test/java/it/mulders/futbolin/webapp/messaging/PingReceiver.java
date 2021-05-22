package it.mulders.futbolin.webapp.messaging;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.ShutdownSignalException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

import static it.mulders.futbolin.webapp.messaging.PingService.QUEUE_NAME;

@ApplicationScoped
@Slf4j
public class PingReceiver {
    @Getter
    @Inject
    @NamedQueue(queueName = QUEUE_NAME)
    private Queue queue;

    private String consumerTag;

    private final CancelCallback cancelCallback = consumerTag -> {
        // Nothing to do
    };

    @Getter
    private final ArrayBlockingQueue<Delivery> receivedMessages = new ArrayBlockingQueue<>(10);

    @PostConstruct
    public void configure() throws IOException {
        log.debug("Configuring responder on queue {}", queue.name);
        consumerTag = queue.channel.basicConsume(queue.name, (consumerTag, message) -> {
            var correlationId = message.getProperties().getCorrelationId();
            var replyTo = message.getProperties().getReplyTo();
            log.debug("Incoming message with correlationId {} on queue {}", correlationId, replyTo);
            receivedMessages.offer(message);
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
