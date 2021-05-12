package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.app;

import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.NeuralNetwork;
import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model.Image;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class NeuralNetworkLoad {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Loader.load(opencv_java.class);
        System.out.println("Load data opencv_java");
     //   List<Image> imagesPositive = loadImage("./frame2/5ffebfc975969426f85324ff/positive/", 1, 0);
      //  System.out.println("Read positive data size:=" + imagesPositive.size());
/*        List<Image> imagesNegative = loadImage("./frame2/605373f13f727636dff06c0d/negative/", 0, 1).subList(4000, 4020);
        System.out.println("Read negative data size:=" + imagesNegative.size());*/
      //  List<Image> association = association(imagesPositive/*, imagesNegative*/);
        System.out.println("Association and shuffle data");
    //    List<Image> data = imageResizingAndConvertMapToArrayDouble(association, 150, 150);
        NeuralNetwork neuralNetwork;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("NeuralNetwork2021-03-25T15:46:05.152906084Z.dat"))) {
            System.out.println("Load NeuralNetwork");
            neuralNetwork = (NeuralNetwork) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }
        // here we use a little forbidden magic due to the fact that the derivative activation fields are not serialized
        NeuralNetwork finalNeuralNetwork = neuralNetwork;
        UnaryOperator<Double> activation = x -> 1 / (1 + Math.exp(-1 * x));
        UnaryOperator<Double> derivative = y -> y * (1 - y);
        neuralNetwork.activation = activation;
        neuralNetwork.derivative = derivative;
    /*    System.out.println("Load successfully");
        data.forEach(image -> {
            System.out.println(image.getName());
            double[] doubles = finalNeuralNetwork.calculateOutput(image.getInputData());
            System.out.println(Arrays.toString(doubles) + "  " + Arrays.toString(image.getAnswer()));
            System.out.println("___________________________________________________________________");
        });*/
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
