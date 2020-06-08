package it.mulders.futbolin.webapp.messaging;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This qualifier signals that an injection point for a {@link Queue} should refer to a named queue on the message bus.
 */
@Qualifier
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface NamedQueue {
    /**
     * The name of the queue to bind to.
     */
    @Nonbinding String queueName();
}
