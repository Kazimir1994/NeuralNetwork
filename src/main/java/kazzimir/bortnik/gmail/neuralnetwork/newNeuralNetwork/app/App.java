package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.app;

import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.NeuralNetwork;
import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model.Layer;

import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        Layer layer_1 = new Layer(0, 2);
        Layer layer_2 = new Layer(2, 3);
        /*       Layer layer_3 = new Layer(3, 2);*/
        Layer layer_4 = new Layer(3, 1);
        System.out.println(Arrays.deepToString(layer_1.getWeights()));
        System.out.println(Arrays.deepToString(layer_2.getWeights()));
        System.out.println(Arrays.deepToString(layer_4.getWeights()));
        NeuralNetwork neuralNetwork = new NeuralNetwork(layer_1, layer_2, layer_4);

        double[][] inputParameters = {
                {0, 0},
                {0, 1},
                {1, 0},
                {1, 1}
        };

        double answer;
        double[] answers = {0, 1, 1, 0};
        do {
            answer = 0;
            for (int i = 0; i < inputParameters.length; i++) {
                neuralNetwork.feedForward(inputParameters[i]);
                double backpropagation = neuralNetwork.backpropagation(answers[i]);
                answer += Math.abs(backpropagation);
            }
           // System.out.println(answer);
        } while (answer > 0.01);
        for (int i = 0; i < inputParameters.length; i++) {
            double[] doubles = neuralNetwork.feedForward(inputParameters[i]);
            System.out.println(Arrays.toString(inputParameters[i]) + " -> " + answers[i] + "| actual->" + Arrays.toString(doubles));
        }
    }
}
