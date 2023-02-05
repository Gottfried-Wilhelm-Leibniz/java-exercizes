package filesystem.server.actions;

import com.google.gson.Gson;
import filesystem.FileSystem;
import filesystem.server.Ansewrs.Error;
import filesystem.server.Ansewrs.Result;

import java.io.IOException;

public class RemoveFileAction implements filesystem.server.actions.Action {
    @Override
    public String doAction(FileSystem fs, String toRemove) throws IOException {
        var gson = new Gson();
        try {
            fs.deleteFile(toRemove);
        } catch (RuntimeException e) {
            var result = new Error(e.toString());
            return gson.toJson(result);
        }
        var result = new Result("Ok");
        return gson.toJson(result);
    }
}
