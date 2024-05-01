package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.BitSet;

public class BloomFilter {
    BitSet filterBits;
    MessageDigest[] hashEngines;

    public BloomFilter(int capacity, String... hashAlgs) {
        filterBits = new BitSet(capacity);
        hashEngines = new MessageDigest[hashAlgs.length];

        for (int index = 0; index < hashEngines.length; index++) {
            try {
                hashEngines[index] = MessageDigest.getInstance(hashAlgs[index]); // Initialize each hash function by name
            } catch (Exception e) {
                System.out.println("Error initializing hash function: No such algorithm");
            }
        }
    }

    public void add(String element) {
        for (MessageDigest hashEngine : hashEngines) {
            filterBits.set(computeIndex(element, hashEngine));
        }
    }

    public boolean contains(String element) {
        for (MessageDigest hashEngine : hashEngines) {
            if (!filterBits.get(computeIndex(element, hashEngine))) {
                return false; // Element is definitely not in the set
            }
        }
        return true; // Element might be in the set
    }

    private int computeIndex(String element, MessageDigest hashEngine) {
        hashEngine.reset(); // Ensure clean state for hash calculation
        byte[] bytes = hashEngine.digest(element.getBytes());
        BigInteger hashValue = new BigInteger(1, bytes); // Use positive signum to avoid negative values
        return Math.abs(hashValue.intValue()) % filterBits.size(); // Compute index within BitSet bounds
    }

    @Override
    public String toString() {
        StringBuilder bitRepresentation = new StringBuilder();
        for (int i = 0; i < filterBits.length(); i++) {
            bitRepresentation.append(filterBits.get(i) ? '1' : '0');
        }
        return bitRepresentation.toString();
    }
}
