package com.golov.springspace.application;
import com.golov.springspace.application.generalactions.UniverseCosmicAction;
import com.golov.springspace.startkit.toolmodels.*;
import com.golov.springspace.startkit.robotsmodels.*;
import com.golov.springspace.infra.*;
import com.golov.springspace.station.Reply;
import com.golov.springspace.station.RobotOrder;
import com.golov.springspace.station.SpaceStation;
import com.golov.springspace.station.Station;
import com.golov.springspace.station.exceptions.RobotNotActiveException;
import com.golov.springspace.station.exceptions.RobotNotFailingException;
import com.golov.springspace.station.fleet.Fleet;
import com.golov.springspace.station.fleet.RobotsFleet;
import com.golov.springspace.station.robotactions.DispatchAction;
import com.golov.springspace.station.robotactions.Reboot;
import com.golov.springspace.station.robotactions.RobotAction;
import com.golov.springspace.station.robotactions.SelfDiagnostic;
import com.golov.springspace.ui.StationUi;
import com.golov.springspace.ui.UiEnum;
import com.golov.springspace.ui.uiactions.*;
import input.Input;
import input.UserInput;
import loader.FileLoader;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import output.Printer;
import output.SoutPrinter;
import parser.Parser;
import randomizer.Randomizer;

import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan
@EnableAsync
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
    public Hal9000 hal9000(String name, String callSign) {
//        List<Tool> toolList = List.of(laserCutter(), replicator(), disruptor());
//        return new Hal9000();
        return new Hal9000(name, callSign, List.of(laserCutter(), replicator(), disruptor()));
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
    public Reply commandRobot(RobotOrder robotOrder, String callSign) {
        return station().commandRobot(robotOrder, callSign);
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
    public Maschinemensch maschinenmensch(String name, String callSign) {
        return new Maschinemensch(name, callSign, List.of(replicator(), disruptor()));
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Fleet<Robot> robotsFleet() {
        return new RobotsFleet();
    }
    @Bean
    public Printer printer() {
        return new SoutPrinter();
    }

    @Bean
    public Station<Robot> station() {
        return new SpaceStation();
    }
//    @Bean
//    public ExecutorService executorService() {
//        return Executors.newCachedThreadPool();
//    }
    @Bean
    public Input input() {
        return new UserInput();
    }
    @Bean
    public StationUi stationUi() {
        return new StationUi();
    }

    @Bean
    public FileLoader fileLoader() {
        return new FileLoader();
    }
    @Bean
    public Parser parser() {
        return new Parser();
    }
    @Bean
    public Randomizer randomizer() {
        return new Randomizer();
    }

    @Bean
    public UiAction menu() {
        return new UiMenu();
    }
    @Bean
    public UiAction fleetList() {
        return new FleetList();
    }
    @Bean
    public UiAction provision() {
        return new Provision();
    }
    @Bean
    public UiAction issucommand() {
        return new IssuCommand();
    }
    @Bean
    public UiAction quit() {
        return new Quit();
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
    public Reply quitSystem() {
        return station().quit();
    }

    @Bean
    public UniverseCosmicAction universeCosmicAction() {
        return new UniverseCosmicAction();
    }

    @Bean
    public ExecutorService taskExecutor() {
        return Executors.newCachedThreadPool();
    }


}


//    @Bean
//    public EnumMap<UiEnum, UiAction> actionEnumMap() {
//        EnumMap<UiEnum, UiAction> m = new EnumMap<>(UiEnum.class);
//        m.put(UiEnum.MENU, uiMenu());
//        m.put(UiEnum.FLEETLIST, fleetList());
//        m.put(UiEnum.PROVISION, provision());
//        m.put(UiEnum.ISSUCOMMAND, issuCommand());
//        return m;
//    }

//    @Bean
//    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//    public String getModels() {
//        return station().listAvailableModels();
//    }