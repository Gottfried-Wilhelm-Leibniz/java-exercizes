package com.golov.springspace.application;
import com.golov.springspace.application.generalactions.UniverseCosmicAction;
import com.golov.springspace.startkit.toolmodels.*;
import com.golov.springspace.startkit.robotsmodels.*;
import com.golov.springspace.infra.*;
import com.golov.springspace.station.SpaceStation;
import com.golov.springspace.station.Station;
import com.golov.springspace.station.fleet.Fleet;
import com.golov.springspace.station.fleet.RobotsFleet;
import com.golov.springspace.ui.StationUi;
import com.golov.springspace.ui.UiEnum;
import com.golov.springspace.ui.context.Context;
import com.golov.springspace.ui.uiactions.*;
import input.Input;
import input.UserInput;
import loader.FileLoader;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
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
    @Bean
    public Printer printer() {
        return new SoutPrinter();
    }
//    @Bean("rununi")
//    public UniverseCosmicAction runUniverseThread() {
//        return new UniverseCosmicAction(robotsFleet(), printer());
//    }
    @Bean
    public Station<Robot> station() {
        return new SpaceStation();
    }
    @Bean
    public ExecutorService executorService() {
        return Executors.newCachedThreadPool();
    }
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
    public Context context() {
        var ui = stationUi();
        return new Context(ui::print, ui::input, ui::getFleetList, ui::getModels,
                ui::createNew, ui:: getAvailableRobots, ui::getRobotDetails, ui::commandRobot);
    }

    @Bean
    public UiAction uiMenu() {
        return new UiMenu(context());
    }
    @Bean
    public UiAction fleetList() {
        return new FleetList(context());
    }
    @Bean
    public UiAction provision() {
        return new Provision(context());
    }
    @Bean
    public UiAction issuCommand() {
        return new IssuCommand(context());
    }
    @Bean
    public EnumMap<UiEnum, UiAction> actionEnumMap() {
        EnumMap<UiEnum, UiAction> m = new EnumMap<>(UiEnum.class);
        m.put(UiEnum.MENU, uiMenu());
        m.put(UiEnum.FLEETLIST, fleetList());
        m.put(UiEnum.PROVISION, provision());
        m.put(UiEnum.ISSUCOMMAND, issuCommand());
        return m;
    }




}
