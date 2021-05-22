package it.mulders.futbolin.webapp.security;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * When the request comes from an authenticated user, this filter replaces the default {@link Principal}
 * on that request with an instance of {@link FutbolinUser}.
 *
 * Computing the  {@link FutbolinUser} instance involves a few calls to a proprietary OpenLiberty API. Since
 * their performance impact is unknown, the result of those calls is cached in {@link #userCache}.
 */
@Slf4j
@WebFilter(urlPatterns = "/app/*")
public class EnhancedPrincipalFilter extends HttpFilter {
    private final transient Map<Principal, FutbolinUser> userCache = new HashMap<>();

    @Inject
    FutbolinPrincipalExtractor extractor;

    @Override
    public void init(final FilterConfig filterConfig) {
        // Nothing to do here.
    }

    @Override
    protected void doFilter(final HttpServletRequest request,
                            final HttpServletResponse response,
                            final FilterChain chain) throws IOException, ServletException {
        var originalPrincipal = request.getUserPrincipal();

        if (originalPrincipal == null) {
            log.debug("Request does not have user principal, continuing");
            chain.doFilter(request, response);
            return;
        }

        var replacedPrincipal = userCache.computeIfAbsent(originalPrincipal, this::createFutbolinPrincipal);

        var wrappedRequest = new HttpServletRequestWrapper(request) {
            @Override
            public Principal getUserPrincipal() {
                return replacedPrincipal;
            }
        };

        chain.doFilter(wrappedRequest, response);
    }

    private FutbolinUser createFutbolinPrincipal(final Principal originalPrincipal) {
        return extractor.extractFutbolinPrincipal();
    }

    @Override
    public void destroy() {
        this.userCache.clear();
    }
}
