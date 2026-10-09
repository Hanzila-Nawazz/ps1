package twitter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extract
{
    private static final Pattern MENTION_PATTERN =
            Pattern.compile("(?<![A-Za-z0-9_-])@([A-Za-z0-9_-]+)(?![A-Za-z0-9_-])");

    public static Timespan getTimespan(List<Tweet> tweets)
    {
        if (tweets.isEmpty())
        {
            return new Timespan(Instant.EPOCH, Instant.EPOCH);
        }

        Instant start = tweets.get(0).getTimestamp();
        Instant end = tweets.get(0).getTimestamp();

        for (Tweet tweet : tweets)
        {
            Instant timestamp = tweet.getTimestamp();

            if (timestamp.isBefore(start))
            {
                start = timestamp;
            }

            if (timestamp.isAfter(end))
            {
                end = timestamp;
            }
        }

        return new Timespan(start, end);
    }

    public static Set<String> getMentionedUsers(List<Tweet> tweets)
    {
        Set<String> mentionedUsers = new HashSet<String>();

        for (Tweet tweet : tweets)
        {
            Matcher matcher = MENTION_PATTERN.matcher(tweet.getText());

            while (matcher.find())
            {
                mentionedUsers.add(matcher.group(1).toLowerCase());
            }
        }

        return mentionedUsers;
    }
}