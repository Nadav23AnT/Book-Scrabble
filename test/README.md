### Class: BloomFilter
#### Description:
The `BloomFilter` class manages a probabilistic data structure which is very space-efficient and is used for testing whether an element is a member of a set.

#### Attributes:
- **filterBits**: A `BitSet` representing the set of bits used in the bloom filter.
- **hashEngines**: An array of `MessageDigest` instances, each representing a hash function.

#### Methods:
- **Constructor**: Initializes the bloom filter with a specified capacity and hash algorithms.
- **add(String element)**: Adds an element to the bloom filter by computing hashes and setting bits in the bit array.
- **contains(String element)**: Checks if an element might be in the set by verifying if all the bits computed by the hash functions are set.
- **computeIndex(String element, MessageDigest hashEngine)**: Computes the index in the BitSet for a given element and hash function.
- **toString()**: Provides a string representation of the BitSet for debugging purposes, showing which bits are set.

### Class: CacheManager
#### Description:
Manages a cache to store frequently accessed data, reducing the need for repeated expensive operations.

#### Attributes:
- **cache**: A `Set` that stores cached data.
- **maxSize**: Maximum number of elements the cache can hold.
- **replacementPolicy**: Strategy for replacing elements when the cache is full.

#### Methods:
- **Constructor**: Sets the initial cache size and the replacement policy.
- **query(String word)**: Checks if a word is present in the cache.
- **add(String word)**: Adds a new word to the cache, possibly evicting an old word according to the replacement policy.
- **remove(String word)**: Removes a word from the cache.

### Class: Dictionary
#### Description:
Integrates all components, managing word existence queries across cached and persistent storage.

#### Attributes:
- **existingWordsCache**: A `CacheManager` for words that exist.
- **nonExistingWordsCache**: A `CacheManager` for words that do not exist.
- **bloomFilter**: A `BloomFilter` to quickly check the probable presence of a word.
- **fileNames**: An array of file names to load words from.

#### Methods:
- **Constructor**: Initializes caches and the bloom filter, and loads words.
- **loadWordsIntoBloomFilter()**: Loads words from files into the Bloom filter.
- **query(String word)**: Checks for the presence of a word using caches and the Bloom filter.
- **challenge(String word)**: Force a definitive search for a word across files, updating caches accordingly.

### Class: IOSearcher
#### Description:
Performs direct file I/O to find words in stored documents.

#### Methods:
- **search(String word, String... fileNames)**: Searches for a word across multiple files, returning `true` if found.

### Interface: CacheReplacementPolicy
#### Description:
Defines methods for cache replacement strategies.

#### Methods:
- **add(String word)**: Adds a word to the tracking structure.
- **remove()**: Selects and returns a word to be removed based on the policy.

### Implementations of CacheReplacementPolicy:
1. **LRU (Least Recently Used)**: Evicts the least recently used item.
2. **LFU (Least Frequently Used)**: Evicts the least frequently used item.

This project uses design patterns like Strategy for varying cache replacement policies and encapsulates file reading operations to ensure scalability and efficiency.