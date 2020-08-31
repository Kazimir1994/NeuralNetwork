package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.app;

import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.NeuralNetwork;
import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model.Layer_1;
import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model.Layer_2;
import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model.Layer_3;

import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        Layer_1 layer_1 = new Layer_1();
        Layer_2 layer_2 = new Layer_2();
        Layer_3 layer_3 = new Layer_3();

        NeuralNetwork neuralNetwork = new NeuralNetwork(layer_1, layer_2, layer_3);
/*
        double[][] inputParameters = {
                {0, 0},
                {0, 1},
                {1, 0},
                {1, 1}
        };
        double[] answers = {0, 1, 1, 0};
        double[] doubles = neuralNetwork.feedForward(inputParameters[3]);
        System.out.println(Arrays.toString(doubles));
        double backpropagation = neuralNetwork.backpropagation(answers[3]);

*/

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
          //      System.out.println(Arrays.toString(inputParameters[i]) + " -> " + answers[i] + "| actual->" + Arrays.toString(doubles));
                double backpropagation = neuralNetwork.backpropagation(answers[i]);
                //System.out.println("error-> " + backpropagation);
                answer += Math.abs(backpropagation);
            }
            System.out.println(answer);
        } while (answer > 0.1);
        System.out.println(sizeTraining);
    }
}
