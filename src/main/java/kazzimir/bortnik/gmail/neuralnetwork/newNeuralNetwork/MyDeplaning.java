package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork;

import com.twelvemonkeys.imageio.ImageReaderBase;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.records.listener.impl.LogRecordListener;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.core.storage.StatsStorage;
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
/*
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.model.stats.StatsListener;
import org.deeplearning4j.ui.model.storage.InMemoryStatsStorage;
*/
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.model.stats.StatsListener;
import org.deeplearning4j.ui.model.storage.InMemoryStatsStorage;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.image.ImageResize;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.CropAndResizeDataSetPreProcessor;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.learning.config.Nadam;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.threeten.bp.Instant;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class MyDeplaning {
    public static void main(String[] args) throws IOException, InterruptedException {
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
                        .nOut(80)
                        .activation(Activation.RELU)
                        .build())
                .layer(new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .build())
                .layer(new ConvolutionLayer.Builder(3, 3)
                        .stride(1, 1)
                        .nOut(80)
                        .activation(Activation.RELU)
                        .build())
                .layer(new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .build())
                .layer(new DenseLayer.Builder().activation(Activation.RELU)
                        .nOut(500)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nOut(2)
                        .activation(Activation.SOFTMAX)
                        .build())
                .build();

        //   MultiLayerNetwork model = MultiLayerNetwork.load(new File("N2021-04-05T11:39:52.574Z.h5"), true);

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(1));

              //Initialize the user interface backend
        UIServer uiServer = UIServer.getInstance();

        //Configure where the network information (gradients, score vs. time etc) is to be stored. Here: store in memory.
        StatsStorage statsStorage = new InMemoryStatsStorage();         //Alternative: new FileStatsStorage(File), for saving and loading later

        //Attach the StatsStorage instance to the UI: this allows the contents of the StatsStorage to be visualized
        uiServer.attach(statsStorage);

        //Then add the StatsListener to collect this information from the network, as it trains
        model.setListeners(new StatsListener(statsStorage));


        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
        RecordReader recordReader = new ImageRecordReader(40, 40, 3, labelMaker);
        // recordReader.setListeners(new LogRecordListener());
        recordReader.initialize(new FileSplit(new File("./newData")));

        DataSetIterator iterator = new RecordReaderDataSetIterator.Builder(recordReader, 13964)
                .classification(1, 2)
                .preProcessor(new ImagePreProcessingScaler())
                .build();
        DataSet next = iterator.next();
        next.shuffle(888323);
        next.shuffle(3);
        next.shuffle(5);
        System.out.println("Full_dataSet" + next.asList().size());
        SplitTestAndTrain testAndTrain = next.splitTestAndTrain(13000);
        DataSet trainingData = testAndTrain.getTrain();
        System.out.println("trainingData " + trainingData.asList().size());
        DataSet testData = testAndTrain.getTest();
        System.out.println("testData " + testData.asList().size());
        System.out.println("start");
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 13; j++) {
                DataSet range = (DataSet) trainingData.getRange(j * 1000, (j * 1000 + 1000));
                range.shuffle(new Random().nextLong());
                model.fit(range);
                System.out.println("=========================Epoch -> " + i + "___iteratorDataSet ->" + j + "========================");
            }
        }
        System.out.println("end");
        Evaluation eval = new Evaluation(2);
        INDArray output = model.output(testData.getFeatures());
        eval.eval(testData.getLabels(), output);
        System.out.println(eval.accuracy());
        System.out.println(eval.stats());
        //    model.save(new File("./N" + Instant.now() + ".h5"));
    }
}


