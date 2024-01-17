package ca.jrvs.apps.practice;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class RegexExcImpTest {

    @Test
    public void testMatchJpeg() {
        RegexExc regex = new RegexExcImp();
        assertTrue(regex.matchJpeg("example.jpg"));
        assertTrue(regex.matchJpeg("photo.jpeg"));
        assertFalse(regex.matchJpeg("document.txt"));
    }

    @Test
    public void testMatchIp() {
        RegexExc regex = new RegexExcImp();
        assertTrue(regex.matchIp("192.168.0.1"));
        assertFalse(regex.matchIp("256.256"));
    }

    @Test
    public void testIsEmptyLine() {
        RegexExc regex = new RegexExcImp();
        assertTrue(regex.isEmptyLine(""));
        assertTrue(regex.isEmptyLine("  "));
        assertFalse(regex.isEmptyLine("Some non-empty text"));
    }
}
