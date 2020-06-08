package it.mulders.futbolin.webapp.messaging;

/**
 * Component that sends out messages over a message bus.
 */
public interface MessageSender {
    /**
     * Publish a message on the message bus that this component is configured with.
     * @param request A request envelope, containing both the metadata about the message and the message body itself.
     * @throws MessagingException In case publishing the message fails.
     */
    void sendMessage(final RequestEnvelope request) throws MessagingException;
}
