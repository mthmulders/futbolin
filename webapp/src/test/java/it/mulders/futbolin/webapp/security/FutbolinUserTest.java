package it.mulders.futbolin.webapp.security;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class FutbolinUserTest implements WithAssertions  {
    private DefaultFutbolinUser principal = DefaultFutbolinUser.builder()
            .displayName("Display Name")
            .email("no-reply@example.com")
            .id("8957348581491")
            .build();

    @Test
    void toString_shouldOnlyIncludeId() {
        assertThat(principal.toString()).contains("8957348581491");
        assertThat(principal.toString()).doesNotContain("Display Name", "no-reply@example.com");
    }

    @Test
    void getDisplayName_shouldReturnDisplayName() {
        assertThat(principal.getDisplayName()).isEqualTo("Display Name");
    }

    @Test
    void getName_shouldReturnDisplayName() {
        assertThat(principal.getName()).isEqualTo("Display Name");
    }

    @Test
    void getId_shouldReturnId() {
        assertThat(principal.getId()).isEqualTo("8957348581491");
    }

    @Test
    void getEmail_shouldReturnEmail() {
        assertThat(principal.getEmail()).isEqualTo("no-reply@example.com");
    }
}