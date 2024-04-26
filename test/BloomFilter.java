package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.BitSet;

public class BloomFilter {
  private int numHashFunctions;
  private MessageDigest[] hashFunctions;
  private BitSet bitSet;
  private int numBits;

  public BloomFilter(int numBits, String... hashFuncNames) {
    this.numBits = numBits;
    this.numHashFunctions = hashFuncNames.length;
    this.hashFunctions = new MessageDigest[numHashFunctions];
    this.bitSet = new BitSet(this.numBits);

    for (int i = 0; i < numHashFunctions; i++) {
      try {
        hashFunctions[i] = MessageDigest.getInstance(hashFuncNames[i]);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void add(String word) {
    for (int i = 0; i < numHashFunctions; i++) {
      hashFunctions[i].update(word.getBytes());
      byte[] hash = hashFunctions[i].digest();
      BigInteger hashInt = new BigInteger(hash);
      int val = hashInt.intValue();
      if (val > 0)
        bitSet.set(val % this.numBits);
      else
        bitSet.set((-val) % this.numBits);
    }
  }

  public boolean contains(String word) {
    return isContains(word);
  }

  public boolean isContains(String word) {
    for (int i = 0; i < numHashFunctions; i++) {
      hashFunctions[i].update(word.getBytes());
      byte[] hash = hashFunctions[i].digest();
      BigInteger hashInt = new BigInteger(hash);
      int val = hashInt.intValue();
      if (val < 0)
        val *= -1;
      if (!bitSet.get(val % this.numBits))
        return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < bitSet.length(); i++) {
      sb.append(bitSet.get(i) ? "1" : "0");
    }
    return sb.toString();
  }

}
