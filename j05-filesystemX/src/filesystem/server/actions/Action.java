package filesystem.server.actions;

import filesystem.FileSystem;

import java.io.IOException;

@FunctionalInterface
public interface Action {
    String doAction(FileSystem fs, String str) throws IOException;
}
