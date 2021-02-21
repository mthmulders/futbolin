package it.mulders.futbolin.user;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import io.smallrye.reactive.messaging.amqp.IncomingAmqpMetadata;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.ApplicationScoped;

/**
 * Support class for components that consume incoming Protocol Buffer encoded messages.
 */
@ApplicationScoped
@Slf4j
public class ProtocolBufferMessageConsumerSupport {
    private String extractCorrelationId(final Message<byte[]> message) {
        return message.getMetadata(IncomingAmqpMetadata.class)
                .map(IncomingAmqpMetadata::getCorrelationId)
                .orElse("(unknown)");
    }

    /**
     * Log start of processing a message.
     * @param message The message that is going to be processed.
     */
    public void logIncomingMessage(final Message<byte[]> message) {
        var correlationId = extractCorrelationId(message);
        log.info("Processing incoming message with Correlation ID {}", correlationId);
    }

    /**
     * Extract the Protocol Buffer encoded payload from the {@link Message}, converting it to an object of type
     * {@link T}.
     * @param message The incoming message
     * @return The payload of that message, as an instance of type {@link T}
     * @throws UnprocessableMessageException In case the message payload could not be extracted into an object of
     * type {@link T}.
     */
    public <T> T extractPayload(final Parser<T> parser, final Message<byte[]> message) throws UnprocessableMessageException {
        try {
            return parser.parseFrom(message.getPayload());
        } catch (final InvalidProtocolBufferException ipbe) {
            var correlationId = extractCorrelationId(message);
            throw new UnprocessableMessageException("Could not read incoming message", ipbe, correlationId);
        }
    }
}
