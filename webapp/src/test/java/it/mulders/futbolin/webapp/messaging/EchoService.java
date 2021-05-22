package it.mulders.futbolin.webapp.messaging;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
@Slf4j
public class EchoService {
    public static final String QUEUE_NAME = "Echo";

    @Inject
    @NamedQueue(queueName = QUEUE_NAME)
    private MessageSender sender;

    @Getter
    @Inject
    @TemporaryQueue
    private Queue responseQueue;

    protected byte[] convertRequestToBytes(final String request) {
        return request.getBytes(StandardCharsets.UTF_8);
    }

    protected String convertBytesToResponse(final byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public CompletionStage<String> echo(final String request) throws MessagingException {
        var message = buildEnvelope(request, responseQueue);
        var receiver = new DefaultMessageReceiver(responseQueue);
        var response = receiver.awaitResponse(message.correlationId);
        sender.sendMessage(message);

        return response.thenApply(this::convertBytesToResponse);
    }

    private RequestEnvelope buildEnvelope(final String request, final Queue responseQueue) {
        var correlationId = UUID.randomUUID().toString();
        var message = convertRequestToBytes(request);

        return new RequestEnvelope(correlationId, responseQueue.name, message);
    }
}
