package me.itache.filter;

/**
 * Interface for filtering values by specified condition
 *
 * @param <T> type of filtered column
 */
public interface ColumnFilter<T> {
    /**
     * @param value to filter
     * @return true if given value satisfy specified condition, false - otherwise.
     */
    boolean isPassed(T value);

    String getColumn();
}
