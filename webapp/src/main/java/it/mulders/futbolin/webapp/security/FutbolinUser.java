package it.mulders.futbolin.webapp.security;

import java.security.Principal;

/**
 * Describes authenticated users of the system.
 */
public interface FutbolinUser extends Principal {
    /** User-friendly name, suitable for display in user interfaces. */
    String getDisplayName();
    /** Email address of the user. */
    String getEmail();
    /** Opaque user identifier, guaranteed to be unique. */
    String getId();

    @Override
    default String getName() {
        return getDisplayName();
    }
}
