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
    }

    public boolean query(String word) {
        return cache.contains(word);
    }

    public void add(String word) {
        if (cache.size() >= maxSize) {
            String evictWord = replacementPolicy.remove();
            cache.remove(evictWord);
        }
        cache.add(word);
        replacementPolicy.add(word);
    }

    public void remove(String word) {
        if (cache.contains(word)) {
            cache.remove(word);
        }
    }
}
