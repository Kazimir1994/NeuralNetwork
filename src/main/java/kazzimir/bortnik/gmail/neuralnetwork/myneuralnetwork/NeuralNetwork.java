package kazzimir.bortnik.gmail.neuralnetwork.myneuralnetwork;

import kazzimir.bortnik.gmail.neuralnetwork.myneuralnetwork.model.Layer;

import java.util.function.UnaryOperator;

public class NeuralNetwork {
    private final double learningRate;
    private final UnaryOperator<Double> activation, derivative;
    private final Layer[] layers;

    public NeuralNetwork(double learningRate,
                         UnaryOperator<Double> activation,
                         UnaryOperator<Double> derivative, Layer... layers) {
        this.learningRate = learningRate;
        this.activation = activation;
        this.derivative = derivative;
        this.layers = layers;
    }

    public double[] feedForward(double[] inputs) {
        layers[0].setNeurons(inputs);
        for (int l = 1; l < layers.length; l++) {
            Layer layerPrevious = layers[l - 1];
            Layer layerCurrent = layers[l];
            for (int i = 0; i < layerCurrent.getWeights().length; i++) {
                layerCurrent.getNeurons()[i] = 0;
                for (int j = 0; j < layerCurrent.getWeights()[i].length; j++) {
                    double a = layerPrevious.getNeurons()[j] * layerCurrent.getWeights()[i][j];
                    layerCurrent.getNeurons()[i] += a;
                }
                layerCurrent.getNeurons()[i] = activation.apply(layerCurrent.getNeurons()[i]);
    /*            if (layerCurrent.getNeurons()[i] > 0.5) {
                    layerCurrent.getNeurons()[i] = 1;
                } else {
                    layerCurrent.getNeurons()[i] = 0;
                }*/
            }
        }
        return layers[layers.length - 1].getNeurons();
    }

    public double backpropagation(double answer) {
   /*     System.out.println("====");
        Layer layer = layers[layers.length - 1];
        layer.getErrors()[0] = answer - layers[layers.length - 1].getNeurons()[0];
        for (int l = layers.length - 2; l >= 0; l--) {
            Layer layerPrevious = layers[l + 1];
            Layer layerCurrent = layers[l];
            for (int nC = 0; nC < layerCurrent.getNeurons().length; nC++) {
                layerCurrent.getErrors()[nC] = 0;
                for (int nP = 0; nP < layerPrevious.getNeurons().length; nP++) {
                    layerCurrent.getErrors()[nC] += layerPrevious.getErrors()[nP] * layerPrevious.getWeights()[nP][nC];
                }
            }
        }
        for (int l = 1; l < layers.length; l++) {
            Layer layerPrevious = layers[l - 1];
            Layer layerCurrent = layers[l];
            for (int wi = 0; wi < layerCurrent.getWeights().length; wi++) {
                for (int wj = 0; wj < layerCurrent.getWeights()[wi].length; wj++) {
                    double a = learningRate
                            * layerPrevious.getErrors()[wi]
                            * layerPrevious.getNeurons()[wj]
                            * derivative.apply(layerPrevious.getNeurons()[wj]);
                    layerCurrent.getWeights()[wi][wj] +=a;
                }
            }
        }
        return layer.getErrors()[0];*/
        return 0;
    }

    /*            for (int wi = 0; wi < layerPrevious.getWeights().length; wi++) {
                System.out.println(layerCurrent.getNeurons()[wi]);
                for (int wj = 0; wj < layerPrevious.getWeights()[wi].length; wj++) {
                    layerPrevious.getWeights()[wi][wj] = layerPrevious.getWeights()[wi][wj] + learningRate
                            * layerPrevious.getErrors()[wi] * layerCurrent.getNeurons()[wj];
                }
            }*/
}

