package model.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import model.Deck;
import model.DeckFormatter;

public class DeckFileWriter {


    public static void write(Deck deck, String fileName) throws IOException {
        String content = DeckFormatter.toFileFormat(deck);
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(fileName), StandardCharsets.UTF_8)) {
            writer.write(content);
        }
    }
}
