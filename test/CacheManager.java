package test;

import java.util.HashSet;
import java.util.Set;

public class CacheManager {
    private Set<String> cache;
    private int maxSize;
    private CacheReplacementPolicy replacementPolicy;

    public CacheManager(int maxSize, CacheReplacementPolicy replacementPolicy) {
        this.cache = new HashSet<>();
        this.maxSize = maxSize;
        this.replacementPolicy = replacementPolicy;
        // System.out.println("CacheManager initialized with maxSize: " + maxSize);
    }

    public boolean query(String word) {
        boolean found = cache.contains(word);
        // System.out.println("Query for word: " + word + " | Found: " + found);
        return found;
    }

    public void add(String word) {
        if (cache.size() >= maxSize) {
            String evictWord = replacementPolicy.remove();
            cache.remove(evictWord);
            // System.out.println("Evicted word: " + evictWord);
        }
        cache.add(word);
        replacementPolicy.add(word);
        // System.out.println("Added word to cache: " + word);
    }

    public void remove(String word) {
        if (cache.contains(word)) {
            cache.remove(word);
            // System.out.println("Removed word from cache: " + word);
        }
    }
}
