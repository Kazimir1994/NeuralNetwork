package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork;

import org.datavec.api.conf.Configuration;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.records.listener.impl.LogRecordListener;
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
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.threeten.bp.Instant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MyDeplaning {
    public static void main(String[] args) throws IOException, InterruptedException {
/*
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(3)
                .weightInit(WeightInit.XAVIER)
                .updater(new Adam(0.001))
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
        model.setListeners(new ScoreIterationListener(1));*/

        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
        ImageRecordReader recordReader = new ImageRecordReader(40, 40, 3, labelMaker);
        recordReader.initialize(new FileSplit(new File("./newData3/n")));

        RecordReaderDataSetIterator iterator = new RecordReaderDataSetIterator.Builder(recordReader, 3)
                .classification(1, 2)
                .preProcessor(new ImagePreProcessingScaler())
                .build();
        while (true) {
            DataSet next = iterator.next();
            //INDArray transpose = next.getLabels().add(Nd4j.createFromArray(1, -1));
            //next.setLabels(transpose);
            System.out.println(next);
        }
      /*  DataSet next = iterator.next();
        next.setLabels(Nd4j.createFromArray(new int[][]{{0, 1}}));
        System.out.println(next);*/

/*        DataSet next = iterator.next();
        next.setLabels(Nd4j.createFromArray(new int[][]{{0, 1}}));
        System.out.println(next);
        DataSet next2 = iterator.next();
        next2.setLabels(Nd4j.createFromArray(new int[][]{{1, 0}}));
        System.out.println(next2);
        List<DataSet> list = new ArrayList<>();
        list.add(next);
        list.add(next2);
        DataSet merge = DataSet.merge(list);
        System.out.println(merge);*/
  /*      next.shuffle(888323);
        System.out.println("Full_dataSet" + next.asList().size());
        SplitTestAndTrain testAndTrain = next.splitTestAndTrain(2039999);
        DataSet trainingData = testAndTrain.getTrain();
        System.out.println("trainingData " + trainingData.asList().size());
        DataSet testData = testAndTrain.getTest();
        System.out.println("testData " + testData.asList().size());
        System.out.println("start");
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 2040; j++) {
                DataSet range = (DataSet) trainingData.getRange(j * 1000, (j * 1000 + 1000));
                range.shuffle(new Random().nextLong());
                model.fit(range);
                System.out.println("=========================Epoch -> " + i + "___iteratorDataSet ->" + j + "========================");
            }
        }
        System.out.println("end");
        System.out.println(Arrays.toString(testData.getFeatures().shape()));
        Evaluation eval = new Evaluation(2);
        INDArray output = model.output(testData.getFeatures());
        System.out.println(Arrays.toString(output.shape()));
        eval.eval(testData.getLabels(), output);
        System.out.println(eval.accuracy());
        System.out.println(eval.stats());
            model.save(new File("./N" + Instant.now() + ".h5"));*/
    }
}


