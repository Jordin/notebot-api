package in.jord.notebot.api.action;

import in.jord.notebot.api.NoteBot;
import in.jord.notebot.api.exception.InvalidNoteException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The {@link Note} class represents the note {@link NoteBotAction}. The note action is a {@code 16-bit} action
 * given an {@code action-id} of {@code 0} and is encoded in the following format:
 *
 * <blockquote><pre>
 *     0--- IIII ---N NNNN
 * </pre></blockquote><p>
 *
 * where {@code I} denotes the {@code instrument-id} (the instrument {@link Enum#ordinal()})<p>
 * and {@code N} denotes the {@code note-id}<p>
 * and {@literal -} denotes an unused bit ({@code 0})<p>
 *
 * @param <I> instrument enumeration
 */
public final class Note<I extends Enum<I>> implements NoteBotAction<I> {
    public static final int NOTE_ACTION_ID = 0;

    public static final int NOTE_MIN = 0;
    public static final int NOTE_MAX = 24;
    public static final int NOTE_COUNT = (NOTE_MAX - NOTE_MIN) + 1;

    public final I instrument;
    public final byte note;

    public Note(final I instrument, final byte note) {
        if (note < NOTE_MIN || note > NOTE_MAX) {
            throw new InvalidNoteException(note);
        }

        this.instrument = instrument;
        this.note = note;
    }

    public Note(final I instrument, final int note) {
        if (note < NOTE_MIN || note > NOTE_MAX) {
            throw new InvalidNoteException(note);
        }
        this.instrument = instrument;
        this.note = (byte) note;
    }

    public static <I extends Enum<I>> Note<I> of(final I instrument, final byte note) {
        return new Note<>(instrument, note);
    }

    public static <I extends Enum<I>> Note<I> of(final I instrument, final int note) {
        return new Note<>(instrument, note);
    }

    @Override
    public final void write(final OutputStream stream) throws IOException {
        stream.write((NOTE_ACTION_ID << NoteBot.ACTION_SHIFT) | this.instrument.ordinal());
        stream.write(this.note);
    }

    public static <I extends Enum<I>> Note<I> read(final NoteBot<I> noteBot, final byte opcode, final InputStream stream) throws IOException {
        assert stream.available() > 0;
        return new Note<>(noteBot.instruments[opcode], stream.read());
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note<?> note1 = (Note<?>) o;
        return this.note == note1.note &&
                this.instrument.equals(note1.instrument);
    }

    @Override
    public final int hashCode() {
        return 31 * this.instrument.hashCode() + Byte.hashCode(this.note);
    }

    @Override
    public final String toString() {
        return "Note{" +
                "instrument=" + this.instrument +
                ", note=" + this.note +
                '}';
    }
}
