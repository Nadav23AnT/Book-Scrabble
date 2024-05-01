package test;

import java.util.LinkedHashSet;

public class LRU implements CacheReplacementPolicy{
    LinkedHashSet<String> recentlyWords; //First word: least recently asked word, Last word: most recently asked word

    public LRU()
    {
        recentlyWords = new LinkedHashSet<>();
    }

    public void add(String word)
    {//Replacing the word to the end of the list or adding the word to the end if the word does not exists
        recentlyWords.remove(word);
        recentlyWords.add(word);
    }

    public String remove()
    {//Removing the first word
        String firstWord = recentlyWords.iterator().next();
        recentlyWords.remove(firstWord);
        return firstWord;
    }
}