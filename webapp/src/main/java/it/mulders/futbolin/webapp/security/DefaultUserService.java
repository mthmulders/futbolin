package it.mulders.futbolin.webapp.security;

import it.mulders.futbolin.messages.Queues;
import it.mulders.futbolin.messages.UserLoggedIn;
import it.mulders.futbolin.webapp.messaging.MessageSender;
import it.mulders.futbolin.webapp.messaging.MessagingException;
import it.mulders.futbolin.webapp.messaging.NamedQueue;
import it.mulders.futbolin.webapp.messaging.RequestEnvelope;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static it.mulders.futbolin.messages.ProtobufTimestampUtils.now;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@ApplicationScoped
@NoArgsConstructor
@Slf4j
public class DefaultUserService implements UserService {
    @Inject
    @NamedQueue(queueName = Queues.USER_LOGGED_IN)
    private MessageSender sender;

    @Override
    public void notifyUserLoggedIn(final FutbolinUser principal) throws MessagingException {
        var message = buildEnvelope(principal);
        log.info("Registering login of user {}", principal.getId());
        sender.sendMessage(message);
    }

    private RequestEnvelope buildEnvelope(final FutbolinUser principal) {
        var message = UserLoggedIn.newBuilder()
                .setDisplayName(principal.getDisplayName())
                .setEmail(principal.getEmail())
                .setId(principal.getId())
                .setTimestamp(now())
                .build();

        return new RequestEnvelope(message.toByteArray());
    }
}
