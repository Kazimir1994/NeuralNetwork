package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.app;

import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.NeuralNetwork;
import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.NeuralNetwork2;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Arrays;

public class TestNMap {
    public static double randomValue(double lower_bound, double upper_bound) {
        return Math.random() * (upper_bound - lower_bound) + lower_bound;
    }

    public static void main(String[] args) {

        double[] target = new double[]{0.1, 0.5};
        int sizeInputData = 2500;
        int colEpoh = 1000;
        double[] inputData = new double[sizeInputData];
        for (int i = 0; i < sizeInputData; i++) {
            inputData[i] = i;
        }
        NeuralNetwork neuralNetwork = new NeuralNetwork(sizeInputData, 700, 10, 2);
        long l2 = System.nanoTime();
        for (int i = 0; i < colEpoh; i++) {
            double[] doublesA = neuralNetwork.calculateOutput(inputData);
              neuralNetwork.backPropagationError(target, 0.1);
        }
        long l3 = System.nanoTime();
        System.out.println("time " + (l3 - l2));
/*        double[] doublesA = neuralNetwork.calculateOutput(inputData);
        System.out.println(Arrays.toString(doublesA));*/

        NeuralNetwork2 neuralNetwork2 = new NeuralNetwork2(sizeInputData, 700, 40, 2);
        INDArray fromArray = Nd4j.createFromArray(inputData);
        long l = System.nanoTime();

        for (int i = 0; i < colEpoh; i++) {
            INDArray indArray = neuralNetwork2.calculateOutput(fromArray);
             neuralNetwork2.backPropagationError(Nd4j.create(target), 0.1);
        }
        long l1 = System.nanoTime();
        System.out.println("time " + (l1 - l));
   /*     INDArray indArray = neuralNetwork2.calculateOutput(inputData);
        System.out.println(indArray);
        System.out.println("end");*/

    }

    public static double[][] mull(double[][] a, double[][] b) {
        double[][] re = new double[a.length][b[0].length];

        for (int i = 0; i < a.length; i++) {
            for (int neuron = 0; neuron < b[0].length; neuron++) {
                double sum = 0;
                for (int prevNeuron = 0; prevNeuron < b.length; prevNeuron++) {
                    sum += a[i][prevNeuron] * b[prevNeuron][neuron];
                }
                re[i][neuron] = sum;
            }
        }
        return re;
    }
}
