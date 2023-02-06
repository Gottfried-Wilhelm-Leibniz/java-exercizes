package filesystem.server.actions;
import filesystem.FileSystem;
import filesystem.server.actions.Action;
import java.io.IOException;

public class CreateFileAction implements Action {
    @Override
    public Record doAction(FileSystem fs, String[] data) throws IOException {
        var name = data[0];
        // todo complete
        return null;
    }
}
