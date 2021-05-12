package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork;

import org.bytedeco.opencv.opencv_core.Mat;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.records.listener.impl.LogRecordListener;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.IntervalIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndexAll;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;

public class TestMyNeron {
    public static void main(String[] args) throws IOException, InterruptedException {
        MultiLayerNetwork model = MultiLayerNetwork.load(new File("N2021-04-05T11:39:52.574Z.h5"), true);
        model.setListeners(new ScoreIterationListener(1));


        RecordReader recordReader = new ImageRecordReader(40, 40, 3);
        recordReader.initialize(new FileSplit(new File("./newData/n")));

        DataSetIterator iterator = new RecordReaderDataSetIterator.Builder(recordReader, 6492)
                .preProcessor(new ImagePreProcessingScaler())
                .build();
        DataSet next = iterator.next();
        SplitTestAndTrain testAndTrain = next.splitTestAndTrain(6491);
        DataSet train = testAndTrain.getTrain();
        INDArray features = train.getRange(0, 6491).getFeatures();

        NativeImageLoader nativeImageLoader = new NativeImageLoader(40, 40, 3);

        INDArray output = model.output(features);

        var ref = new Object() {
            int i = 0;
        };

        IntStream.range(0, 6491).forEach(value -> {
            INDArray row = output.getRow(value);
            double[] doubles = row.toDoubleVector();
            if (doubles[0] < doubles[1]) {
                ref.i++;
                INDArrayIndex[] INDArray2 = {NDArrayIndex.point(value)};
                INDArray row1 = features.get(INDArray2);
                row1.muli(255);
                Mat mat = nativeImageLoader.asMat(row1);
                imwrite(value + "test.jpg", mat);
            }
        });
        System.out.println(ref.i);
    }
}
