package model.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import model.Deck;
import model.DeckFormatter;

/**
 * Saves a Deck's current top-to-bottom order to a text file, in the same
 * "D-A,D-K,..." format used for loading a deck. Used at game end to record
 * the winner's final card arrangement.
 *
 * Kept separate from Deck itself so file I/O doesn't have to live inside
 * the class that just manages a pile of cards.
 */
public class DeckFileWriter {

    //private DeckFileWriter() {
        // utility class — every method is static, so this is never instantiated
    //}

    public static void write(Deck deck, String fileName) throws IOException {
        String content = DeckFormatter.toFileFormat(deck);
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(fileName), StandardCharsets.UTF_8)) {
            writer.write(content);
        }
    }
}
