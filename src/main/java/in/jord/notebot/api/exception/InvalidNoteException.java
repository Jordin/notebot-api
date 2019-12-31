package in.jord.notebot.api.exception;

import in.jord.notebot.api.action.Note;

public final class InvalidNoteException extends RuntimeException {
    public final int note;

    public InvalidNoteException(final int note) {
        super("Invalid note = " + note + ", note must fall between " + Note.NOTE_MIN + " and " + Note.NOTE_MAX + " (inclusive).");
        this.note = note;
    }
}
