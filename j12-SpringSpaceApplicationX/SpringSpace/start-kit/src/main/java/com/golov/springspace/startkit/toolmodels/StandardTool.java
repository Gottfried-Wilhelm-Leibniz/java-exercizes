package com.golov.springspace.startkit.toolmodels;
import com.golov.springspace.infra.*;
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
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}

