package me.itache.filter;

import java.util.regex.Pattern;

/**
 * Filtering strings by specified pattern.
 * String does not pass if it matches pattern.
 */
public class RegexpExcludingFilter implements ColumnFilter<String> {
    private final Pattern pattern;
    private final String column;

    /**
     *
     * @param column to apply filter
     * @param patternStr pattern to check
     */
    public RegexpExcludingFilter(String column, String patternStr) {
        pattern = Pattern.compile(patternStr);
        this.column = column;
    }

    @Override
    public boolean isPassed(String value) {
        return !pattern.matcher(value).matches();
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return column + "=" + pattern;
    }
}
