package in.jord.notebot.api.exception;

import in.jord.notebot.api.action.Rest;

public final class InvalidRestDurationException extends RuntimeException {
    public final int duration;

    public InvalidRestDurationException(final int duration) {
        super("Invalid rest duration = " + duration + ", duration must fall between " + Rest.REST_DURATION_MIN + " and " + Rest.REST_DURATION_MAX + " (inclusive).");
        this.duration = duration;
    }
}
