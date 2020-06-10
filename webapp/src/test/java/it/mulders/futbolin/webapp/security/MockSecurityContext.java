package it.mulders.futbolin.webapp.security;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
@Getter
public class MockSecurityContext implements SecurityContext {
    private final Principal userPrincipal;
    private final List<String> roles;
    private final boolean isSecure;
    private final String authenticationScheme = "MOCK";

    @Override
    public boolean isUserInRole(final String role) {
        return roles.contains(role);
    }

    public static SecurityContext notLoggedIn() {
        return MockSecurityContext.builder()
                .build();
    }

    public static SecurityContext loggedIn() {
        var principal = DefaultFutbolinUser.builder().build();
        return MockSecurityContext.builder()
                .userPrincipal(principal)
                .build();
    }
}
