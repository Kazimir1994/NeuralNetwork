package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork;

import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;

public class AppTest {
    public static void main(String[] args) {
        Loader.load(opencv_java.class);
        avutil.av_log_set_level(-1);
        String path = AppTest.class.getResource("/1.jpg").getPath();
        System.out.println(path);
        Mat img = Imgcodecs.imread(path);
        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        ArrayList<Mat> list = new ArrayList<>();
        Core.split(img, list);
        System.out.println(list.size());
    }
}
