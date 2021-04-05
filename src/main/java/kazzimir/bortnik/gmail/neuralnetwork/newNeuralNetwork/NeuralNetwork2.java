package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

import java.io.Serializable;
import java.util.UUID;

public class NeuralNetwork2 implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int[] NETWORK_LAYER_SIZES;
    private final int INPUT_SIZE;
    private final int OUTPUT_SIZE;
    private final int NETWORK_SIZE;
    private final INDArray[] OUTPUT;
    private final INDArray[] BIAS;
    private final INDArray[] ERROR_SIGNAL;
    private final INDArray[] OUTPUT_DERIVATIVE;
    private final INDArray[] WEIGHTS;

    public NeuralNetwork2(int... NETWORK_LAYER_SIZES) {
        this.NETWORK_LAYER_SIZES = NETWORK_LAYER_SIZES;
        this.INPUT_SIZE = NETWORK_LAYER_SIZES[0];
        this.NETWORK_SIZE = NETWORK_LAYER_SIZES.length;
        this.OUTPUT_SIZE = NETWORK_LAYER_SIZES[NETWORK_SIZE - 1];
        this.OUTPUT = new INDArray[NETWORK_SIZE];
        this.BIAS = new INDArray[NETWORK_SIZE];
        this.ERROR_SIGNAL = new INDArray[NETWORK_SIZE];
        this.OUTPUT_DERIVATIVE = new INDArray[NETWORK_SIZE];
        this.WEIGHTS = new INDArray[NETWORK_SIZE];

        for (int i = 0; i < NETWORK_SIZE; i++) {
            this.OUTPUT[i] = Nd4j.create(new double[NETWORK_LAYER_SIZES[i]]);
            double[] randomArray1 = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i], 1, 1);
            this.BIAS[i] = Nd4j.create(randomArray1);
            this.ERROR_SIGNAL[i] = Nd4j.create(NETWORK_LAYER_SIZES[i]);
            this.OUTPUT_DERIVATIVE[i] = Nd4j.create(NETWORK_LAYER_SIZES[i]);
            if (i > 0) {
                WEIGHTS[i] = Nd4j.rand(Nd4j.create(new double[NETWORK_LAYER_SIZES[i]][NETWORK_LAYER_SIZES[i - 1]]), 1, 1, Nd4j.getRandom());
            }
        }
    }

    public INDArray calculateOutput(INDArray input) {
        this.OUTPUT[0] = input;
        for (int layer = 1; layer < NETWORK_SIZE; layer++) {
            INDArray outputMultiplyWeights = OUTPUT[layer - 1]
                    .mmul(WEIGHTS[layer].transpose())
                    .add(BIAS[layer]);
            OUTPUT[layer] = Transforms.sigmoid(outputMultiplyWeights);
            OUTPUT_DERIVATIVE[layer] = Transforms.sigmoidDerivative(outputMultiplyWeights);
        }
        return this.OUTPUT[NETWORK_SIZE - 1];
    }

    public void backPropagationError(INDArray target, double learnRate) {
        ERROR_SIGNAL[NETWORK_SIZE - 1] = OUTPUT[NETWORK_SIZE - 1]
                .sub(target)
                .mulRowVector(OUTPUT_DERIVATIVE[NETWORK_SIZE - 1]);
        for (int layer = NETWORK_SIZE - 2; layer > 0; layer--) {
            this.ERROR_SIGNAL[layer] = ERROR_SIGNAL[layer + 1]
                    .mmul(WEIGHTS[layer + 1])
                    .muliRowVector(OUTPUT_DERIVATIVE[layer]);
        }
        updateWeights(learnRate);
    }

    private void updateWeights(double learnRate) {
        for (int layer = 1; layer < NETWORK_SIZE; layer++) {
            INDArray delta = ERROR_SIGNAL[layer].muli((-1 * learnRate));
            BIAS[layer] = delta.addRowVector(BIAS[layer]);
            INDArray deltaM = delta.transpose().mmul(OUTPUT[layer - 1]);
            WEIGHTS[layer] = WEIGHTS[layer].add(deltaM);
        }
    }

  /*  public void backPropagationError(INDArray target, double learnRate) {
        ERROR_SIGNAL[NETWORK_SIZE - 1] = OUTPUT[NETWORK_SIZE - 1]
                .subi(target)
                .muli(OUTPUT_DERIVATIVE[NETWORK_SIZE - 1]).transpose();
        for (int layer = NETWORK_SIZE - 2; layer > 0; layer--) {
            this.ERROR_SIGNAL[layer] = WEIGHTS[layer + 1]
                    .mmul(ERROR_SIGNAL[layer + 1])
                    .muliColumnVector(OUTPUT_DERIVATIVE[layer].transpose());
        }
        updateWeights(learnRate);
    }

    private void updateWeights(double learnRate) {
        for (int layer = 1; layer < NETWORK_SIZE; layer++) {
            INDArray delta = ERROR_SIGNAL[layer].muli((-1 * learnRate));
            BIAS[layer] = BIAS[layer].addiColumnVector(delta);
            INDArray deltaM = delta.mmul(OUTPUT[layer - 1]);
            WEIGHTS[layer] = WEIGHTS[layer].add(deltaM.transpose());
        }
    }*/
}
