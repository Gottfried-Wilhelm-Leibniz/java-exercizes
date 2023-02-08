package newtvm.codeword;

import newtvm.exceptions.NotExecutableWord;
import newtvm.context.Context;

public class DataWord implements CodeWord {
    private final int data;

    public DataWord(int data) {
        this.data = data;
    }

    @Override
    public void execute(Context context) throws NotExecutableWord {
        throw new NotExecutableWord("im data");
    }

    @Override
    public int data() {
        return data;
    }
}
