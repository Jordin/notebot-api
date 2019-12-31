package in.jord.notebot.api.action;

import in.jord.notebot.api.NoteBot;
import in.jord.notebot.api.exception.InvalidRestDurationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The {@link Rest} class represents the rest {@link NoteBotAction}. The rest action is an {@code 8-bit} action
 * given an {@code action-id} of {@code 1} and is encoded in the following format:
 *
 * <blockquote><pre>
 *     1DDD DDDD
 * </pre></blockquote><p>
 *
 * where {@code D} denotes the {@code duration} of the rest<p>
 *
 * @param <I> instrument enumeration
 */
public final class Rest<I extends Enum<I>> implements NoteBotAction<I> {
    public static final int REST_ACTION_ID = 1;

    public static final int REST_DURATION_MIN = 0;
    public static final int REST_DURATION_MAX = 0b01111111;

    public final byte duration;

    public Rest(final byte duration) {
        //noinspection ConstantConditions
        if (duration < REST_DURATION_MIN || duration > REST_DURATION_MAX) {
            throw new InvalidRestDurationException(duration);
        }
        this.duration = duration;
    }

    public Rest(final int duration) {
        if (duration < REST_DURATION_MIN || duration > REST_DURATION_MAX) {
            throw new InvalidRestDurationException(duration);
        }
        this.duration = (byte) duration;
    }

    public static <I extends Enum<I>> Rest<I> of(final byte duration) {
        return new Rest<>(duration);
    }

    public static <I extends Enum<I>> Rest<I> of(final int duration) {
        return new Rest<>(duration);
    }

    @Override
    public final void write(final OutputStream stream) throws IOException {
        stream.write((REST_ACTION_ID << NoteBot.ACTION_SHIFT) | this.duration);
    }

    public static <I extends Enum<I>> Rest<I> read(final NoteBot<I> noteBot, final byte opcode, final InputStream stream) {
        return new Rest<>(opcode);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rest<?> rest = (Rest<?>) o;
        return this.duration == rest.duration;
    }

    @Override
    public final int hashCode() {
        return Byte.hashCode(this.duration);
    }

    @Override
    public final String toString() {
        return "Rest{" +
                "duration=" + this.duration +
                '}';
    }
}
