package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ExtractTest
{
    /*
     * Testing strategy for getTimespan:
     * - one tweet
     * - multiple tweets in chronological order
     * - multiple tweets out of order
     *
     * Testing strategy for getMentionedUsers:
     * - no mentions
     * - multiple mentions
     * - repeated username with different case
     * - invalid mention inside email address
     */

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "hello world", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "hello @alyssa", d2);
    private static final Tweet tweet3 = new Tweet(3, "charlie", "@Alyssa please ask @bbitdiddle.", d3);
    private static final Tweet tweet4 = new Tweet(4, "dana", "email bitdiddle@mit.edu and ask @MIT-team", d1);

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled()
    {
        assert false;
    }

    @Test
    public void testGetTimespanOneTweet()
    {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));

        assertEquals(d1, timespan.getStart());
        assertEquals(d1, timespan.getEnd());
    }

    @Test
    public void testGetTimespanTwoTweets()
    {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));

        assertEquals(d1, timespan.getStart());
        assertEquals(d2, timespan.getEnd());
    }

    @Test
    public void testGetTimespanTweetsOutOfOrder()
    {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet3, tweet1));

        assertEquals(d1, timespan.getStart());
        assertEquals(d3, timespan.getEnd());
    }

    @Test
    public void testGetMentionedUsersNoMention()
    {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));

        assertTrue(mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersMultipleMentions()
    {
        Set<String> mentionedUsers = lowercaseSet(Extract.getMentionedUsers(Arrays.asList(tweet3)));

        assertTrue(mentionedUsers.contains("alyssa"));
        assertTrue(mentionedUsers.contains("bbitdiddle"));
        assertEquals(2, mentionedUsers.size());
    }

    @Test
    public void testGetMentionedUsersCaseInsensitiveNoDuplicate()
    {
        Tweet tweet = new Tweet(5, "eve", "@Alyssa thanks @alyssa", d1);
        Set<String> mentionedUsers = lowercaseSet(Extract.getMentionedUsers(Arrays.asList(tweet)));

        assertTrue(mentionedUsers.contains("alyssa"));
        assertEquals(1, mentionedUsers.size());
    }

    @Test
    public void testGetMentionedUsersEmailAddressIsNotMention()
    {
        Set<String> mentionedUsers = lowercaseSet(Extract.getMentionedUsers(Arrays.asList(tweet4)));

        assertFalse(mentionedUsers.contains("mit"));
        assertTrue(mentionedUsers.contains("mit-team"));
    }

    private static Set<String> lowercaseSet(Set<String> strings)
    {
        Set<String> result = new HashSet<String>();

        for (String string : strings)
        {
            result.add(string.toLowerCase());
        }

        return result;
    }
}