package it.mulders.futbolin.user;

import lombok.Getter;

/**
 * Signals that an incoming message could not be processed.
 */
@Getter
public class UnprocessableMessageException extends Exception {
    private final String correlationId;

    public UnprocessableMessageException(final String message, final Throwable cause, final String correlationId) {
        super(message, cause);
        this.correlationId = correlationId;
    }
}
