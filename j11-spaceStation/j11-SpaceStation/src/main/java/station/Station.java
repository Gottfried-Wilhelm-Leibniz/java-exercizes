package station;
public interface Station<T> {
    String getFleetList();
    String getAvailableModels();
    Reply createNew(String model, String name, String sign);
}
