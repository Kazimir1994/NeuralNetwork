package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork;

import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.servise.GrabberFrameService;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.servise.impl.GrabberFrameServiceImpl;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;

import java.util.function.Function;

public class App {
    public static void main(String[] args) {
        Function<String, Integer> f1 = s -> {
            System.out.println(1);
            return 5;
        };
        Function<Integer, String> f2 = i -> {
            System.out.println(2);
            return "3";
        };
        Function<String, String> stringStringFunction = f1.andThen(f2);
        System.out.println(stringStringFunction.apply("aaaaaaaaa"));
      /*  Function<String, String> composeqq = stringStringFunction.andThen(o -> o + "2");
        Function<String, String> objectStringFunction = composeqq.andThen(s -> s + "3");
        System.out.println(objectStringFunction.apply("4"));*/
 /*       Loader.load(opencv_java.class);
        avutil.av_log_set_level(-1);
        GrabberFrameService grabberFrameService = GrabberFrameServiceImpl.getInstant();
        grabberFrameService.createAndRunGrabberFrame("605373f13f727636dff06c0d");*/
        //grabberFrameService.createAndRunGrabberFrame("5ffebfc975969426f85324ff");
    }
}
