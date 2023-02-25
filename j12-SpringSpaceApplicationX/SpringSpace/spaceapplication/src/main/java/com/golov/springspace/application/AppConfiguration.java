package com.golov.springspace.application;
import com.golov.springspace.application.generalactions.LoadService;
import com.golov.springspace.startkit.toolmodels.*;
import com.golov.springspace.startkit.robotsmodels.*;
import com.golov.springspace.infra.*;
import com.golov.springspace.station.Reply;
import com.golov.springspace.station.SpaceStation;
import com.golov.springspace.station.Station;
import com.golov.springspace.station.robotactions.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import java.util.List;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan(basePackages = {"randomizer", "loader", "output", "input", "com.golov.springspace", "parser"})
@EnableAsync
@EnableScheduling
public class AppConfiguration implements AsyncConfigurer {

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
    public Hal9000 hal9000(String name, String callSign) {
        return new Hal9000(name, callSign, List.of(laserCutter(), replicator(), disruptor()));
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Johnny5 johnny5(String name, String callSign) {
        return new Johnny5(name, callSign, List.of(laserCutter(), staticBrush()));
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Tachikomas tachikomas(String name, String callSign) {
        return new Tachikomas(name, callSign, List.of(laserCutter(), disruptor()));
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Maschinemensch maschinemensch(String name, String callSign) {
        return new Maschinemensch(name, callSign, List.of(replicator(), disruptor()));
    }



    @Bean
    public Station<Robot> station() {
        return new SpaceStation();
    }



    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Reply createNew(String model, String name, String callSign) {
        return station().createNew(model, name, callSign);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Reply getRobotDetails(String callSign) {
        return station().getRobotDetails(callSign);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Reply commandRobot(String command, String callSign) {
        return station().commandRobot(command, callSign);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public String getFleetList() {
        return station().getFleetList();
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public String getAvailableRobots() {
        return station().listAvailableRobots();
    }



    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RobotAction dispatch(Robot r) {
        return new DispatchAction(r);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RobotAction reboot(Robot r) {
        return new Reboot(r);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RobotAction diagnostic(Robot r) {
        return new SelfDiagnostic(r);
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RobotAction remove(Robot r) {
        return new RemoveRobot(r);
    }



    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public LoadService loadService(String arg) {
        return new LoadService(arg);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new ConcurrentTaskExecutor(Executors.newCachedThreadPool());
//        return Executors.newCachedThreadPool(); //
    }

}