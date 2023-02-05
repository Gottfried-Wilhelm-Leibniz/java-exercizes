package filesystem.server.actions;

import com.google.gson.Gson;
import filesystem.FileSystem;
import filesystem.server.Ansewrs.Error;
import filesystem.server.Ansewrs.FileAndData;
import filesystem.File;

import java.io.IOException;

public class GetFileAction implements filesystem.server.actions.Action {
    @Override
    public String doAction(FileSystem fs, String fileName) throws IOException {
        var gson = new Gson();
        File file;
        try {
            file = fs.open(fileName);
        } catch (RuntimeException e) {
            var result = new Error(e.toString());
            return gson.toJson(result);
        }
        var bytes = file.readBytes(file.getSize());
        var fileAndData = new FileAndData(fileName, bytes);
        return gson.toJson(fileAndData);
    }
}
