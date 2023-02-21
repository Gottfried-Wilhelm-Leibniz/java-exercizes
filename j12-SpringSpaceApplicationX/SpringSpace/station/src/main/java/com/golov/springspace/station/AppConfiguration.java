package com.golov.springspace.station;
import com.golov.springspace.startkit.toolmodels.*;
import com.golov.springspace.startkit.robotsmodels.*;
import com.golov.springspace.infra.*;
import com.golov.springspace.station.fleet.Fleet;
import com.golov.springspace.station.fleet.RobotsFleet;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import randomizer.Randomizer;

import java.util.List;

@Configuration
@ComponentScan
public class AppConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Disruptor disruptor() {
        return new Disruptor();
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public LaserCutter laserCutter() {
        return new LaserCutter();
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Replicator replicator() {
        return new Replicator();
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public StaticBrush staticBrush() {
        return new StaticBrush();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Hal9000 hal9000() {
        List<Tool> toolList = List.of(laserCutter(), replicator(), disruptor());
        return new Hal9000(robotname(), robotcallSign(), toolList);
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Johnny5 johnny5() {
        List<Tool> toolList = List.of(laserCutter(), staticBrush());
        return new Johnny5(robotname(), robotcallSign(), toolList);
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Tachikomas tachikomas() {
        List<Tool> toolList = List.of(laserCutter(), disruptor());
        return new Tachikomas(robotname(), robotcallSign(), toolList);
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Maschinemensch maschinenmensch() {
        List<Tool> toolList = List.of(replicator(), disruptor());
        return new Maschinemensch(robotname(), robotcallSign(), toolList);
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public String robotname() {
        return new Randomizer().randomString(5);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public String robotcallSign() {
        return new Randomizer().randomString(3);
    }

    @Bean
    public Fleet<Robot> robotsFleet() {
        return new RobotsFleet();
    }

}
