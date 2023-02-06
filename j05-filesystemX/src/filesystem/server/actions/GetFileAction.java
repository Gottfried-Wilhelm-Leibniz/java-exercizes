package filesystem.server.actions;

import com.google.gson.Gson;
import filesystem.FileSystem;
import filesystem.server.Ansewrs.Error;
import filesystem.server.Ansewrs.FileAndData;
import filesystem.File;
import filesystem.server.actions.Action;
import java.io.IOException;

public class GetFileAction implements Action {
    @Override
    public Record doAction(FileSystem fs, String[] data) throws IOException {
        var fileName = data[0];
        var gson = new Gson();
        File file;
        try {
            file = fs.open(fileName);
        } catch (RuntimeException e) {
            return new Error(e.toString());
        }
        var bytes = file.readBytes(file.getSize());
        return new FileAndData(fileName, bytes);
    }
}
