package in.jord.notebot.api;

import in.jord.notebot.api.action.Note;
import in.jord.notebot.api.action.NoteBotAction;
import in.jord.notebot.api.action.Rest;
import in.jord.notebot.api.data.NoteBotSong;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NoteBot<I extends Enum<I>> {
    public static final int ACTION_SHIFT = Byte.SIZE - 1;
    public static final int ACTION_IDENTIFIER = 1 << ACTION_SHIFT;

    public final I[] instruments;

    public NoteBot(final Class<? extends I> instrumentClass) {
        this(instrumentClass.getEnumConstants());
    }

    public NoteBot(final I[] instruments) {
        this.instruments = instruments;
    }

    public NoteBotSong<I> read(final File file) throws IOException {
        final String name = FilenameUtils.getBaseName(file.getAbsolutePath());
        try (InputStream stream = new FileInputStream(file)) {
            return this.read(name, stream);
        }
    }

    public NoteBotSong<I> read(final String name, final InputStream stream) throws IOException {
        int totalDuration = 0;
        int trimmedDuration = 0;
        final ArrayList<NoteBotAction<I>> actions = new ArrayList<>();

        while (stream.available() > 0) {
            int opcode = stream.read();

            // extract action-id from opcode
            int action = opcode >> ACTION_SHIFT;

            // remove action-id from optcode
            opcode &= ~ACTION_IDENTIFIER;

            switch (action) {
                case Note.NOTE_ACTION_ID:
                    trimmedDuration = totalDuration;
                    actions.add(Note.read(this, (byte) (opcode), stream));
                    break;
                case Rest.REST_ACTION_ID:
                    totalDuration += opcode;
                    actions.add(Rest.read(this, (byte) (opcode), stream));
                    break;
                default:
                    throw new RuntimeException("Unknown action with id = " + action); // should be impossible
            }
        }

        // Remove trailing rests
        this.removeExtraneousRests(actions);

        actions.trimToSize();

        return new NoteBotSong<>(name, trimmedDuration, actions);
    }

    public final void removeExtraneousRests(final List<NoteBotAction<I>> actions) {
        // remove leading
        while (!actions.isEmpty() && actions.get(0) instanceof Rest) {
            actions.remove(0);
        }

        // remove trailing
        while (!actions.isEmpty() && actions.get(actions.size() - 1) instanceof Rest) {
            actions.remove(actions.size() - 1);
        }
    }

    public void write(final File file, final NoteBotSong<I> song) throws IOException {
        this.write(file, song.actions);
    }

    public void write(final File file, final List<NoteBotAction<I>> actions) throws IOException {
        this.removeExtraneousRests(actions);

        try (OutputStream stream = new FileOutputStream(file)) {
            for (NoteBotAction<I> action : actions) {
                action.write(stream);
            }
        }
    }
}
