package engine.exceptions;

public class OccupiedLocationException extends RuntimeException {

    public OccupiedLocationException(String message) {
        super(message);
    }
}
