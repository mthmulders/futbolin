package it.mulders.futbolin.user;

import it.mulders.futbolin.messages.UserLoggedIn;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@AllArgsConstructor(onConstructor = @__({ @Inject }))
@ApplicationScoped
@Slf4j
public class UserLoggedInConsumer {
    private final ProtocolBufferMessageConsumerSupport support;

    @Incoming("user-logged-in")
    public CompletionStage<Void> consumeUserLoggedIn(final Message<byte[]> message) throws UnprocessableMessageException {
        support.logIncomingMessage(message);

        var payload = support.extractPayload(UserLoggedIn.parser(), message);

        return message.ack();
    }
}
