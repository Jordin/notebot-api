package in.jord.notebot.api.data;

import in.jord.notebot.api.action.NoteBotAction;

import java.util.List;

public final class NoteBotSong<I extends Enum<I>> {
    public final String name;
    public final int duration;
    public final List<NoteBotAction<I>> actions;

    public NoteBotSong(String name, int duration, List<NoteBotAction<I>> actions) {
        this.name = name;
        this.duration = duration;
        this.actions = actions;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteBotSong<?> that = (NoteBotSong<?>) o;
        return this.duration == that.duration &&
                this.name.equals(that.name) &&
                this.actions.equals(that.actions);
    }

    @Override
    public final int hashCode() {
        return 31 * 31 * this.name.hashCode() +
                31 * Integer.hashCode(this.duration) +
                this.actions.hashCode();
    }

    @Override
    public final String toString() {
        return "NoteBotSong{" +
                "name='" + this.name + '\'' +
                ", duration=" + this.duration +
                ", actions=" + this.actions +
                '}';
    }
}
