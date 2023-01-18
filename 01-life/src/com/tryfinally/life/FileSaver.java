package com.tryfinally.life;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.function.Function;

public class FileSaver {
    private int iteration = 0;

    public <T> void save(Grid<T> grid, Function<T, Integer> transformer, String name) throws IOException {
        int width = grid.getWidth();
        int height = grid.getHeight();
        var path = Path.of(name, name + "." + String.format("%03d", iteration++) + ".pbm");
        Files.createFile(path);
        var stringBuilder = new StringBuilder(width*height + (width - 1 * height) + height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                var v = grid.get(i, j);
                var state = transformer.apply(v);
                stringBuilder.append(state);
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }

        Files.write(path, stringBuilder.toString().getBytes());
    }
    public void initilize(String name) throws IOException {
        Path path = Path.of(name);
        if (Files.exists(path)) {
            try (Stream<Path> walk = Files.walk(path)) {
                walk.forEach(FileSaver::deletefile);
            }
        }
        Files.deleteIfExists(path);
        Files.createDirectory(path);
    }
    public static void deletefile(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
        }
    }

}
