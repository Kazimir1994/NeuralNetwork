package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model;

public enum EnumTableName {
    TOOL("Tool", ElementVision.class),
    BORDER("Border", ElementVision.class),
    SMART_BOARD("SmartBoard", SmartBoard.class);

    private final String name;
    private final Class<?> aClass;

    EnumTableName(String name, Class<?> aClass) {
        this.name = name;
        this.aClass = aClass;
    }

    public Class<?> getTypeClass() {
        return aClass;
    }

    public String getName() {
        return name;
    }
}
