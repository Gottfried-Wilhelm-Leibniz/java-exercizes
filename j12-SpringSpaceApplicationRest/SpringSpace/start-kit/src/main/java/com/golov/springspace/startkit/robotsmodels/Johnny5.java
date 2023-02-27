package com.golov.springspace.startkit.robotsmodels;
import com.golov.springspace.infra.Tool;
import com.golov.springspace.startkit.robotsmodels.anotations.JohnnyTool;
import com.golov.springspace.startkit.robotsmodels.anotations.RobotAno;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import randomizer.Randomizer;

import java.util.List;
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Johnny5 extends StandardRobot {
    public Johnny5(@JohnnyTool List<Tool> toolList, Randomizer randomizer) {
        super(toolList, randomizer.boolRandom(0.9));
    }
}
