package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork;


import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model.Layer;

import java.util.function.UnaryOperator;

public class NeuralNetwork {
    private final double learningRate = 0.1;
    UnaryOperator<Double> activation = x -> 1 / (1 + Math.exp(-x));
    UnaryOperator<Double> derivative = y -> y * (1 - y);
    private final Layer[] layers;

    public NeuralNetwork(Layer... layers) {
        this.layers = layers;
    }

    public double[] feedForward(double[] inputs) {
        layers[0].setNeurons(inputs);
        for (int l = 1; l < layers.length; l++) {
            Layer layerPrevious = layers[l - 1];
            Layer layerCurrent = layers[l];
            for (int i = 0; i < layerCurrent.getNeurons().length; i++) {
                layerCurrent.getNeurons()[i] = 0;
                for (int j = 0; j < layerCurrent.getWeights()[i].length; j++) {
                    layerCurrent.getNeurons()[i] += layerPrevious.getNeurons()[j] * layerCurrent.getWeights()[i][j];
                }
                layerCurrent.getNeurons()[i] = activation.apply(layerCurrent.getNeurons()[i]);
            }
        }

        return layers[layers.length - 1].getNeurons();
    }

    public double backpropagation(double answer) {
        Layer layer = layers[layers.length - 1];
        layer.getErrors()[0] = answer - layers[layers.length - 1].getNeurons()[0];
        for (int l = layers.length - 2; l >= 1; l--) {
            Layer layerPrevious = layers[l + 1];
            Layer layerCurrent = layers[l];
            for (int nC = 0; nC < layerCurrent.getNeurons().length; nC++) {
                layerCurrent.getErrors()[nC] = 0;
                for (int nP = 0; nP < layerPrevious.getNeurons().length; nP++) {
                    layerCurrent.getErrors()[nC] += layerPrevious.getErrors()[nP] * layerPrevious.getWeights()[nP][nC];
                }
            }
        }
        for (int i = 1; i < layers.length; i++) {
            Layer layerPrevious = layers[i - 1];
            Layer layerCurrent = layers[i];
            for (int j = 0; j < layerCurrent.getNeurons().length; j++) {
                for (int k = 0; k < layerPrevious.getNeurons().length; k++) {
                    layerCurrent.getWeights()[j][k] +=
                            learningRate
                                    * layerCurrent.getErrors()[j]
                                    * layerPrevious.getNeurons()[k]
                                    * derivative.apply(layerCurrent.getNeurons()[j]);
                }
            }
        }
        return layer.getErrors()[0];
    }
}
