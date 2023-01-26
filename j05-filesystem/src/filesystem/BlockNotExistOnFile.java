package filesystem;

import java.io.IOException;

public class BlockNotExistOnFile extends IOException {
    private final int blkNum;

    public BlockNotExistOnFile(int blkNum) {
        this.blkNum = blkNum;
        System.out.println(blkNum);
    }
}
