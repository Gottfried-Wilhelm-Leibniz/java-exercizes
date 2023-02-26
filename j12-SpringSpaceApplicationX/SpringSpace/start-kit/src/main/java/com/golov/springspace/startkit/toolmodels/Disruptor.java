package com.golov.springspace.startkit.toolmodels;
import com.golov.springspace.startkit.robotsmodels.anotations.Hal9000Tool;
import com.golov.springspace.startkit.robotsmodels.anotations.MaschinemenschTool;
import com.golov.springspace.startkit.robotsmodels.anotations.TachikomasTool;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Hal9000Tool
@TachikomasTool
@MaschinemenschTool
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Disruptor extends StandardTool {
    public Disruptor() {
        super("Disruptor");
    }
}
