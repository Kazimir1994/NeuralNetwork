package kazzimir.bortnik.gmail.neuralnetwork.myneuralnetwork.app;

import kazzimir.bortnik.gmail.neuralnetwork.myneuralnetwork.NeuralNetwork;
import kazzimir.bortnik.gmail.neuralnetwork.myneuralnetwork.model.Layer;

import java.util.Arrays;
import java.util.function.UnaryOperator;

public class Main {
    public static void main(String[] args) {
        Layer layer_1 = new Layer(0, 2);
        Layer layer_2 = new Layer(2, 3);
        Layer layer_3 = new Layer(3, 1);
        UnaryOperator<Double> activation = x -> 1 / (1 + Math.exp(-x));
        UnaryOperator<Double> derivative = y -> y * (1 - y);
        NeuralNetwork neuralNetwork = new NeuralNetwork(0.9, activation, derivative, layer_1, layer_2, layer_3);

        double[][] inputParameters = {
                {0, 0},
                {0, 1},
                {1, 0},
                {1, 1}
        };


        double answer = 0;
        int sizeTraining = 0;
        double[] answers = {0, 1, 1, 0};
/*        double[] doubles = neuralNetwork.feedForward(inputParameters[1]);
        double backpropagation = neuralNetwork.backpropagation(answers[1]);*/
        do {
            sizeTraining++;
            answer = 0;
            System.out.println("===================================" + sizeTraining);
            for (int i = 0; i < inputParameters.length; i++) {
                double[] doubles = neuralNetwork.feedForward(inputParameters[i]);
                System.out.println(Arrays.toString(inputParameters[i]) + " -> " + answers[i] + "| actual->" + Arrays.toString(doubles));
                double backpropagation = neuralNetwork.backpropagation(answers[i]);
                System.out.println("error-> " + backpropagation);
                answer += Math.abs(backpropagation);
            }
        } while (answer != 0.0);
        System.out.println(sizeTraining);
    }
}
