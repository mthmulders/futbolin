package it.mulders.futbolin.webapp.security;

import it.mulders.futbolin.webapp.messaging.MessagingException;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LogonControllerTest implements WithAssertions {
    private final UserService userService = mock(UserService.class);

    private final LogonController controller = new LogonController(userService);

    @Test
    void onLogin_shouldPublishEvent() throws MessagingException {
        // Arrange
        var user = DefaultFutbolinUser.builder().build();

        // Act
        controller.login(MockSecurityContext.loggedInWith(user));

        // Assert
        verify(userService).notifyUserLoggedIn(user);
    }
}