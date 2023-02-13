package filesystem.server.actions;

import filesystem.FileSystem;

import java.io.IOException;

@FunctionalInterface
public interface Action {
    Record doAction(FileSystem fs, String[] data) throws IOException;
}
