package it.mulders.futbolin.webapp.messaging;

/**
 * Exception signalling that something went wrong in configuring the messaging framework.
 */
public class MessagingConfigurationException extends MessagingException {
    public MessagingConfigurationException(Throwable cause) {
        super(cause);
    }
}
