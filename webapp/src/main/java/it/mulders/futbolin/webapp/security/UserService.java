package it.mulders.futbolin.webapp.security;

import it.mulders.futbolin.webapp.messaging.MessagingException;

/**
 * Service for user-related functionality.
 */
public interface UserService {
    /**
     * Signals that a user has logged in.
     * @param principal The user that logged in.
     * @throws MessagingException In case of trouble.
     */
    void notifyUserLoggedIn(final FutbolinUser principal) throws MessagingException;
}
