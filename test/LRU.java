package test;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRU implements CacheReplacementPolicy {
    private LinkedHashMap<String, Void> cache;
    private int capacity;
    private static final int DEFAULT_CAPACITY = 100; // Default capacity

    public LRU() {
        this(DEFAULT_CAPACITY);
    }

    public LRU(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(this.capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Void> eldest) {
                return size() > LRU.this.capacity;
            }
        };
    }

    @Override
    public void add(String word) {
        cache.put(word, null);
    }

    @Override
    public String remove() {
        if (!cache.isEmpty()) {
            String firstKey = cache.keySet().iterator().next();
            cache.remove(firstKey);
            return firstKey;
        }
        return null; // Or throw exception if preferred
    }
}
