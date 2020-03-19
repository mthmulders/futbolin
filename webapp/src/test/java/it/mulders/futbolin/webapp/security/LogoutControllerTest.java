package it.mulders.futbolin.webapp.security;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LogoutControllerTest implements WithAssertions {
    private LogoutController controller = new LogoutController();

    @Test
    void should_logout() throws ServletException {
        // Can't use MockRunner Servlet, it doesn't follow version 3.0+ of the Servlet Spec that describes the
        // logout() method on HttpServletRequest.
        var request = mock(HttpServletRequest.class);

        controller.logout(request);

        verify(request).logout();
    }
}