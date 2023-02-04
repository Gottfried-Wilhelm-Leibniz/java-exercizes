package filesystem;

import java.util.List;

public class Inode {
    private final int inodeNum;
    private final int valid;
    private final int size;
    private final List<Integer> dirct;
    private final int  indirect;

    public Inode(int inodeNum, int valid, int size, List<Integer> dirct, int indirect) {
        this.inodeNum = inodeNum;
        this.valid = valid;
        this.size = size;
        this.dirct = dirct;
        this.indirect = indirect;
    }
}
