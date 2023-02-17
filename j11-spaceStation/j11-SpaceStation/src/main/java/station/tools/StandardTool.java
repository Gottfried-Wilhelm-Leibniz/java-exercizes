package station.tools;
import lombok.Getter;
import lombok.Setter;

public abstract class StandardTool implements Tool {
    @Getter
    private final String toolName;
    @Getter
    @Setter
    private ToolState toolState = ToolState.READY;

    public StandardTool(String name) {
        this.toolName = name;
    }

}

