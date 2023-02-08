package tvmprog.Translator;

import java.util.BitSet;

public class BitsTranslator implements Translator{
    @Override
    public int binaryToDecimal(BitSet bitSet) {
        var result = 0;
        for (int i = 0; i < bitSet.size() -1; i++) {
            if(bitSet.get(i)) {
                result += Math.pow(2, i);
            }
        }
        if(bitSet.get(bitSet.size() - 1)) {
            result *= -1;
        }
        return result;
    }
}
