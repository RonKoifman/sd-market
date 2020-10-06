package engine.models.location;

import engine.exceptions.LocationOutOfRangeException;

import java.awt.*;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return x == location.x && y == location.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
