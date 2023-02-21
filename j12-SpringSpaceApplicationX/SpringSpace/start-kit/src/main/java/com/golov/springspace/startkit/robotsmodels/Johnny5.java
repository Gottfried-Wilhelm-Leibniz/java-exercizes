package com.golov.springspace.startkit.robotsmodels;
import com.golov.springspace.infra.Tool;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.List;
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Johnny5 extends StandardRobot {
    public Johnny5(String name, String callSign, List<Tool> toolList) {
        super(name, callSign, toolList);
    }
}
