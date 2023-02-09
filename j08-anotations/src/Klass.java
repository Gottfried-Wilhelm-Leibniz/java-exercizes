import lombok.Getter;

public class Klass {
    @Getter
    @Randomize
    private int ivalue;

    @Getter
    @Randomize(low = 1, high = 6)
    private int dice;
    @Getter
    @Randomize
    private double dvalue;
    @Getter
    @Randomize(low = -1000, high = 1000)
    private long lvalue;
    @Getter
    @Randomize(length = 18)
    private String name;
}
