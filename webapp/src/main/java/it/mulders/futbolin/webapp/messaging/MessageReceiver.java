package it.mulders.futbolin.webapp.messaging;

import java.util.concurrent.CompletionStage;

/**
 * Component that waits for messages on a temporary queue.
 * As soon as the first message comes in with the given {code correlationId}, it comples the
 */
public interface MessageReceiver {
    /**
     * Wait for a message to come in that matches the {@code correlationId}
     * @param correlationId The unique identifier to match response messages with their request message.
     * @return A future {code byte[]} value with the response message.
     */
    CompletionStage<byte[]> awaitResponse(final String correlationId);
}
