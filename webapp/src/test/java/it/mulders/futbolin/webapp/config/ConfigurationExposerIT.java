package it.mulders.futbolin.webapp.config;

import org.assertj.core.api.WithAssertions;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.inject.Named;

class ConfigurationExposerIT implements WithAssertions {
    private static WeldContainer container;
    private static Dummy dummy;

    @Named
    static class Dummy {
        @Inject
        @Config("an.example")
        private String existing;

        @Inject
        @Config("another.example")
        private String nonExisting;
    }

    @BeforeAll
    public static void setup() {
        var weld = new Weld()
                .disableDiscovery()
                .addBeanClass(Dummy.class)
                .addBeanClass(ConfigurationExposer.class);
        container = weld.initialize();
        dummy = container.select(Dummy.class).get();
    }

    @AfterAll
    public static void close() {
        container.shutdown();
    }

    @Test
    void should_inject_existing_values_from_property_file() {
        assertThat(dummy.existing).isEqualTo("1");
    }

    @Test
    void should_not_inject_non_existing_values_from_property_file() {
        assertThat(dummy.nonExisting).isNull();
    }
}