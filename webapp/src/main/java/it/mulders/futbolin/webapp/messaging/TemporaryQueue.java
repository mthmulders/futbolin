package it.mulders.futbolin.webapp.messaging;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This qualifier signals that an injection point for a {@link Queue} should refer to a temporary queue on the message bus.
 * Such a queue is exclusively available for the creator to consume, and it is non-durable. When the consumer quites or
 * the connection closes, the temporary queue will be deleted.
 */
@Qualifier
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface TemporaryQueue {
}
