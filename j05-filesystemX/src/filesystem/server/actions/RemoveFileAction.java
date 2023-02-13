package filesystem.server.actions;

import com.google.gson.Gson;
import filesystem.FileSystem;
import filesystem.server.Ansewrs.Error;
import filesystem.server.Ansewrs.Result;
import filesystem.server.actions.Action;
import java.io.IOException;

public class RemoveFileAction implements Action {
    @Override
    public Record doAction(FileSystem fs, String[] data) throws IOException {
        var toRemove = data[0];
        var gson = new Gson();
        try {
            fs.deleteFile(toRemove);
        } catch (RuntimeException e) {
           return new Error(e.toString());
        }
       return new Result("Ok");
    }
}
