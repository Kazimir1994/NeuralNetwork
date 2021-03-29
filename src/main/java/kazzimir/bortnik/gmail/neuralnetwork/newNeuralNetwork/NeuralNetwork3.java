package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class NeuralNetwork3 {
    public static void main(String[] args) {
        int[] NETWORK_LAYER_SIZES = {2, 4, 2};
        INDArray OUTPUT = Nd4j.rand(Nd4j.create(new double[1][NETWORK_LAYER_SIZES[0]]), -1, 1, Nd4j.getRandom());
        INDArray WEIGHTS = Nd4j.rand(Nd4j.create(new double[NETWORK_LAYER_SIZES[0]][NETWORK_LAYER_SIZES[1]]), -1, 1, Nd4j.getRandom());
        System.out.println(WEIGHTS);
        System.out.println();
        System.out.println(OUTPUT);
        System.out.println();
        INDArray mmul = OUTPUT.mmul(WEIGHTS);
        System.out.println(mmul);

    }
}
