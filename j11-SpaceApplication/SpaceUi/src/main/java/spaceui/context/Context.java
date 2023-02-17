package spaceui.context;
import spaceui.input.Input;
import spaceui.output.Printer;
import station.Reply;
import station.robot.RobotOrder;

public class Context {
    private final Printer printer;
    private final Input input;
    private final GetFleetList getFleetList;
    private final GetModels getModels;
    private final CreateNew createNew;
    private final GetAvailableRobots getAvailableRobots;
    private final GetRobotDetails getit;
    private final CommandRobot commandRobot;
    public Context(Printer printer, Input input, GetFleetList getFleetList, GetModels getModels, CreateNew createNew, GetAvailableRobots getAvailableRobots, GetRobotDetails getRobotDetails, CommandRobot commandRobot) {
        this.printer = printer;
        this.input = input;
        this.getFleetList = getFleetList;
        this.getModels = getModels;
        this.createNew = createNew;
        this.getAvailableRobots = getAvailableRobots;
        this.getit = getRobotDetails;
        this.commandRobot = commandRobot;
    }

    public void printIt(String s) {
        printer.print(s);
    }
    public String inputIt() {
        return input.in();
    }
    public String getFleetList() {
        return getFleetList.getFleet();
    }
    public String getModels() {
        return getModels.getTheModels();
    }
    public Reply createNew(String model, String name, String sign) {
        return createNew.create(model, name, sign);
    }
    public String getAvailableRobots() {
        return getAvailableRobots.getAvailable();
    }
    public Reply getRobotDetails(String callsign) {
        return getit.getRobotDetails(callsign);
    }
    public Reply commandRobot(RobotOrder robotOrder, String callSign) {
        return commandRobot.orderRobot(robotOrder, callSign);
    }
}
