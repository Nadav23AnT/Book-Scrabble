package archive2.test;

import java.util.HashSet;
import java.util.Set;

public class Dict_Board {
    private Set<String> words;

    public Dict_Board() {
        this.words = new HashSet<>();
        // Optionally, initialize the dictionary with some words for testing
        initialize();
    }

    private void initialize() {
        // This is just an example. You should populate it with actual words.
        addWord("TEST");
        addWord("WORD");
        // Add more words as needed
    }

    public void addWord(String word) {
        words.add(word.toUpperCase());
    }

    public boolean contains(String word) {
        return words.contains(word.toUpperCase());
    }
}
