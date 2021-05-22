package it.mulders.futbolin.webapp.security;

/**
 * Strategy for extracting a {@link FutbolinUser}.
 */
public interface FutbolinPrincipalExtractor {
    FutbolinUser extractFutbolinPrincipal();
}
