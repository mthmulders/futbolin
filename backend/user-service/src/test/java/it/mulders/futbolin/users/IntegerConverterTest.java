package it.mulders.futbolin.users;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class IntegerConverterTest {
    private IntegerConverter converter = new IntegerConverter();

    @Test
    void shouldConvertValidValue() {
        assertThat(converter.convert("10")).isEqualTo(10);
    }

    @Test
    void shouldNotConvertIllegalValue() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            converter.convert("a");
        });
    }
}