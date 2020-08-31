package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model;

import kazzimir.bortnik.gmail.neuralnetwork.myneuralnetwork.RandomService;
import kazzimir.bortnik.gmail.neuralnetwork.myneuralnetwork.impl.RandomServiceImpl;

public class Layer_2 extends Layer {
    private final RandomService randomService = RandomServiceImpl.getInstants();

    public Layer_2() {
        neurons = new double[3];
        errors = new double[3];
        weights = new double[3][2];
        weightAssignment();
    }

    private void weightAssignment() {
/*        weights = new double[][]{
                {0.8, 0.2},
                {0.4, 0.9},
                {0.3, 0.5}
        };*/
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = randomService.getRandomDouble(0.1, 0.9);
            }
        }
    }
}
