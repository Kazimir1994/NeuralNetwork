package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model;

import java.util.Arrays;

public abstract class Layer {
    protected double[] neurons;
    protected double[][] weights;
    protected double[] errors;

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
                ", weights=" + Arrays.deepToString(weights)  +
                ", errors=" + Arrays.toString(errors) +
                '}';
    }
}
