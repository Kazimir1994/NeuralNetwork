package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.app;

import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.NeuralNetwork;
import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model.Image;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {

        Loader.load(opencv_java.class);
        System.out.println("Load data opencv_java");
        searchAndPreprocessingImage();
        // runNeuralNetwork();
    }

    private static void searchAndPreprocessingImage() {
        searchAndPreprocessingImage("./newData/p", "./N_P/p");
        searchAndPreprocessingImage("./newData/n", "./N_P/n");
    }

    private static void runNeuralNetwork() {
        List<Image> imagesPositive = loadImage("./frame2/605373f13f727636dff06c0d/positive/", 1, 0).subList(0, 40400);
        System.out.println("Read positive data size:=" + imagesPositive.size());
        List<Image> imagesNegative = loadImage("./frame2/605373f13f727636dff06c0d/negative/", 0, 1).subList(0, 40400);
        System.out.println("Read negative data size:=" + imagesNegative.size());
        List<Image> association = association(imagesPositive, imagesNegative);
        System.out.println("Association and shuffle data");
        List<Image> data = imageResizingAndConvertMapToArrayDouble(association, 150, 150);
        System.out.println("Resizing and convert Map to array Double image");
        NeuralNetwork neuralNetwork = new NeuralNetwork(22500, 225, 6, 2);

        var ref = new Object() {
            double mse = 0;
            int epoch = 1;
            int previousEpoch = 0;
        };

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            if (ref.previousEpoch != ref.epoch) {
                System.out.println("==========Epoch -> " + ref.epoch + ", Error train-> " + ref.mse + ", time-> " + Instant.now() + "==========");
                ref.previousEpoch = ref.epoch;
            }
        }, 1, 3, TimeUnit.SECONDS);

        do {
            ref.mse = neuralNetwork.MSE(data, 0.001);
            ref.epoch++;
        } while ((ref.mse > 0.001) && (ref.epoch < 30));
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("NeuralNetwork" + Instant.now() + ".dat"))) {
            oos.writeObject(neuralNetwork);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        scheduler.shutdown();
    }

    public static List<Image> imageResizingAndConvertMapToArrayDouble(List<Image> images, int x, int y) {
        return images.stream()
                .peek(image -> {
                    Mat mat = image.getMat();
                    Mat newImageSize = new Mat();
                    Imgproc.resize(mat, newImageSize, new Size(x, y));
                    Imgproc.cvtColor(newImageSize, newImageSize, Imgproc.COLOR_BGR2GRAY);
                    image.setMat(newImageSize);
                    mat.release();
                    double[] inputData = convertMapToArrayDouble2(image.getMat());
                    image.setInputData(inputData);
                })
                .collect(Collectors.toList());
    }

    public static List<Image> loadImage(String path, double... answer) {
        File file = new File(path);
        List<Image> collect = Stream.of(Objects.requireNonNull(file.list()))
                .parallel()
                // .filter(nameFile -> nameFile.matches("^.*_ORIGINAL_.*$"))
                .map(nameFile -> buildImage(file.getAbsolutePath(), nameFile, answer))
                .collect(Collectors.toList());
        Collections.shuffle(collect);
        return collect;
    }


    public static void searchAndPreprocessingImage(String pathSave, String pathSearch) {
        new File(pathSave).mkdirs();
        AtomicInteger i = new AtomicInteger();
        String[] list = new File(pathSearch).list();
        String s = list[0];
        List.of(list)
                .forEach(nameFile -> {
                    try {
                        System.out.println(nameFile);
                        Mat img = Imgcodecs.imread(pathSearch.concat("/").concat(nameFile));
                        Imgcodecs.imwrite(pathSave.concat("/").concat(i.toString()) + ".jpg", img);

                        IntStream.rangeClosed(1, 3).parallel()
                                .forEach(value -> {
                                    Mat rotate = new Mat();
                                    String prefixNameSveFile;
                                    switch (value) {
                                        case 1 -> {
                                            prefixNameSveFile = "ROTATE_90_CLOCKWISE";
                                            Core.rotate(img, rotate, Core.ROTATE_90_CLOCKWISE);
                                        }
                                        case 2 -> {
                                            prefixNameSveFile = "ROTATE_180";
                                            Core.rotate(img, rotate, Core.ROTATE_180);
                                        }
                                        case 3 -> {
                                            prefixNameSveFile = "ROTATE_90_COUNTERCLOCKWISE";
                                            Core.rotate(img, rotate, Core.ROTATE_90_COUNTERCLOCKWISE);
                                        }
                                        default -> throw new IllegalStateException("Unexpected value: " + value);
                                    }
                                    Imgcodecs.imwrite(pathSave.concat("/").concat(i.toString()).concat(prefixNameSveFile) + ".jpg", rotate);
                                });

                        img.release();
                        System.out.println("end");
                    } catch (Exception exception) {
                        System.err.println(exception.getMessage());
                    }
                    i.getAndIncrement();
                });
    }

    public static void lightControl(Mat workspace, int startPoint, int endPoint, String nameFile, String pathSave) {
        Mat imgHSV = new Mat();
        Imgproc.cvtColor(workspace, imgHSV, Imgproc.COLOR_BGR2HSV);
        IntStream.range(startPoint, endPoint)
                .parallel()
                .forEach(brightnessLevel -> {
                    Mat imgHSVWithChangedLightLevel = new Mat();
                    Core.add(imgHSV, new Scalar(0, 0, brightnessLevel * 10), imgHSVWithChangedLightLevel);
                    Imgproc.cvtColor(imgHSVWithChangedLightLevel, imgHSVWithChangedLightLevel, Imgproc.COLOR_HSV2BGR);
                    Imgproc.cvtColor(imgHSVWithChangedLightLevel, imgHSVWithChangedLightLevel, Imgproc.COLOR_BGR2GRAY);
                    Imgcodecs.imwrite(pathSave.concat("/").concat(nameFile).concat(Instant.now().toString()) + ".jpg", imgHSVWithChangedLightLevel);
                    imgHSVWithChangedLightLevel.release();
                });
        imgHSV.release();
        workspace.release();
    }

    public static Image buildImage(String path, String nameFile, double... answer) {
        System.out.println(path.concat("/").concat(nameFile));
        Mat img = Imgcodecs.imread(path.concat("/").concat(nameFile));
        return new Image(nameFile, null, img, answer);
    }

    public static double[] convertMapToArrayDouble(Mat mat) {
        double[] arr = new double[mat.cols() * mat.rows()];
        int index = 0;
        for (int x = 0; x < mat.rows(); x++) {
            for (int y = 0; y < mat.cols(); y++) {
                arr[index] = mat.get(x, y)[0] / 255;
                index++;
            }
        }
        return arr;
    }

    public static double[] convertMapToArrayDouble2(Mat mat) {
        Mat newImageSize = new Mat();
        Core.divide(mat, new Scalar(255), newImageSize, 1, CvType.CV_64FC1);
        double[] arr = new double[newImageSize.cols() * newImageSize.rows()];
        newImageSize.get(0, 0, arr);
        return arr;
    }

    @SafeVarargs
    public static List<Image> association(List<Image>... images) {
        List<Image> collect = Stream.of(images)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        IntStream.range(0, 10).forEach(value -> Collections.shuffle(collect, new Random()));
        return collect;
    }
}

