package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.app;

import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.NeuralNetwork;
import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model.Image;
import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model.Layer;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {
        NeuralNetwork neuralNetwork = new NeuralNetwork(4, 1, 3, 2);
        double[] input = new double[]{0.1, 0.5, 0.2, 0.9};
        double[] output = new double[]{1,0};
        IntStream.range(0, 1000)
                .forEach(value -> {
                    neuralNetwork.trainNetwork(input, output, 0.1);
                });
        double[] doubles = neuralNetwork.calculateOutput(input);
        System.out.println(Arrays.toString(doubles));

   /*     Loader.load(opencv_java.class);
        System.out.println("Load data opencv_java");
        List<Image> imagesPositive = loadImage("./frame/605373f13f727636dff06c0d/Tool/", 1).subList(0, 1000);
        System.out.println("Read positive data size");
        List<Image> imagesNegative = loadImage("./frame/605373f13f727636dff06c0d/EmptyTool/", 0).subList(0, 1000);
        System.out.println("Read negative data size");
        List<Image> association = association(imagesPositive, imagesNegative);
        System.out.println("Association and shuffle data");
        List<Image> data = imageResizingAndConvertMapToArrayDouble(association, 50, 50);
        System.out.println("Resizing and convert Map to array Double image");*/
/*
        Layer layer_1 = new Layer(0, 2500);
        Layer layer_2 = new Layer(2500, 1000);
        Layer layer_3 = new Layer(1000, 800);
        Layer layer_4 = new Layer(800, 300);
        Layer layer_5 = new Layer(300, 100);
        Layer layer_6 = new Layer(100, 50);
        Layer layer_7 = new Layer(50, 1);
        NeuralNetwork neuralNetwork = new NeuralNetwork(layer_1, layer_2, layer_3, layer_4, layer_5, layer_6, layer_7);

        AtomicReference<Double> error = new AtomicReference<>((double) 0);
        int epoch = 1;
        do {
            error.set((double) 0);
            data.forEach(image -> {
                double[] doubles = neuralNetwork.feedForward(image.getInputData());
                System.out.printf("---Epoch -> %d, Main answer -> %d, Current answer -> %f %n---", epoch, image.getAnswer(), doubles[0]);
                double backpropagation = neuralNetwork.backpropagation(image.getAnswer());
                error.updateAndGet(v -> v + Math.abs(backpropagation));
            });
            System.out.printf("==========Epoch -> %d, Error -> %f %n==========", epoch, error.get());
            error.set(error.get() + 1);
        } while (error.get() > 0.01);*/
        //      for (int i = 0; i < inputParameters.length; i++) {
        //     double[] doubles = neuralNetwork.feedForward(inputParameters[i]);
        //     System.out.println(Arrays.toString(inputParameters[i]) + " -> " + answers[i] + "| actual->" + Arrays.toString(doubles));
        // }
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
                })
                .peek(image -> {
                    double[] inputData = convertMapToArrayDouble2(image.getMat());
                    image.setInputData(inputData);
                })
                .collect(Collectors.toList());
    }

    private static List<Image> loadImage(String path, int answer) {
        File file = new File(path);
        List<Image> collect = Stream.of(Objects.requireNonNull(file.list()))
                .parallel()
                .filter(nameFile -> !nameFile.matches("^.*_ORIGINAL_.*$"))
                .map(nameFile -> buildImage(file.getAbsolutePath(), nameFile, answer))
                .collect(Collectors.toList());
        Collections.shuffle(collect, new Random(55));
        return collect;
    }

    private static Image buildImage(String path, String nameFile, int answer) {
        int i = nameFile.indexOf("{");
        String name = nameFile.substring(0, nameFile.indexOf("{"));
        String parameter = nameFile.substring(i + 1, i + 2);
        Mat img = Imgcodecs.imread(path.concat("/").concat(nameFile));
        return new Image(name, parameter, img, answer);
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

