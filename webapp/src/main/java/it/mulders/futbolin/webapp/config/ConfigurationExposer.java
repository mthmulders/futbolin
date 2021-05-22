package it.mulders.futbolin.webapp.config;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Simple CDI producer that reads properties from a classpath resource.
 *
 * @see <a href="https://www.amazon.com/Architecting-Modern-Java-Applications-business-oriented-ebook/dp/B0761YQNRM">
 *     "Architecting Modern Java EE Applications" by Sebastian Daschner.
 *     </a>
 */
@ApplicationScoped
@Slf4j
public class ConfigurationExposer {
    private static final String CONFIG_PATH_VARIABLE = "CONFIGURATION_FILE";
    private final Properties properties = new Properties();

    @PostConstruct
    public void loadProperties() {
        loadProperties(System.getenv(CONFIG_PATH_VARIABLE));
    }

    // Visible for testing
    void loadProperties(final String configFileLocation) {
        if (configFileLocation == null) {
            log.error("Could not init configuration. Specify a path to the configuration file with the {} environment variable", CONFIG_PATH_VARIABLE);
            throw new IllegalArgumentException("Could not init configuration, no configuration file given");
        }

        log.info("Loading configuration file {}", configFileLocation);
        try (var input = Files.newInputStream(Paths.get(configFileLocation))) {
            properties.load(input);
            log.info("{} configuration value(s) loaded from {}", properties.size(), configFileLocation);
        } catch (IOException e) {
            log.error("Could not init configuration from {}", configFileLocation, e);
            throw new IllegalArgumentException("Could not init configuration, " + configFileLocation + " could not be read", e);
        }
    }

    @Produces
    @Config("")
    public String exposeConfigAsString(final InjectionPoint injectionPoint) {
        var config = injectionPoint.getAnnotated().getAnnotation(Config.class);
        if (config != null) {
            return properties.getProperty(config.value());
        }
        log.warn("Could not inject value for config property {}", injectionPoint);
        return null;
    }

    @Produces
    @Config("")
    public Integer exposeConfigAsInteger(final InjectionPoint injectionPoint) {
        var value = exposeConfigAsString(injectionPoint);
        try {
            return Integer.parseInt(value, 10);
        } catch (final NumberFormatException nfe) {
            log.warn("Value {} for injection point {} could not be converted to an Integer", value, injectionPoint);
        }
        return null;
    }
}
