package it.mulders.futbolin.webapp.messaging;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@AllArgsConstructor
@Slf4j
public class DefaultMessageReceiver implements MessageReceiver {
    @AllArgsConstructor
    private class CorrelationIdDeliveryCallback implements DeliverCallback {
        private static final boolean SINGLE_DELIVERY = false;

        private final CompletableFuture<byte[]> result;
        private final String sentCorrelationId;

        @Override
        public void handle(final String consumerTag, final Delivery message) throws IOException {
            var deliveryTag = message.getEnvelope().getDeliveryTag();
            var receivedCorrelationId = message.getProperties().getCorrelationId();
            if (sentCorrelationId.equals(receivedCorrelationId)) {
                log.debug("Response for message {} received", sentCorrelationId);
                result.complete(message.getBody());

                queue.channel.basicAck(deliveryTag, SINGLE_DELIVERY);
            } else {
                queue.channel.basicReject(deliveryTag, SINGLE_DELIVERY);
            }
        }
    }

    private static final CancelCallback cancelCallback = consumerTag -> {
        // Nothing to do
    };

    private final Queue queue;

    @Override
    public CompletionStage<byte[]> awaitResponse(final String correlationId) {
        var result = new CompletableFuture<byte[]>();
        var deliverCallback = new CorrelationIdDeliveryCallback(result, correlationId);

        try {
            log.debug("Sent message {}", correlationId);
            var consumer = queue.channel.basicConsume(queue.name, deliverCallback, cancelCallback);
            result.thenRun(() -> {
                try {
                    queue.channel.basicCancel(consumer);
                } catch (IOException e) {
                    log.error("Could not cancel RabbitMQ listener {}", consumer, e);
                }
            });
        } catch (IOException e) {
            log.error("Could not consume message(s) from RabbitMQ", e);
            result.completeExceptionally(new MessagingException(e));
        }

        return result;
    }
}
