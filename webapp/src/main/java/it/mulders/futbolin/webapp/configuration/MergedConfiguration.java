package it.mulders.futbolin.webapp.configuration;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Merges configuration from a configuration file and environment variables.
 */
@Slf4j
public class MergedConfiguration {
    private final Map<String,String> environment;
    private final Properties configuration;

    public MergedConfiguration(final String filename) {
        this.environment = System.getenv();
        this.configuration = new Properties();

        try (var resource = getClass().getResourceAsStream(filename)) {
            if (resource != null) {
                this.configuration.load(resource);
            } else {
                log.warn("Resource \"{}\" not found on classpath", filename);
            }
        } catch (final IOException ioe) {
            log.error("Failed to read resource \"{}\" from classpath", filename, ioe);
        }
    }

    protected String getValue(final String environmentVariableName, final String configurationKey) {
        return environment.getOrDefault(environmentVariableName, configuration.getProperty(configurationKey));
    }
}
