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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class app3 {
    public static void main(String[] args) {

        Loader.load(opencv_java.class);
        System.out.println("Load data opencv_java");
        List<Image> imagesPositive = loadImage("./frame2/605373f13f727636dff06c0d/positive/", 1, 0).subList(0, 400);

        System.out.println("Read positive data size:=" + imagesPositive.size());
        List<Image> imagesNegative = loadImage("./frame2/605373f13f727636dff06c0d/negative/", 0, 1).subList(0, 400);

        System.out.println("Read negative data size:=" + imagesNegative.size());
        List<Image> association = association(imagesPositive, imagesNegative);
        System.out.println("Association and shuffle data");
        List<Image> data = imageResizingAndConvertMapToArrayDouble(association, 150, 150);
        System.out.println("Resizing and convert Map to array Double image");
        NeuralNetwork neuralNetwork = new NeuralNetwork(22500, 225, 6, 2);
        data.forEach(image -> {
            Imgcodecs.imwrite(image.getName().concat(Instant.now().toString()) + ".jpg", image.getMat());
        });
        var ref = new Object() {
            double mse = 0;
            int epoch = 1;
        };
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
            System.out.println("==========Epoch -> " + ref.epoch + ", Error train-> " + ref.mse + "==========");
        }, 1, 5, TimeUnit.SECONDS);

        //scheduler.shutdown();
        do {
            ref.mse = neuralNetwork.MSE(data, 0.1);
            ref.epoch++;
        } while (ref.mse > 0.01);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("NeuralNetwork" + Instant.now() + ".dat"))) {
            oos.writeObject(neuralNetwork);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        data.forEach(image -> {
            double[] doubles = neuralNetwork.calculateOutput(image.getInputData());
            System.out.println(Arrays.toString(doubles) + "  " + Arrays.toString(image.getAnswer()));
        });
        scheduledFuture.cancel(true);
    }

    private static List<Image> imageResizingAndConvertMapToArrayDouble(List<Image> images, int x, int y) {
        return images.stream()
                .parallel()
                .peek(image -> {
                    Mat mat = image.getMat();
                    Mat newImageSize = new Mat();
                    Imgproc.resize(mat, newImageSize, new Size(x, y));
                    Imgproc.cvtColor(newImageSize, newImageSize, Imgproc.COLOR_BGR2GRAY);
                    image.setMat(newImageSize);
                    mat.release();

                    double[] inputData = convertMapToArrayDouble(image.getMat());
                    image.setInputData(inputData);
                })
                .collect(Collectors.toList());
    }

    private static List<Image> loadImage(String path, double... answer) {
        File file = new File(path);
        List<Image> collect = Stream.of(Objects.requireNonNull(file.list()))
                .parallel()
                .filter(nameFile -> nameFile.matches("^.*_ORIGINAL_.*$"))
                .map(nameFile -> buildImage(file.getAbsolutePath(), nameFile, answer))
                .collect(Collectors.toList());
        Collections.shuffle(collect, new Random(55));
        return collect;
    }

    private static void search(String pathSave, String pathSearch) {
        new File(pathSave).mkdirs();
        Stream.of(Objects.requireNonNull(new File(pathSearch).list()))
                .filter(nameFile -> nameFile.matches("^.*scissors_ORIGINAL_.*$"))
                .forEach(nameFile -> {
                    Mat img = Imgcodecs.imread(pathSearch.concat("/").concat(nameFile));
                    Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
                    Imgcodecs.imwrite(pathSave.concat("/").concat(nameFile).concat(Instant.now().toString()) + ".jpg", img);
                });
    }

    private static Image buildImage(String path, String nameFile, double... answer) {
        String name = nameFile.substring(0, nameFile.indexOf("_"));
   /*     int i = nameFile.indexOf("{");
        String name = nameFile.substring(0, nameFile.indexOf("_"));
        String parameter = nameFile.substring(i + 1, i + 2);*/
        Mat img = Imgcodecs.imread(path.concat("/").concat(nameFile));
        return new Image(name, null, img, answer);
    }

    private static double[] convertMapToArrayDouble(Mat mat) {
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

    private static double[] convertMapToArrayDouble2(Mat mat) {
        Mat newImageSize = new Mat();
        Core.divide(mat, new Scalar(255), newImageSize, 1, CvType.CV_64FC1);
        double[] arr = new double[newImageSize.cols() * newImageSize.rows()];
        newImageSize.get(0, 0, arr);
        return arr;
    }

    @SafeVarargs
    private static List<Image> association(List<Image>... images) {
        List<Image> collect = Stream.of(images)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        IntStream.range(0, 10).forEach(value -> Collections.shuffle(collect, new Random()));
        return collect;
    }
}

