package test;


import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;

public class LFU implements CacheReplacementPolicy {
    private Map<String, Integer> freqMap;
    private TreeMap<Integer, LinkedHashSet<String>> freqToList;
    private int capacity;
    private static final int DEFAULT_CAPACITY = 100; // Default capacity

    public LFU() {
        this(DEFAULT_CAPACITY);
    }

    public LFU(int capacity) {
        this.capacity = capacity;
        this.freqMap = new HashMap<>();
        this.freqToList = new TreeMap<>();
    }

    private void touch(String word) {
        int freq = freqMap.getOrDefault(word, 0);
        if (freq > 0) {
            freqToList.get(freq).remove(word);
            if (freqToList.get(freq).isEmpty()) {
                freqToList.remove(freq);
            }
        }
        freq += 1;
        freqMap.put(word, freq);
        freqToList.computeIfAbsent(freq, k -> new LinkedHashSet<>()).add(word);
    }

    @Override
    public void add(String word) {
        if (freqMap.containsKey(word)) {
            touch(word);
        } else {
            if (freqMap.size() >= capacity) {
                remove(); // Evict least frequently used item
            }
            freqMap.put(word, 1);
            freqToList.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(word);
        }
    }

    @Override
    public String remove() {
        if (!freqToList.isEmpty()) {
            Map.Entry<Integer, LinkedHashSet<String>> minFreq = freqToList.firstEntry();
            String word = minFreq.getValue().iterator().next();
            minFreq.getValue().remove(word);
            if (minFreq.getValue().isEmpty()) {
                freqToList.pollFirstEntry();
            }
            freqMap.remove(word);
            return word;
        }
        return null; // Or throw exception if preferred
    }
}
