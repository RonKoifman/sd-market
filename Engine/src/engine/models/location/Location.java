package engine.models.location;

import engine.exceptions.LocationOutOfRangeException;

import java.awt.*;

public class Location extends Point {

    private static final int MIN_COORDINATE = 1;
    private static final int MAX_COORDINATE = 50;

    public Location(int x, int y) {
        checkForLegalCoordinate(x);
        checkForLegalCoordinate(y);
        this.x = x;
        this.y = y;
    }

    public static int getMinCoordinate() {
        return MIN_COORDINATE;
    }

    public static int getMaxCoordinate() {
        return MAX_COORDINATE;
    }

    private void checkForLegalCoordinate(int coordinate) {
        if (!(MIN_COORDINATE <= coordinate && coordinate <= MAX_COORDINATE)) {
            throw new LocationOutOfRangeException("Location coordinates must be between " + MIN_COORDINATE +
                    "-" + MAX_COORDINATE + ".");
        }
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
