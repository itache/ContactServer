package me.itache.filter;

import java.util.Optional;

/**
 * Represents class for chunking information by primary keys.
 */
public class Cursor {
    private static final int DEFAULT_SIZE = 100;
    private int size;
    private long lowerBound;
    private long upperBound;
    private long lastViewedId;
    private Direction direction;
    private long maxId;

    /**
     *
     * @param lowerBound primary key from which data viewing starts
     * @param direction of data viewing
     * @param size items in chunk
     */
    public Cursor(long lowerBound, Direction direction, Optional<Integer> size) {
        this.size = size.orElse(DEFAULT_SIZE);
        this.direction = direction;
        this.lowerBound = lowerBound;
        this.lastViewedId = lowerBound;
    }

    public static Cursor createForward(long lowerBound, Optional<Integer> size) {
        return new Cursor(lowerBound, Direction.FORWARD, size);
    }

    public static Cursor createBackward(long lowerBound, Optional<Integer> size) {
        return new Cursor(lowerBound, Direction.BACKWARD, size);
    }

    public int getSize() {
        return size;
    }

    public long getLowerBound() {
        return lowerBound;
    }

    public long getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(long upperBound) {
        this.upperBound = upperBound;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }

    public long getMaxId() {
        return maxId;
    }

    public long getLastViewedId() {
        return lastViewedId;
    }

    public void setLastViewedId(long lastViewedId) {
        this.lastViewedId = lastViewedId;
    }

    /**
     * Specify data view direction: from lower bound to upper or vise versa
     */
    public enum Direction {
        FORWARD, BACKWARD;
    }
}
