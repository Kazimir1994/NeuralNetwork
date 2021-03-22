package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.model;

import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.Fragment;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.time.Instant;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Processor {
    void process(Mat workspace, Fragment fragment);

    default void saveImage(Mat workspace, String idSmartBoard, String name, String prefix) {
        Imgcodecs.imwrite("frame/" + idSmartBoard + "/" + name + prefix + "_" + Instant.now() + ".jpg", workspace);
    }

    default void lightControl(Mat workspace, String idSmartBoard, String name, int startPoint, int endPoint) {
        Mat imgHSV = new Mat();
        Imgproc.cvtColor(workspace, imgHSV, Imgproc.COLOR_BGR2HSV);
        IntStream.rangeClosed(startPoint, endPoint)
                .forEach(brightnessLevel -> {
                    Mat imgHSVWithChangedLightLevel = new Mat();
                    Core.add(imgHSV, new Scalar(0, 0, brightnessLevel * 10), imgHSVWithChangedLightLevel);
                    Imgproc.cvtColor(imgHSVWithChangedLightLevel, imgHSVWithChangedLightLevel, Imgproc.COLOR_HSV2BGR);
                    Imgproc.cvtColor(imgHSVWithChangedLightLevel, imgHSVWithChangedLightLevel, Imgproc.COLOR_BGR2GRAY);
                    saveImage(imgHSVWithChangedLightLevel, idSmartBoard, name, "_brightnessLevel:" + brightnessLevel);
                    imgHSVWithChangedLightLevel.release();
                });
        imgHSV.release();
        workspace.release();
    }
}
