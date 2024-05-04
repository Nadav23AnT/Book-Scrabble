package test;

import java.util.HashMap;

public class DictionaryManager {
    private static DictionaryManager instance; // Singleton instance
    // Map of dictionary instances indexed by file names
    HashMap<String, Dictionary> dictionaries;

    private DictionaryManager() {
        dictionaries = new HashMap<>();
    }

    public static DictionaryManager get() {
        // Return singleton instance, create if necessary
        if (instance == null) {
            instance = new DictionaryManager();
        }
        return instance;
    }

    public boolean query(String... files) {
        // Process a query to check existence of a word in multiple dictionaries
        String word = files[files.length - 1]; // The last argument is the word to be queried
        boolean existsWord = false;

        // Loop through all files except the last one (which is the word)
        for (int i = 0; i < files.length - 1; i++) {
            dictionaries.computeIfAbsent(files[i], k -> new Dictionary(k)); // Load dictionary if not already loaded

            // Check if word exists in dictionary
            if (dictionaries.get(files[i]).query(word)) {
                existsWord = true;
            }
        }

        return existsWord;
    }

    public boolean challenge(String... files) {
        // Similar to query, but for challenge purposes
        String word = files[files.length - 1];
        boolean existsWord = false;

        for (int i = 0; i < files.length - 1; i++) {
            dictionaries.computeIfAbsent(files[i], k -> new Dictionary(k));

            if (dictionaries.get(files[i]).challenge(word)) {
                existsWord = true;
            }
        }

        return existsWord;
    }

    public int getSize() {
        // Return the number of loaded dictionaries
        return dictionaries.size();
    }
}
