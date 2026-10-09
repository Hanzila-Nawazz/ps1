package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest
{
    /*
     * Testing strategy for writtenBy:
     * - no matching author
     * - one matching author
     * - multiple matching authors
     * - case-insensitive author matching
     *
     * Testing strategy for inTimespan:
     * - tweet before timespan
     * - tweet inside timespan
     * - tweet exactly at start
     * - tweet exactly at end
     * - tweet after timespan
     *
     * Testing strategy for containing:
     * - one matching word
     * - multiple search words
     * - case-insensitive matching
     * - no match when word is only part of another word
     */

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes", d2);
    private static final Tweet tweet3 = new Tweet(3, "ALYSSA", "MIT class starts now", d3);

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled()
    {
        assert false;
    }

    @Test
    public void testWrittenBySingleResult()
    {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");

        assertEquals(Arrays.asList(tweet1), writtenBy);
    }

    @Test
    public void testWrittenByCaseInsensitiveMultipleResults()
    {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet2, tweet1, tweet3), "Alyssa");

        assertEquals(Arrays.asList(tweet1, tweet3), writtenBy);
    }

    @Test
    public void testWrittenByNoResult()
    {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "ben");

        assertTrue(writtenBy.isEmpty());
    }

    @Test
    public void testInTimespanIncludesEndpoints()
    {
        Timespan timespan = new Timespan(d1, d2);
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet3, tweet2, tweet1), timespan);

        assertEquals(Arrays.asList(tweet2, tweet1), inTimespan);
    }

    @Test
    public void testInTimespanNoResult()
    {
        Instant start = Instant.parse("2016-02-18T10:00:00Z");
        Instant end = Instant.parse("2016-02-18T11:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(start, end));

        assertTrue(inTimespan.isEmpty());
    }

    @Test
    public void testContainingOneWord()
    {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));

        assertEquals(Arrays.asList(tweet1, tweet2), containing);
    }

    @Test
    public void testContainingCaseInsensitive()
    {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet3), Arrays.asList("mit"));

        assertEquals(Arrays.asList(tweet3), containing);
    }

    @Test
    public void testContainingAnySearchWord()
    {
        List<Tweet> containing = Filter.containing(
                Arrays.asList(tweet1, tweet2, tweet3),
                Arrays.asList("class", "reasonable"));

        assertEquals(Arrays.asList(tweet1, tweet3), containing);
    }

    @Test
    public void testContainingNoPartialWordMatch()
    {
        Tweet tweet = new Tweet(4, "dana", "talking", d1);

        List<Tweet> containing = Filter.containing(Arrays.asList(tweet), Arrays.asList("talk"));

        assertTrue(containing.isEmpty());
    }
}