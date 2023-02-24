package com.golov.springspace.startkit.toolmodels;
import com.golov.springspace.startkit.robotsmodels.anotations.Hal9000Tool;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Hal9000Tool
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Replicator extends StandardTool {
    public Replicator() {
        super("Replicator");
    }
}
