package in.jord.notebot.api.data;

public enum MinecraftInstrument {
    HARP("harp"),
    BASEDRUM("basedrum"),
    SNARE("snare"),
    HAT("hat"),
    BASS("bass"),
    FLUTE("flute"),
    BELL("bell"),
    GUITAR("guitar"),
    CHIME("chime"),
    XYLOPHONE("xylophone"),
    IRON_XYLOPHONE("iron_xylophone"),
    COW_BELL("cow_bell"),
    DIDGERIDOO("didgeridoo"),
    BIT("bit"),
    BANJO("banjo"),
    PLING("pling");

    public final String name;

    MinecraftInstrument(String name) {
        this.name = name;
    }

    @Override
    public final String toString() {
        return this.name;
    }
}
