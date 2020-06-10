package it.mulders.futbolin.webapp.security;

import lombok.AllArgsConstructor;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.function.Function;

@AllArgsConstructor
public class FutbolinSecurityContext implements SecurityContext {
    private final FutbolinUser principal;
    private final Function<String, Boolean> roleChecker;
    private final boolean secure;

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(final String role) {
        return roleChecker.apply(role);
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "OIDC";
    }
}
