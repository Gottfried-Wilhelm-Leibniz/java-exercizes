package filesystem.server.Ansewrs;

import filesystem.server.Ansewrs.FileAndSize;

import java.util.List;

public record ResultAndFiles(String result, List<FileAndSize> files) {
}
