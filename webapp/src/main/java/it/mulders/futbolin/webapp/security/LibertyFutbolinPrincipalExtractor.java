package it.mulders.futbolin.webapp.security;

import javax.enterprise.context.RequestScoped;
import javax.security.auth.Subject;

import com.ibm.websphere.security.WSSecurityException;
import com.ibm.websphere.security.auth.WSSubject;
import com.ibm.websphere.security.jwt.Claims;
import com.ibm.websphere.security.social.UserProfile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Set;

/**
 * OpenLiberty-based implementation of obtaining a {@link FutbolinUser} instance.
 *
 * There are a few interactions with hard-to-replace OpenLiberty APIs: {@link WSSubject#getCallerSubject()} as well
 * as the {@link UserProfile} class. Those are captured in dedicated interfaces ({@link CallerSubjectSupplier} and
 * {@link ClaimsSupplier}, respectively) so it's easier to replace them in unit tests.
 *
 * The default constructor for this class provides the actual implementation based on those OpenLiberty APIs.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RequestScoped
@Slf4j
public class LibertyFutbolinPrincipalExtractor implements FutbolinPrincipalExtractor {
    /** Describes how to get the {@link Subject caller subject} when running in OpenLiberty. */
    interface CallerSubjectSupplier {
        Subject getCallerSubject() throws WSSecurityException;
    }

    /** Describes how to get the {@link Claims profile claims} when running in OpenLiberty. */
    interface ClaimsSupplier {
        Optional<Claims> findClaims(final Set<Object> credentials);
    }

    /** Call to {@link WSSubject#getCallerSubject} can be replaced by using the protected constructor. */
    private final CallerSubjectSupplier callerSubjectSupplier;

    /** Interactions with the {@link UserProfile} class (which has dependencies on non-public modules) can be replaced by using the protected constructor. */
    private final ClaimsSupplier claimsSupplier;

    /**
     * Default constructor - not used in tests, but used at runtime.
     */
    public LibertyFutbolinPrincipalExtractor() {
        this(WSSubject::getCallerSubject, LibertyFutbolinPrincipalExtractor::findClaims);
    }

    @Override
    public FutbolinUser extractFutbolinPrincipal() {
        try {
            var subject = this.callerSubjectSupplier.getCallerSubject();
            return toFutbolinPrincipal(subject).orElse(null);
        } catch (WSSecurityException wsse) {
            log.error("Could not retrieve social profile", wsse);
        }
        return null;
    }

    private Optional<FutbolinUser> toFutbolinPrincipal(final Subject subject) {
        var credentials = subject.getPrivateCredentials();
        return claimsSupplier.findClaims(credentials).map(this::toFutbolinPrincipal);
    }

    /**
     * Given a {@link Subject subject} find a set of {@link Claims claims} that describe the subject.
     * @param credentials The OpenLiberty subject that may have additional claims about the subject.
     * @return The {@link Claims claims} that describe the subject.
     */
    private static Optional<Claims> findClaims(final Set<Object> credentials) {
        return credentials.stream()
                .filter(UserProfile.class::isInstance)
                .findFirst()
                .map(UserProfile.class::cast)
                .map(UserProfile::getClaims);
    }

    private FutbolinUser toFutbolinPrincipal(final Claims claims) {
        var id = claims.getClaim("sub", String.class);
        var displayName = claims.getClaim("name", String.class);
        var email = claims.getClaim("email", String.class);

        return DefaultFutbolinUser.builder()
                .id(id)
                .displayName(displayName)
                .email(email)
                .build();
    }
}
