package in.jord.notebot.api.action;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Represents an abstract NoteBot action.
 *
 * @param <I> instrument enumeration
 */
public interface NoteBotAction<I extends Enum<I>> {
    void write(final OutputStream stream) throws IOException;
}
