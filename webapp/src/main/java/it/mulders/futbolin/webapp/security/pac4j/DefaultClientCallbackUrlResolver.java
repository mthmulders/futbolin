package it.mulders.futbolin.webapp.security.pac4j;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.http.callback.CallbackUrlResolver;
import org.pac4j.core.http.url.UrlResolver;

/**
 * Implementation of {@link CallbackUrlResolver} that assumes there is just one Pac4J client.
 */
public class DefaultClientCallbackUrlResolver implements CallbackUrlResolver {
    @Override
    public String compute(final UrlResolver urlResolver, final String url, final String clientName, final WebContext context) {
        // No need to append a client name to the URL.
        return urlResolver.compute(url, context);
    }

    @Override
    public boolean matches(final String clientName, final WebContext context) {
        // Since there is just one client, every callback URL is handled by the specified client.
        return true;
    }
}
