package in.jord.notebot.api;

import in.jord.notebot.api.data.MinecraftInstrument;

public final class MinecraftNoteBot extends NoteBot<MinecraftInstrument> {
    public MinecraftNoteBot() {
        super(MinecraftInstrument.class);
    }
}
