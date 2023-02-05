package filesystem.server.actions;
import com.google.gson.Gson;
import filesystem.FileSystem;
import filesystem.server.Ansewrs.Error;
import filesystem.server.Ansewrs.FileAndSize;
import filesystem.server.Ansewrs.ResultAndFiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetDirAction implements Action {
    @Override
    public String doAction(FileSystem fs, String str) throws IOException {
        var gson = new Gson();
        List<String> filesList;
        try {
            filesList = fs.getFilesList();
        } catch (RuntimeException e) {
            var result = new Error(e.toString());
            return gson.toJson(result);
        }
        if(filesList.size() < 2) {
            var result = new Error("No files on disc");
            return gson.toJson(result);
        }

        var filesRecord = new ArrayList<FileAndSize>();
        for (var f : filesList) {
            var file = fs.open(f);
            filesRecord.add(new FileAndSize(f, file.getSize()));
        }
        var resultAndFiles = new ResultAndFiles("OK", filesRecord);
        return gson.toJson(resultAndFiles);
    }
}



//--------why not working ???----------
//var list = fs.getFilesList();
//    var filesList = new ArrayList<File>();
//        for (var f : list) {
//                filesList.add(fs.open(f));
//        }
//        var gson = new Gson();
//        System.out.println("done");
//        var json = gson.toJson(filesList);
//
//        return json;

// -----------making it only a name///////////////
//var g = new Gson();
//    var list = fs.getFilesList();
//    var filesList = new ArrayList<String>();
//        for (var f : list) {
//                filesList.add(g.toJson(f));
//                }
//                var gson = new Gson();
//                var json = gson.toJson(filesList);
//                return json;
//                }