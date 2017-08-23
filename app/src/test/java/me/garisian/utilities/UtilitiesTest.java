package me.garisian.utilities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Tests for java/me.garisian.utilities/Utilities.java
 */

public class UtilitiesTest {
    @Test
    public void testShouldWork()
    {
        Utilities test = new Utilities();
        assertEquals(0,  test.returnZero());
    }


}
