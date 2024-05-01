package test;

import java.util.LinkedHashMap;
import java.util.Map;

public class LFU implements CacheReplacementPolicy{
    LinkedHashMap<String, Integer> frequentlyWords; //Keys: Words, Values: Number of times the word has been asked
    
    public LFU()
    {
        frequentlyWords = new LinkedHashMap<>();
    }

    public void add(String word)
    {//Incrementing the value of the word by 1 or adding the word to frequentlyWords if it does not exists
        frequentlyWords.put(word, frequentlyWords.getOrDefault(word, 0) + 1);
    }

    public String remove()
    {//Removing the word with the minimum number times asked
        Map.Entry<String, Integer> min = null;

        for (Map.Entry<String, Integer> entry : frequentlyWords.entrySet())
        {
            if (min == null || entry.getValue() < min.getValue())
            {
                min = entry;
            }
        }

        return min.getKey();
    }
}