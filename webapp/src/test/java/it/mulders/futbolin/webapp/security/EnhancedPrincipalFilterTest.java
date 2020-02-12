package it.mulders.futbolin.webapp.security;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockUserPrincipal;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class EnhancedPrincipalFilterTest implements WithAssertions {
    private final FilterChain chain = mock(FilterChain.class);
    private final HttpServletResponse response = new MockHttpServletResponse();
    private final ArgumentCaptor<HttpServletRequest> requestCaptor = ArgumentCaptor.forClass(HttpServletRequest.class);

    private EnhancedPrincipalFilter filter = new EnhancedPrincipalFilter();

    @Test
    void doFilter_withFutbolinPrincipal_shouldNotReplacePrincipal() throws IOException, ServletException {
        var request = new MockHttpServletRequest();
        request.setUserPrincipal(FutbolinPrincipal.builder().build());

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_withoutPrincipal_shouldPassRequest() throws IOException, ServletException {
        var request = new MockHttpServletRequest();

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_withAnotherPrincipal_shouldReplacePrincipal() throws IOException, ServletException {
        var name = "dummy";
        var principal = new MockUserPrincipal(name);
        var request = new MockHttpServletRequest();
        request.setUserPrincipal(principal);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(requestCaptor.capture(), eq(response));

        var capturedRequest = requestCaptor.getValue();
        assertThat(capturedRequest).isNotEqualTo(request);

        var actualPrincipal = capturedRequest.getUserPrincipal();
        assertThat(actualPrincipal).isInstanceOf(FutbolinPrincipal.class);
        assertThat(actualPrincipal.getName()).isEqualTo(name);
    }

    @Test
    void doFilter_withAnotherPrincipal_shouldPreservePrincipalName() throws IOException, ServletException {
        var principal = new MockUserPrincipal("dummy");
        var request = new MockHttpServletRequest();
        request.setUserPrincipal(principal);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(requestCaptor.capture(), eq(response));

        var capturedRequest = requestCaptor.getValue();
        var actualPrincipal = capturedRequest.getUserPrincipal();
        assertThat(actualPrincipal.getName()).isEqualTo(principal.getName());
    }
}