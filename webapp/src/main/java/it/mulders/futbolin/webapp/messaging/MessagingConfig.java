package it.mulders.futbolin.webapp.messaging;

import it.mulders.futbolin.webapp.config.Config;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static lombok.AccessLevel.PACKAGE;

/**
 * Value class to hold configuration values for connecting to RabbitMQ.
 */
@AllArgsConstructor(access = PACKAGE)
@ApplicationScoped
@NoArgsConstructor
@Getter
@ToString(exclude = "password")
public class MessagingConfig {
    @Inject
    @Config("rabbitmq.host")
    private String host;

    @Inject
    @Config("rabbitmq.port")
    private Integer port;

    @Inject
    @Config("rabbitmq.user")
    private String username;

    @Inject
    @Config("rabbitmq.password")
    private String password;
}
