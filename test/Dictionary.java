package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Dictionary {
  private CacheManager existingWordsCache;
  private CacheManager nonExistingWordsCache;
  private BloomFilter bloomFilter;
  private String[] fileNames; // Names of files containing words

  public Dictionary(String... fileNames) {
    // Initialize CacheManagers with pre-defined sizes
    this.existingWordsCache = new CacheManager(400, new LRU());
    this.nonExistingWordsCache = new CacheManager(100, new LFU());

    // Initialize the BloomFilter with 256 bits and the specific hash functions
    this.bloomFilter = new BloomFilter(256, "MD5", "SHA1");

    this.fileNames = fileNames;

    // Load words into the BloomFilter from the files
    loadWordsIntoBloomFilter();
  }

  private void loadWordsIntoBloomFilter() {
    for (String fileName : fileNames) {
      try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
        String line;
        while ((line = reader.readLine()) != null) {
          for (String word : line.split("\\W+")) { // Split by non-word characters
            if (!word.isEmpty()) {
              bloomFilter.add(word);
            }
          }
        }
      } catch (IOException e) {
        System.err.println("Failed to load words from file: " + fileName);
        e.printStackTrace();
      }
    }
  }

  public boolean query(String word) {
    if (existingWordsCache.query(word)) {
      return true;
    }
    if (nonExistingWordsCache.query(word)) {
      return false;
    }
    boolean existsInBloomFilter = bloomFilter.contains(word);
    if (existsInBloomFilter) {
      existingWordsCache.add(word); // Optionally update cache on positive Bloom filter
    } else {
      nonExistingWordsCache.add(word);
    }
    return existsInBloomFilter;
  }

  public boolean challenge(String word) {
    try {
      boolean exists = IOSearcher.search(word, fileNames);
      if (exists) {
        existingWordsCache.add(word);
      } else {
        nonExistingWordsCache.add(word);
      }
      return exists;
    } catch (Exception e) {
      System.err.println("Error during IO search: " + e.getMessage());
      e.printStackTrace();
      return false; // Return false on exception
    }
  }
}
