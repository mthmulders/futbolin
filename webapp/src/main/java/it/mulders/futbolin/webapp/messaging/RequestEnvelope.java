package it.mulders.futbolin.webapp.messaging;

import lombok.AllArgsConstructor;

import java.util.UUID;

/**
 * Envelope for requests messages sent over a message bus.
 */
@AllArgsConstructor
public class RequestEnvelope {
    /** An unique identifier for the message. The response message will have the same correlationId, so they can be matched. */
    public final String correlationId;
    /** Name of the queue where the consumer expects the response message to be published. */
    public final String responseQueueName;
    /** Raw content of the message. */
    public final byte[] message;

    /**
     * Convenience constructor for "fire-and-forget" message style, where the consumer does not expect a response
     * message.
     * @param message The bytes of the request message.
     */
    public RequestEnvelope(final byte[] message) {
        this(UUID.randomUUID().toString(), null, message);
    }
}
