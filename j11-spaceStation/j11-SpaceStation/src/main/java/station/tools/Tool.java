package station.tools;
public interface Tool {
    void setToolState(ToolState toolState);
    ToolState getToolState();
    String getToolName();
}
