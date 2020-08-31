package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model;

import kazzimir.bortnik.gmail.neuralnetwork.myneuralnetwork.RandomService;
import kazzimir.bortnik.gmail.neuralnetwork.myneuralnetwork.impl.RandomServiceImpl;

public class Layer_3 extends Layer {
    private final RandomService randomService = RandomServiceImpl.getInstants();

    public Layer_3() {
        neurons = new double[1];
        errors = new double[1];
        weights = new double[1][3];
        weightAssignment();
    }

    private void weightAssignment() {
/*        weights = new double[][]{
                {0.3, 0.5, 0.9}
        };*/
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = randomService.getRandomDouble(0.1, 0.9);
            }
        }
    }
}
