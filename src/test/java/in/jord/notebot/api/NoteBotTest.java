package in.jord.notebot.api;

import in.jord.notebot.api.action.Note;
import in.jord.notebot.api.action.NoteBotAction;
import in.jord.notebot.api.action.Rest;
import in.jord.notebot.api.data.MinecraftInstrument;
import in.jord.notebot.api.data.NoteData;
import in.jord.notebot.api.data.NoteBotSong;
import in.jord.notebot.api.exception.InvalidNoteException;
import in.jord.notebot.api.exception.InvalidRestDurationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NoteBotTest {
    private static final File TEST_DIRECTORY = new File("tests/");
    static {
        TEST_DIRECTORY.mkdirs();
    }

    @Test
    public void testPitchMap() {
        for (int note = Note.NOTE_MIN; note <= Note.NOTE_MAX; note++) {
            Assertions.assertEquals((float) Math.pow(2, (note - 12) / 12.0), NoteData.NOTE_TO_PITCH[note]);
        }
    }

    @Test
    public void testInvalidRest() {
        Assertions.assertThrows(InvalidRestDurationException.class, () -> Rest.of(Rest.REST_DURATION_MIN - 1));
        Assertions.assertThrows(InvalidRestDurationException.class, () -> Rest.of(Rest.REST_DURATION_MAX + 1));
    }

    @Test
    public void testValidRest() {
        for (int duration = Rest.REST_DURATION_MIN; duration <= Rest.REST_DURATION_MAX; duration++) {
            final int finalDuration = duration;
            Assertions.assertDoesNotThrow(() -> Rest.of(finalDuration));
        }
    }

    @Test
    public void testInvalidNote() {
        Assertions.assertThrows(InvalidNoteException.class, () -> Note.of(MinecraftInstrument.HARP, Note.NOTE_MIN - 1));
        Assertions.assertThrows(InvalidNoteException.class, () -> Note.of(MinecraftInstrument.HARP, Note.NOTE_MAX + 1));
    }

    @Test
    public void testValidNote() {
        for (int note = Note.NOTE_MIN; note <= Note.NOTE_MAX; note++) {
            final int finalNote = note;
            Assertions.assertDoesNotThrow(() -> Note.of(MinecraftInstrument.HARP, finalNote));
        }
    }

    @Test
    public void testSongName() throws IOException {
        final NoteBot<MinecraftInstrument> noteBot = new MinecraftNoteBot();
        final File file = new File(TEST_DIRECTORY, "Song A.notebot");

        //noinspection ResultOfMethodCallIgnored
        file.createNewFile();

        Assertions.assertEquals("Song A", noteBot.read(file).name);

        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }

    @Test
    public void testTrimmedDuration() throws IOException {
        final NoteBot<MinecraftInstrument> noteBot = new MinecraftNoteBot();
        final File file = new File(TEST_DIRECTORY, "Song B.notebot");

        final List<NoteBotAction<MinecraftInstrument>> actions = new ArrayList<>();

        for (int i = 0; i <= Rest.REST_DURATION_MAX; i++) {
            actions.add(Rest.of(i));
        }

        actions.add(Note.of(MinecraftInstrument.HARP, 0));

        for (int i = 0; i <= Rest.REST_DURATION_MAX; i++) {
            actions.add(Rest.of(i));
        }

        noteBot.write(file, actions);

        NoteBotSong<MinecraftInstrument> song = noteBot.read(file);

        Assertions.assertEquals(1, song.actions.size());
        Assertions.assertEquals(0, song.duration);

        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }

    @Test
    public void testDuration() throws IOException {
        final NoteBot<MinecraftInstrument> noteBot = new MinecraftNoteBot();
        final File file = new File(TEST_DIRECTORY, "Song C.notebot");

        final List<NoteBotAction<MinecraftInstrument>> actions = new ArrayList<>();

        int expectedDuration = 0;

        for (int duration = Rest.REST_DURATION_MIN; duration <= Rest.REST_DURATION_MAX; duration++) {
            expectedDuration += duration;
            actions.add(Rest.of(duration));
        }

        actions.add(Note.of(MinecraftInstrument.HARP, 0));

        noteBot.write(file, actions);

        Assertions.assertEquals(expectedDuration, noteBot.read(file).duration);

        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }

    @Test
    public void testFullSong() throws IOException {
        final NoteBot<MinecraftInstrument> noteBot = new MinecraftNoteBot();

        final File file = new File(TEST_DIRECTORY, "Song D.notebot");

        final List<NoteBotAction<MinecraftInstrument>> songActions = new ArrayList<>();

        songActions.add(Note.of(MinecraftInstrument.BIT, 5));

        songActions.add(Note.of(MinecraftInstrument.BIT, 2));
        songActions.add(Note.of(MinecraftInstrument.GUITAR, 15));

        songActions.add(Rest.of(100));
        songActions.add(Note.of(MinecraftInstrument.FLUTE, 22));

        noteBot.write(file, songActions);

        NoteBotSong<MinecraftInstrument> song = noteBot.read(file);
        Assertions.assertEquals("Song D", song.name);
        Assertions.assertEquals(100, song.duration);

        Assertions.assertEquals(songActions.size(), song.actions.size());

        for (int i = 0; i < songActions.size(); i++) {
            Assertions.assertEquals(songActions.get(i), song.actions.get(i));
        }

        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }
}
