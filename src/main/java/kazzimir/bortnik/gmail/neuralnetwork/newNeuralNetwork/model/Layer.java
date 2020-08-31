package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model;

import kazzimir.bortnik.gmail.neuralnetwork.myneuralnetwork.RandomService;
import kazzimir.bortnik.gmail.neuralnetwork.myneuralnetwork.impl.RandomServiceImpl;

import java.util.Arrays;

public class Layer {
    private final RandomService randomService = RandomServiceImpl.getInstants();
    protected double[] neurons;
    protected double[][] weights;
    protected double[] errors;

    public Layer(int inputNeurons, int sizeNeurons) {
        neurons = new double[sizeNeurons];
        errors = new double[sizeNeurons];
        weights = new double[sizeNeurons][inputNeurons];
        weightAssignment();
    }

    public double[] getNeurons() {
        return neurons;
    }

    public double[][] getWeights() {
        return weights;
    }

    public void setNeurons(double[] neurons) {
        this.neurons = neurons;
    }

    public void setWeights(double[][] weights) {
        this.weights = weights;
    }

    public double[] getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "Layer{" +
                "neurons=" + Arrays.toString(neurons) +
                ", weights=" + Arrays.deepToString(weights) +
                ", errors=" + Arrays.toString(errors) +
                '}';
    }

    private void weightAssignment() {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = randomService.getRandomDouble(0.1, 0.5);
            }
        }
    }
}
