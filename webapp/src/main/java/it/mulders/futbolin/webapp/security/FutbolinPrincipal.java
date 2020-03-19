package it.mulders.futbolin.webapp.security;

import lombok.Builder;
import lombok.Getter;

import java.security.Principal;

/**
 * Custom {@link Principal} for use inside this application. Placeholder so that we could later add the full user
 * profile in here.
 */
@Builder
@Getter
public class FutbolinPrincipal implements Principal {
    private final String id;
    private final String displayName;
    private final String email;

    @Override
    public String getName() {
        return this.displayName;
    }
}
