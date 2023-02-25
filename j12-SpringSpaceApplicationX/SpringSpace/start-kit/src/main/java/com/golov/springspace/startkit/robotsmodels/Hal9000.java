package com.golov.springspace.startkit.robotsmodels;
import com.golov.springspace.infra.Tool;
import com.golov.springspace.startkit.robotsmodels.anotations.RobotAno;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.List;
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RobotAno
public class Hal9000 extends StandardRobot {
    public Hal9000(String name, String callSign, List<Tool> toolList) {
        super(name, callSign, toolList);
    }

}

// todo methood creation !
