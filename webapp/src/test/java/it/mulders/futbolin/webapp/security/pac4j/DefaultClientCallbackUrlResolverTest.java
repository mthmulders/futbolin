package it.mulders.futbolin.webapp.security.pac4j;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.pac4j.core.http.url.UrlResolver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefaultClientCallbackUrlResolverTest implements WithAssertions {
    private DefaultClientCallbackUrlResolver callbackUrlResolver = new DefaultClientCallbackUrlResolver();

    @Test
    void compute_shouldNotIncludeClientNameInCallbackUrl() {
        var clientName = "whatever";
        var urlResolver = mock(UrlResolver.class);
        when(urlResolver.compute(any(), any())).thenReturn("/cb");
        var baseUrl = "/base";


        var result = callbackUrlResolver.compute(urlResolver, baseUrl, clientName, null);
        assertThat(result)
                .doesNotContain(clientName)
                .contains("/cb");
        verify(urlResolver).compute(baseUrl, null);
    }

    @Test
    void matches_shouldAcceptAnyClient() {
        assertThat(callbackUrlResolver.matches("whatever", null)).isTrue();
    }
}