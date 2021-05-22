package it.mulders.futbolin.webapp.security;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import javax.security.auth.Subject;

import static java.util.Optional.of;

class LibertyFutbolinUserExtractorTest implements WithAssertions {
    private LibertyFutbolinPrincipalExtractor extractor;

    void setup(final String displayName, final String subject) {
        var claims = MockClaims.builder()
                .name(displayName)
                .subject(subject)
                .build();

        extractor  = new LibertyFutbolinPrincipalExtractor(Subject::new, s -> of(claims));
    }

    @Test
    void shouldExtractDisplayName() {
        setup("Display Name", null);

        var result = extractor.extractFutbolinPrincipal();

        assertThat(result.getDisplayName()).isEqualTo("Display Name");
    }

    @Test
    void shouldExtractSubject() {
        setup(null, "192837645");

        var result = extractor.extractFutbolinPrincipal();

        assertThat(result.getId()).isEqualTo("192837645");
    }

    @Test
    void shouldExtractEmail() {
        setup(null, "192837645");

        var result = extractor.extractFutbolinPrincipal();

        assertThat(result.getId()).isEqualTo("192837645");
    }
}