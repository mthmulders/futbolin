package it.mulders.futbolin.webapp.security;

import com.google.protobuf.InvalidProtocolBufferException;
import it.mulders.futbolin.messages.UserLoggedIn;
import it.mulders.futbolin.webapp.messaging.MessageSender;
import it.mulders.futbolin.webapp.messaging.MessagingException;
import it.mulders.futbolin.webapp.messaging.RequestEnvelope;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static it.mulders.futbolin.messages.Assertions.assertThat;

class DefaultUserServiceTest {
    private final MessageSender sender = mock(MessageSender.class);
    private final DefaultUserService service = new DefaultUserService(sender);

    @Test
    void should_send_user_logged_in_message() throws MessagingException, InvalidProtocolBufferException {
        // Arrange
        var user = DefaultFutbolinUser.builder()
                .id(UUID.randomUUID().toString())
                .displayName("Test User")
                .email("noreply@example.com")
                .build();

        // Act
        service.notifyUserLoggedIn(user);

        // Assert
        var captor = ArgumentCaptor.forClass(RequestEnvelope.class);
        verify(sender).sendMessage(captor.capture());

        var message = UserLoggedIn.parseFrom(captor.getValue().message);

        assertThat(message)
                .hasDisplayName(user.getDisplayName())
                .hasEmail(user.getEmail())
                .hasId(user.getId())
                .hasTimestamp();
    }
}