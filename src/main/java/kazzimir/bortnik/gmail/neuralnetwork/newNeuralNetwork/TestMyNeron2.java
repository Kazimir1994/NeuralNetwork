package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork;

import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.IOException;

public class TestMyNeron2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        Loader.load(opencv_java.class);
        avutil.av_log_set_level(-1);
        NativeImageLoader nativeImageLoader = new NativeImageLoader(40, 40, 3);
        MultiLayerNetwork model = MultiLayerNetwork.load(new File("N2021-04-05T11:39:52.574Z.h5"), true);
        String path = TestMyNeron2.class.getResource("/s.png").getFile();
        Mat img = Imgcodecs.imread(path);
        INDArray indArray = nativeImageLoader.asMatrix(img);
        INDArray div = indArray.div(255);

        System.out.println(model.output(div));

    }
}
