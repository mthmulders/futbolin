package it.mulders.futbolin.webapp.messaging;

/**
 * Generic exception signalling that something went wrong in publishing a message or receiving one.
 */
public class MessagingException extends Exception {
    public MessagingException(final Throwable cause) {
        super(cause);
    }
}
