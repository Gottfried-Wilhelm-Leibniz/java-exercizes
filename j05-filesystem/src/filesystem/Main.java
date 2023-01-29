package filesystem;
import filesystem.Exceptions.BufferIsNotTheSizeOfAblockException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class Main {
    private static final int NUMOFBLOCKS = 10;
    private static final int InodesBLOCKS = 1;
    private static final int BLOCKSIZE = 4000;
    private static final int MAGICNUMBER = 1695609641;
    private static final int INODESIZE = 32;
    private static final int INODESTOTAL = InodesBLOCKS * BLOCKSIZE / INODESIZE;
    public static void main(String[] args) throws IOException, BufferIsNotTheSizeOfAblockException {
//        var discController = DiscController.theOne(NUMOFBLOCKS, BLOCKSIZE);
//        //var disc = new Disc(Path.of("./discs"), MAGICNUMBER, NUMOFBLOCKS, InodesBLOCKS, INODESTOTAL,  INODESIZE, BLOCKSIZE);
//        var fs = new filesystem.FileSystem(discController.get(1));

        var byteList = new ArrayList<Byte>();
        try {
            byteList.add((byte)1);
            byteList.add((byte)2);
            byteList.add((byte)3);
        } catch (IndexOutOfBoundsException e) {}

        byte a = 3;
        String s = Byte.toString(a);
        System.out.println(s);
//        Base64.getEncoder().encodeToString(a);
//
//        byte[] array = byteList.stream().mapTo
//        System.out.println(Arrays.stream(byteList.toArray()).sequential().toString());
//        String s = Base64.getEncoder().encodeToString(byteList.toArray(new byte[byteList.size()]));
//        System.out.println(s);
//        var bf = (byte[]) byteList.toArray();
//        String s = new String(bf, StandardCharsets.UTF_8);
//
        var list = new ArrayList<Byte>();
        list.add((byte)4);
        list.add((byte)5);
        list.add((byte)5);
        list.add((byte)5);
        int[] arr = list.stream().mapToInt(i -> i).toArray();
        System.out.println(arr);
//        a.toArray(new TypeA[a.size()]);
    }
}
