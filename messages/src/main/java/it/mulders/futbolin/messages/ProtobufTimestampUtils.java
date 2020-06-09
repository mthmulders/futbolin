package it.mulders.futbolin.messages;

import com.google.protobuf.Timestamp;

/**
 * Utility methods for working with Protocol Buffer {@link Timestamp} values.
 */
public class ProtobufTimestampUtils {
    /**
     * Creates a {@link Timestamp} instance for the current timestamp
     * {@see System#currentTimeMillis}
     * @return a {@link Timestamp} instance
     */
    public static Timestamp now() {
        var millis = System.currentTimeMillis();
        return Timestamp.newBuilder()
                .setSeconds(millis / 1_000)
                .setNanos((int) ((millis % 1000) * 1000000))
                .build();
    }
}
