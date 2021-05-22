package it.mulders.futbolin.webapp.messaging;

import com.rabbitmq.client.AMQP;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class DefaultMessageSender implements MessageSender {
    private static final int PERSISTENT = 2;

    private final Queue queue;

    @Override
    public void sendMessage(final RequestEnvelope request) throws MessagingException {
        final var properties = new AMQP.BasicProperties.Builder()
                .correlationId(request.correlationId)
                .deliveryMode(PERSISTENT)
                .replyTo(request.responseQueueName)
                .build();
        try {
            queue.channel.basicPublish("", queue.name, properties, request.message);
        } catch (IOException e) {
            throw new MessagingException(e);
        }
    }
}
