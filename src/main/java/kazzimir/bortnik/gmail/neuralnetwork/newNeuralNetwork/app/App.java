package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.app;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.List;
import java.util.stream.IntStream;

public class App {
    public static void main(String[] args) {

        Loader.load(opencv_java.class);
        System.out.println("Load data opencv_java");
        searchAndPreprocessingImage();
    }

    private static void searchAndPreprocessingImage() {
         searchAndPreprocessingImage("./newData2/p", "./N_P_2/p");
        searchAndPreprocessingImage("./newData2/n", "./N_P_2/n");
    }


    public static void searchAndPreprocessingImage(String pathSave, String pathSearch) {
        new File(pathSave).mkdirs();
        String[] list = new File(pathSearch).list();
        List.of(list)
                .stream()
                .forEach(nameFile -> {
                    try {
                        System.out.println(nameFile);
                        Mat img = Imgcodecs.imread(pathSearch.concat("/").concat(nameFile));
                        IntStream.rangeClosed(5, 6)
                                .forEach(value -> {
                                    Mat rotate = new Mat();
                                    String prefixNameSveFile;
                                    switch (value) {
                                        case 5 -> {
                                            prefixNameSveFile = "F0__".concat(String.valueOf(System.nanoTime()));
                                            Core.flip(img, rotate, 0);
                                            extracted(pathSave, img, rotate, prefixNameSveFile);
                                        }
                                        case 6 -> {
                                            prefixNameSveFile = "O__".concat(String.valueOf(System.nanoTime()));
                                            extracted(pathSave, img, img, prefixNameSveFile);

                                        }
                                        default -> throw new IllegalStateException("Unexpected value: " + value);
                                    }
                                });

                        img.release();
                        System.out.println("end");
                    } catch (Exception exception) {
                        System.err.println(exception.getMessage());
                    }
                });
    }

    private static void extracted(String pathSave, Mat img, Mat rotate, String prefixNameSveFile) {
        IntStream.rangeClosed(0, 5)
                .forEach(angle -> {
                    Mat rotate2 = new Mat();
                    Mat M = Imgproc.getRotationMatrix2D(
                            new Point(img.width() / 2, img.height() / 2), angle * 72, 1);
                    Imgproc.warpAffine(rotate, rotate2, M, new Size(img.width(), img.height()),
                            Imgproc.INTER_LINEAR, Core.BORDER_CONSTANT,
                            new Scalar(0, 0, 0, 255));
                    lightControl(rotate2, prefixNameSveFile.concat(String.valueOf(angle)), pathSave.concat("/"));
                    rotate2.release();
                });
    }

    public static void lightControl(Mat workspace, String nameFile, String pathSave) {
        Mat imgHSV = new Mat();
        Imgproc.cvtColor(workspace, imgHSV, Imgproc.COLOR_BGR2HSV);
        IntStream.range(0, 5)
                .forEach(brightnessLevel -> {
                    IntStream.range(-5, 5)
                            .forEach(v -> {
                                Mat imgHSVWithChangedLightLevel = new Mat();
                                Core.add(imgHSV, new Scalar(brightnessLevel * 72, 0, v * 10), imgHSVWithChangedLightLevel);
                                Imgproc.cvtColor(imgHSVWithChangedLightLevel, imgHSVWithChangedLightLevel, Imgproc.COLOR_HSV2BGR);
                                Imgcodecs.imwrite(pathSave.concat("/")
                                        .concat(nameFile)
                                        .concat(String.valueOf(brightnessLevel))
                                        .concat(String.valueOf(v)).concat(String.valueOf(System.nanoTime()))
                                        + ".jpg", imgHSVWithChangedLightLevel);
                                imgHSVWithChangedLightLevel.release();
                            });
                });
        imgHSV.release();
    }

}

