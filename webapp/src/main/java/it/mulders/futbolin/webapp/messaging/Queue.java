package it.mulders.futbolin.webapp.messaging;

import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;

/**
 * Combination of a RabbitMQ {@link Channel} and logical queue name.
 */
@AllArgsConstructor
public class Queue {
    final Channel channel;
    final String name;
}
