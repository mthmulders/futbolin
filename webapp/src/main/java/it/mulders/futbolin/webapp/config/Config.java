package it.mulders.futbolin.webapp.config;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that a String value must be injected from a configuration file.
 * @see ConfigurationExposer
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface Config {
    /**
     * The key in the configuration file that feeds this injection point.
     * @return A configuration file key.
     */
    @Nonbinding
    String value();
}
