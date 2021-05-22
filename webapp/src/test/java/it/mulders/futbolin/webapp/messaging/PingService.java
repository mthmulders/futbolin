package it.mulders.futbolin.webapp.messaging;

import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
@Slf4j
public class PingService {
    public static final String QUEUE_NAME = "Ping";

    @Inject
    @NamedQueue(queueName = QUEUE_NAME)
    private MessageSender sender;

    protected byte[] convertRequestToBytes(final String request) {
        return request.getBytes(StandardCharsets.UTF_8);
    }

    public void ping(final String request) throws MessagingException {
        var message = buildEnvelope(request);
        sender.sendMessage(message);
    }

    private RequestEnvelope buildEnvelope(final String request) {
        var message = convertRequestToBytes(request);

        return new RequestEnvelope(null, null, message);
    }
}
