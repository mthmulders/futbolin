package it.mulders.futbolin.webapp.configuration;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class MergedConfigurationTest implements WithAssertions {
    @Test
    void constructor_shouldNotFailForNonExistingFile() {
        new MergedConfiguration("/example2.properties");
    }

    @Test
    void getValue_shouldReadFromProperties() {
        var configuration = new MergedConfiguration("/example.properties");
        assertThat(configuration.getValue("", "an.example")).isEqualTo("1");
    }

    @Test
    void getValue_shouldReadFromProperties_nonExistingKey() {
        var configuration = new MergedConfiguration("/example.properties");
        assertThat(configuration.getValue("", "another.example")).isNull();
    }

    @Test
    void getValue_shouldReadFromEnvironment() {
        var configuration = new MergedConfiguration("/example.properties");
        assertThat(configuration.getValue("PATH", "an.example")).isNotEqualTo("1");
    }

    @Test
    void getValue_shouldReadFromEnvironment_nonExistingKey() {
        var configuration = new MergedConfiguration("/example.properties");
        assertThat(configuration.getValue("PATH", "another.example")).isNotNull();
    }
}