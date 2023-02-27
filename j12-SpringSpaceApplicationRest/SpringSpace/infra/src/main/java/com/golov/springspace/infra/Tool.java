package com.golov.springspace.infra;
public interface Tool {
    void setToolState(ToolState toolState);
    ToolState getToolState();
    String getToolName();
}
