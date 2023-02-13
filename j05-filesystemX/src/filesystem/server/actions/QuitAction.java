package filesystem.server.actions;

import filesystem.FileSystem;
import filesystem.server.Ansewrs.Result;
import filesystem.server.Ansewrs.ResultAndFiles;

import java.io.IOException;

public class QuitAction implements filesystem.server.actions.Action {
    @Override
    public Record doAction(FileSystem fs, String[] data) throws IOException {
        return new Result("OK ByeBye");
    }
}
