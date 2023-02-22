package com.golov.springspace.application;
import com.golov.springspace.startkit.toolmodels.*;
import com.golov.springspace.startkit.robotsmodels.*;
import com.golov.springspace.infra.*;
import com.golov.springspace.station.Reply;
import com.golov.springspace.station.SpaceStation;
import com.golov.springspace.station.Station;
import com.golov.springspace.station.fleet.Fleet;
import com.golov.springspace.station.fleet.RobotsFleet;
import com.golov.springspace.ui.StationUi;
import com.golov.springspace.ui.UiEnum;
import com.golov.springspace.ui.uiactions.*;
import input.Input;
import input.UserInput;
import loader.FileLoader;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
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
    public Reply createhal9000() {
        return station().createNew("hal9000");
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Johnny5 johnny5() {
        List<Tool> toolList = List.of(laserCutter(), staticBrush());
        return new Johnny5(robotname(), robotcallSign(), toolList);
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Reply createjohnny5() {
        return station().createNew("Johnny5");
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Tachikomas tachikomas() {
        List<Tool> toolList = List.of(laserCutter(), disruptor());
        return new Tachikomas(robotname(), robotcallSign(), toolList);
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Reply createtachikomas() {
        return station().createNew("tachikomas");
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Maschinemensch maschinenmensch() {
        List<Tool> toolList = List.of(replicator(), disruptor());
        return new Maschinemensch(robotname(), robotcallSign(), toolList);
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Reply createmaschinenmensch() {
        return station().createNew("maschinenmensch");
    }
    @Bean
    @Primary
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Reply create() {
        return station().createNew(null);
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
    public UiAction uiMenu() {
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
    public UiAction issuCommand() {
        return new IssuCommand();
    }
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public String getModels() {
        return station().listAvailableModels();
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
    public EnumMap<UiEnum, UiAction> actionEnumMap() {
        EnumMap<UiEnum, UiAction> m = new EnumMap<>(UiEnum.class);
        m.put(UiEnum.MENU, uiMenu());
        m.put(UiEnum.FLEETLIST, fleetList());
        m.put(UiEnum.PROVISION, provision());
        m.put(UiEnum.ISSUCOMMAND, issuCommand());
        return m;
    }

}
