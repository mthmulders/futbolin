package it.mulders.futbolin.webapp.config;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.io.IOException;
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
    private static final String CONFIG_PATH = "/application.properties";
    private final Properties properties = new Properties();

    @PostConstruct
    public void loadProperties() {
        try (var input = ConfigurationExposer.class.getResourceAsStream(CONFIG_PATH)) {
            properties.load(input);
            log.info("{} configuration value(s) loaded from {}", properties.size(), CONFIG_PATH);
        } catch (IOException e) {
            log.error("Could not init configuration from {}", CONFIG_PATH, e);
            throw new IllegalStateException("Could not init configuration", e);
        }
    }

    @Produces
    @Config("")
    public String exposeConfig(final InjectionPoint injectionPoint) {
        var config = injectionPoint.getAnnotated().getAnnotation(Config.class);
        if (config == null) {
            return properties.getProperty(config.value());
        }
        log.warn("Could not inject value for config property {}", injectionPoint);
        return null;
    }
}
