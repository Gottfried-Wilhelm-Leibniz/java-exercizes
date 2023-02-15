package tools;
public abstract class Tool {
    private final String name;
    private ToolState toolState = ToolState.READY;

    public Tool(String name) {
        this.name = name;
    }

}
