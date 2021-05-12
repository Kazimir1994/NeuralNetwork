package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

public class DeepLearningLongDataSet {
    public static void main(String[] args) throws IOException, InterruptedException {
        MultiLayerNetwork multiLayerNetwork = buildMultiLayerNetwork();
        for (int i = 0; i < 50; i++) {
            int iterator = 0;
            Data dataSet = getDataSet();
            try {
                while (true) {
                    DataSet dataN = dataSet.getDataN();
                    DataSet dataP = dataSet.getDataP();
                    DataSet merge = DataSet.merge(List.of(dataN, dataP));
                    merge.shuffle();
                    multiLayerNetwork.fit(merge);
                    iterator++;
                    System.out.println("=========================Epoch -> " + i + " IteratorDataSet ->" + iterator + "========================");
                    if (iterator % 500 == 0) {
                        save(multiLayerNetwork, i, iterator);
                    }
                }
            } catch (NoSuchElementException noSuchElementException) {
                System.out.println("END Epoch " + i);
                save(multiLayerNetwork, i, iterator);
            }
        }
    }

    private static void save(MultiLayerNetwork multiLayerNetwork, int i, int iterator) throws IOException {
        multiLayerNetwork.save(new File("./Network"
                + "_Epoch_" + i
                + "_Iterator_" + iterator
                + "_" + Instant.now() + ".h5"));
    }

    private static MultiLayerNetwork buildMultiLayerNetwork() {
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(3)
                .weightInit(WeightInit.XAVIER)
                .updater(new Adam(0.0001))
                .l2(1e-4)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .list()
                .setInputType(InputType.convolutional(40, 40, 3))
                .layer(new ConvolutionLayer.Builder(3, 3)
                        .stride(1, 1)
                        .nIn(3)
                        .nOut(120)
                        .activation(Activation.RELU)
                        .build())
                .layer(new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .build())
                .layer(new ConvolutionLayer.Builder(3, 3)
                        .stride(1, 1)
                        .nOut(180)
                        .activation(Activation.RELU)
                        .build())
                .layer(new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .build())
                .layer(new DenseLayer.Builder().activation(Activation.RELU)
                        .nOut(900)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nOut(2)
                        .activation(Activation.SOFTMAX)
                        .build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(1));

        return model;
    }

    private static Data getDataSet() throws IOException, InterruptedException {
        String MainPath = "newData2";
        ParentPathLabelGenerator labelMakerP = new ParentPathLabelGenerator();
        RecordReader recordReaderP = new ImageRecordReader(40, 40, 3, labelMakerP);
        recordReaderP.initialize(new FileSplit(new File("./" + MainPath + "/p")));
        RecordReaderDataSetIterator p = new RecordReaderDataSetIterator.Builder(recordReaderP, 500)
                .classification(1, 2)
                .preProcessor(new ImagePreProcessingScaler())
                .build();

        ParentPathLabelGenerator labelMakerN = new ParentPathLabelGenerator();
        RecordReader recordReaderN = new ImageRecordReader(40, 40, 3, labelMakerN);
        recordReaderN.initialize(new FileSplit(new File("./" + MainPath + "/n")));
        RecordReaderDataSetIterator n = new RecordReaderDataSetIterator.Builder(recordReaderN, 500)
                .classification(1, 2)
                .preProcessor(new ImagePreProcessingScaler())
                .build();
        return new Data(n, p);
    }

    private static class Data {
        private final RecordReaderDataSetIterator N;
        private final RecordReaderDataSetIterator P;

        public Data(RecordReaderDataSetIterator n, RecordReaderDataSetIterator p) {
            N = n;
            P = p;
        }

        public DataSet getDataN() {
            DataSet next = N.next();
            INDArray transpose = next.getLabels().add(Nd4j.createFromArray(1, -1));
            next.setLabels(transpose);
            next.shuffle(10);
            return next;
        }

        public DataSet getDataP() {
            DataSet next = P.next();
            next.shuffle(10);
            return next;
        }
    }
}
