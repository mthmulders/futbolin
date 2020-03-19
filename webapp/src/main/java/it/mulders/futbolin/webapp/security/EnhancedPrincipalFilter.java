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

@Slf4j
@WebFilter(urlPatterns = "/app/*")
public class EnhancedPrincipalFilter extends HttpFilter {
    private final transient Map<Principal, FutbolinPrincipal> userCache = new HashMap<>();

    @Inject FutbolinPrincipalExtractor extractor;

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
        } else if (originalPrincipal instanceof FutbolinPrincipal) {
            log.debug("Request user principal is already of expected type, continuing");
            chain.doFilter(request, response);
            return;
        }

        userCache.computeIfAbsent(originalPrincipal, this::createFutbolinPrincipal);

        var replacedPrincipal = userCache.get(originalPrincipal);

        var wrappedRequest = new HttpServletRequestWrapper(request) {
            @Override
            public Principal getUserPrincipal() {
                return replacedPrincipal;
            }
        };

        chain.doFilter(wrappedRequest, response);
    }

    private FutbolinPrincipal createFutbolinPrincipal(final Principal originalPrincipal) {
        return extractor.extractFutbolinPrincipal();
    }

    @Override
    public void destroy() {
        this.userCache.clear();
    }
}
