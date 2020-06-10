package it.mulders.futbolin.webapp.security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.util.function.Function;

/**
 * Copies the {@link java.security.Principal} from the {@link HttpServletRequest} to the JAX-RS
 * {@link javax.ws.rs.core.SecurityContext}.
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Provider
@Priority(Priorities.AUTHENTICATION)
public class FutbolinSecurityContextFilter implements ContainerRequestFilter {
    private static final String UNAUTHENTICATED = "UNAUTHENTICATED";

    @Context
    UriInfo uriInfo;

    @Context
    HttpServletRequest request;

    @Override
    public void filter(final ContainerRequestContext context) {
        if (!isAuthenticated(context)) {
            return;
        }

        var principal = (FutbolinUser) request.getUserPrincipal();
        var roleChecker = (Function<String, Boolean>) context.getSecurityContext()::isUserInRole;
        var secure = uriInfo.getAbsolutePath().toString().startsWith("https");

        var securityContext = new FutbolinSecurityContext(principal, roleChecker, secure);
        context.setSecurityContext(securityContext);
    }

    private boolean isAuthenticated(final ContainerRequestContext context) {
        var securityContext = context.getSecurityContext();
        var userPrincipal = securityContext.getUserPrincipal();
        return userPrincipal != null && !UNAUTHENTICATED.equals(userPrincipal.getName());
    }

}
