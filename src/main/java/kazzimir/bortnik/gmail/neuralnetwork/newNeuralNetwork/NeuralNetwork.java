package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork;


import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model.Image;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

public class NeuralNetwork implements Serializable {
    private static final long serialVersionUID = 1L;
    public transient UnaryOperator<Double> activation = x -> 1 / (1 + Math.exp(-1 * x));
    public transient UnaryOperator<Double> derivative = y -> y * (1 - y);

    private final int[] NETWORK_LAYER_SIZES;
    private final int INPUT_SIZE;
    private final int OUTPUT_SIZE;
    private final int NETWORK_SIZE;
    private final double[][] output;
    private final double[][][] weights;
    private final double[][] bias;
    private final double[][] error_signal;
    private final double[][] output_derivative;

    public NeuralNetwork(int... NETWORK_LAYER_SIZES) {
        this.NETWORK_LAYER_SIZES = NETWORK_LAYER_SIZES;
        this.INPUT_SIZE = NETWORK_LAYER_SIZES[0];
        this.NETWORK_SIZE = NETWORK_LAYER_SIZES.length;
        this.OUTPUT_SIZE = NETWORK_LAYER_SIZES[NETWORK_SIZE - 1];
        this.output = new double[NETWORK_SIZE][];
        this.weights = new double[NETWORK_SIZE][][];
        this.bias = new double[NETWORK_SIZE][];
        this.error_signal = new double[NETWORK_SIZE][];
        this.output_derivative = new double[NETWORK_SIZE][];

        for (int i = 0; i < NETWORK_SIZE; i++) {
            this.output[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.bias[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i], -0.5, 0.7);
            this.error_signal[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.output_derivative[i] = new double[NETWORK_LAYER_SIZES[i]];
            if (i > 0) {
                weights[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i], NETWORK_LAYER_SIZES[i - 1], -1, 1);
            }
        }
        for (int i = 1; i < weights.length; i++) {
            System.out.println(weights[i][0].length+"X"+weights[i].length);

        }
    }

    public double[] calculateOutput(double... input) {
        if (input.length != this.INPUT_SIZE) {
            throw new IllegalArgumentException("the number of incoming neurons must be equal to the number of neurons in the layer");
        }
        this.output[0] = input;
        for (int layer = 1; layer < NETWORK_SIZE; layer++) {
            for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                double sum = bias[layer][neuron];
                for (int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer - 1]; prevNeuron++) {
                    sum += (output[layer - 1][prevNeuron]) * (weights[layer][neuron][prevNeuron]);
                }
                output[layer][neuron] = activation.apply(sum);
                output_derivative[layer][neuron] = derivative.apply(output[layer][neuron]);
            }
        }
        return output[NETWORK_SIZE - 1];
    }

    public void trainNetwork(double[] input, double[] target, double learnRate) {
        if (input.length != INPUT_SIZE || target.length != OUTPUT_SIZE) {
            throw new IllegalArgumentException("the number of incoming neurons must be equal to the number of neurons in the layer");
        }
        double[] doubles = calculateOutput(input);
        backPropagationError(target, learnRate);
    }

    public void backPropagationError(double[] target, double learnRate) {
        for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[NETWORK_SIZE - 1]; neuron++) {
            error_signal[NETWORK_SIZE - 1][neuron] = (output[NETWORK_SIZE - 1][neuron] -
                    target[neuron]) * (output_derivative[NETWORK_SIZE - 1][neuron]);
        }
        for (int layer = NETWORK_SIZE - 2; layer > 0; layer--) {
            for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                double sum = 0;
                for (int nextNeuron = 0; nextNeuron < NETWORK_LAYER_SIZES[layer + 1]; nextNeuron++) {
                    sum += (weights[layer + 1][nextNeuron][neuron]) *
                            (error_signal[layer + 1][nextNeuron]);
                }
                this.error_signal[layer][neuron] = sum * output_derivative[layer][neuron];
            }
        }
        updateWeights(learnRate);
    }

    private void updateWeights(double learnRate) {
        for (int layer = 1; layer < NETWORK_SIZE; layer++) {
            for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                double delta = (-1 * learnRate) * error_signal[layer][neuron];
                bias[layer][neuron] += delta;
                for (int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer - 1]; prevNeuron++) {
                    weights[layer][neuron][prevNeuron] += delta * output[layer - 1][prevNeuron];
                }
            }
        }
    }

    public double MSE(double[] input, double[] target, double learnRate) {
        if (input.length != INPUT_SIZE || target.length != OUTPUT_SIZE) {
            return 0;
        }
        calculateOutput(input);
        backPropagationError(target, learnRate);
        double v = 0;
        for (int i = 0; i < target.length; i++) {
            v += (target[i] - output[NETWORK_SIZE - 1][i]) * (target[i] - output[NETWORK_SIZE - 1][i]);
        }
        return v / (2d * target.length);
    }

    public double MSE(List<Image> data, double learnRate) {
        double v = 0;
        for (Image image : data) {
            v += MSE(image.getInputData(), image.getAnswer(), learnRate);
        }
        return v / data.size();
    }
}
