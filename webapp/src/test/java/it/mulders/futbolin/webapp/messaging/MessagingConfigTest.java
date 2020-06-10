package it.mulders.futbolin.webapp.messaging;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class MessagingConfigTest implements WithAssertions {
    @Test
    void toString_shouldNotIncludePassword() {
        var sut = new MessagingConfig("localhost", 5672, "guest", "secret");
        assertThat(sut.toString()).doesNotContain("secret");
    }
}