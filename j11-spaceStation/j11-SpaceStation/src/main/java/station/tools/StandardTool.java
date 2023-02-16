package station.tools;
import lombok.Setter;

public abstract class StandardTool implements Tool {
    private final String name;
    @Setter
    private ToolState toolState = ToolState.READY;

    public StandardTool(String name) {
        this.name = name;
    }

}

