package it.mulders.futbolin.messages;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static java.time.temporal.ChronoUnit.MILLIS;

class ProtobufTimestampUtilsTest implements WithAssertions {
    @Test
    void now_should_create_timestamp() {
        var before = LocalDateTime.now().minus(5, MILLIS);
        var result = ProtobufTimestampUtils.now();
        var after = LocalDateTime.now().plus(5, MILLIS);

        var zoneOffset = OffsetDateTime.now().getOffset();
        var converted = LocalDateTime.ofEpochSecond(result.getSeconds(), result.getNanos(), zoneOffset);

        assertThat(converted).isBetween(before, after);
    }
}