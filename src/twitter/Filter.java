package twitter;

import java.util.ArrayList;
import java.util.List;

public class Filter
{
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username)
    {
        List<Tweet> result = new ArrayList<Tweet>();

        for (Tweet tweet : tweets)
        {
            if (tweet.getAuthor().equalsIgnoreCase(username))
            {
                result.add(tweet);
            }
        }

        return result;
    }

    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan)
    {
        List<Tweet> result = new ArrayList<Tweet>();

        for (Tweet tweet : tweets)
        {
            if (!tweet.getTimestamp().isBefore(timespan.getStart())
                    && !tweet.getTimestamp().isAfter(timespan.getEnd()))
            {
                result.add(tweet);
            }
        }

        return result;
    }

    public static List<Tweet> containing(List<Tweet> tweets, List<String> words)
    {
        List<Tweet> result = new ArrayList<Tweet>();

        for (Tweet tweet : tweets)
        {
            if (containsAnyWord(tweet.getText(), words))
            {
                result.add(tweet);
            }
        }

        return result;
    }

    private static boolean containsAnyWord(String text, List<String> words)
    {
        String[] tweetWords = text.split(" ");

        for (String tweetWord : tweetWords)
        {
            if (tweetWord.isEmpty())
            {
                continue;
            }

            for (String word : words)
            {
                if (tweetWord.equalsIgnoreCase(word))
                {
                    return true;
                }
            }
        }

        return false;
    }
}