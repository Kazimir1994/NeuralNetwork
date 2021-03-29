package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model;

import java.io.Serializable;

public class TestProcessing implements Processing, Serializable {
    private static final long serialVersionUID = 1L;
    private final String line = "1";

    @Override
    public String run() {
        return line;
    }
}
