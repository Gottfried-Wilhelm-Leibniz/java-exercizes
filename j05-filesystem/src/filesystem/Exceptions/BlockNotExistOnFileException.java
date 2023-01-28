package filesystem.Exceptions;

import java.io.IOException;

public class BlockNotExistOnFileException extends IOException {
    private final int blkNum;

    public BlockNotExistOnFileException(int blkNum) {
        this.blkNum = blkNum;
        System.out.println(blkNum);
    }
}
