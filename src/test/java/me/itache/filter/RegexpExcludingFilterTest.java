package me.itache.filter;

import org.junit.Assert;
import org.junit.Test;

public class RegexpExcludingFilterTest {
    @Test
    public void filteringShouldPass() {
        RegexpExcludingFilter filter = new RegexpExcludingFilter("name", "^[A-X][a-y].*$");
        Assert.assertTrue(filter.isPassed("YaStartsContact"));
        Assert.assertTrue(filter.isPassed("SzStartsContact"));
    }

    @Test
    public void filteringShouldNotPass() {
        RegexpExcludingFilter filter = new RegexpExcludingFilter("name", "^[A-X][a-y].*$");
        Assert.assertFalse(filter.isPassed("AaStartsContact"));
    }
}
