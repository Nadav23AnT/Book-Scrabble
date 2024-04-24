package test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter {
  private BitSet bitset;
  private int bitSetSize;
  private MessageDigest digest1;
  private MessageDigest digest2;

  public BloomFilter(int bitSetSize, String hashName1, String hashName2) {
    try {
      this.bitset = new BitSet(bitSetSize);
      this.bitSetSize = bitSetSize;
      this.digest1 = MessageDigest.getInstance(hashName1);
      this.digest2 = MessageDigest.getInstance(hashName2);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Failed to initialize hash functions", e);
    }
  }

  public void add(String word) {
    byte[] bytes = word.getBytes();
    bitset.set(hash(bytes, digest1) % bitSetSize);
    bitset.set(hash(bytes, digest2) % bitSetSize);
  }

  public boolean contains(String word) {
    byte[] bytes = word.getBytes();
    boolean result = true;
    for (int i = 0; i < 2; i++) { // Assuming two hash functions for simplicity
      int hash = hash(bytes, i == 0 ? digest1 : digest2);
      result = result && bitset.get(hash);
      System.out.println("Checking bit at index " + hash + ": " + result);
    }
    return result;
  }

  private int hash(byte[] bytes, MessageDigest digest) {
    digest.reset();
    byte[] hashBytes = digest.digest(bytes);
    int hash = 0;
    for (int i = 0; i < Math.min(4, hashBytes.length); i++) { // Use up to the first four bytes
      hash = (hash << 8) + (hashBytes[i] & 0xFF);
    }
    return Math.abs(hash % bitSetSize);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < bitSetSize; i++) {
      sb.append(bitset.get(i) ? '1' : '0');
    }
    return sb.toString();
  }

}
