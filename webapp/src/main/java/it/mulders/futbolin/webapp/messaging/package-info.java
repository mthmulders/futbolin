/**
 * This package contains some low-level messaging code based on RabbitMQ. It is there because it's not possible to use
 * JMS for this.
 * <ul>
 *     <li>RabbitMQ bundles with AMQP 0-9-1 enabled - but the Apache Qpid JMS client matching that version is only compatible
 *         with JMS 1.1.</li>
 *     <li>Alternatively, there is a AMQP 1.0 plugin for RabbitMQ. Using the Generic JMS Resource Adapter and Apache Qpid, it
 *         is possible to connect to RabbitMQ using JMS 2.0. But AMQP 1.0 support in RabbitMQ is not complete - for instance,
 *         it lacks dynamic target support, which is needed for dynamically creating (temporary) response queues.</li>
 * </ul>
 *
 * {@link it.mulders.futbolin.webapp.messaging.MessageSender} -> knows how to send a message.
 * {@link it.mulders.futbolin.webapp.messaging.MessageReceiver} -> waits for the response to come in.
 * {@link it.mulders.futbolin.webapp.messaging.MessagingFactory} -> CDI factory methods to inject some stuff.
 *
 * Since a temporary queue (declared without a name) is bound to the channel that created it, the
 * {@link it.mulders.futbolin.webapp.messaging.Queue} class combines the RabbitMQ channel with the generated or
 * specified queue names.
 */
package it.mulders.futbolin.webapp.messaging;