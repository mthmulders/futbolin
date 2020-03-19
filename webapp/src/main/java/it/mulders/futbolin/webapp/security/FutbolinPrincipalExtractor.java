package it.mulders.futbolin.webapp.security;

/**
 * Strategy for extracting a {@link FutbolinPrincipal}.
 */
public interface FutbolinPrincipalExtractor {
    FutbolinPrincipal extractFutbolinPrincipal();
}
