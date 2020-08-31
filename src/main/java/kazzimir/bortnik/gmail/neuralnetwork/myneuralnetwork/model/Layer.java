package kazzimir.bortnik.gmail.neuralnetwork.myneuralnetwork.model;

import kazzimir.bortnik.gmail.neuralnetwork.myneuralnetwork.RandomService;
import kazzimir.bortnik.gmail.neuralnetwork.myneuralnetwork.impl.RandomServiceImpl;

import java.util.Arrays;

public class Layer {
    private final RandomService randomService = RandomServiceImpl.getInstants();
    private double[] neurons;
    private double[] errors;
    private final double[][] weights;

    public Layer(int inputNeurons, int sizeNeurons) {
        neurons = new double[sizeNeurons];
        errors = new double[sizeNeurons];
        weights = new double[sizeNeurons][inputNeurons];
        weightAssignment();
        System.out.println(Arrays.deepToString(weights));
    }

    public double[] getErrors() {
        return errors;
    }

    public double[] getNeurons() {
        return neurons;
    }

    public void setNeurons(double[] neurons) {
        this.neurons = neurons;
    }

    public double[][] getWeights() {
        return weights;
    }

    private void weightAssignment() {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = randomService.getRandomDouble(0.01, 0.3);
            }
        }
    }

    @Override
    public String toString() {
        return "Layer{" +
                "randomService=" + randomService +
                ", neurons=" + Arrays.toString(neurons) +
                ", errors=" + Arrays.toString(errors) +
                ", weights=" + Arrays.deepToString(weights) +
                '}';
    }
}
