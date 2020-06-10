package it.mulders.futbolin.webapp.security;

import com.mockrunner.mock.web.MockHttpServletRequest;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FutbolinSecurityContextFilterTest implements WithAssertions {
    private final UriInfo uriInfo = mock(UriInfo.class);
    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final FutbolinSecurityContextFilter filter = new FutbolinSecurityContextFilter(uriInfo, request);

    @BeforeEach
    void configure() throws URISyntaxException {
        when(uriInfo.getAbsolutePath()).thenReturn(new URI("http://localhost:8080"));
    }

    @Test
    void whenAuthenticated_shouldCopyPrincipalFromRequestToJaxRsContext() {
        // Arrange
        var principal = DefaultFutbolinUser.builder().build();
        request.setUserPrincipal(principal);
        var requestContext = mock(ContainerRequestContext.class);
        var securityContext = mock(SecurityContext.class);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        when(securityContext.getUserPrincipal()).thenReturn(() -> "WSPrincipal(nobody@example.com)");

        // Act
        filter.filter(requestContext);

        // Assert
        var scCaptor = ArgumentCaptor.forClass(SecurityContext.class);
        verify(requestContext).setSecurityContext(scCaptor.capture());
        assertThat(scCaptor.getValue().getUserPrincipal()).isEqualTo(principal);
    }

    @Test
    void whenNotAuthenticated_shouldNotCopyPrincipalFromRequestToJaxRsContext() {
        // Arrange
        var principal = DefaultFutbolinUser.builder().build();
        request.setUserPrincipal(principal);
        var requestContext = mock(ContainerRequestContext.class);
        var securityContext = mock(SecurityContext.class);
        when(requestContext.getSecurityContext()).thenReturn(securityContext);
        when(securityContext.getUserPrincipal()).thenReturn(() -> "UNAUTHENTICATED");

        // Act
        filter.filter(requestContext);

        // Assert
        verify(requestContext, never()).setSecurityContext(any());
    }
}