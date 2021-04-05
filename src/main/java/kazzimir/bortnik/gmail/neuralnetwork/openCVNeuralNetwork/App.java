package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork;

import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.servise.GrabberFrameService;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.servise.impl.GrabberFrameServiceImpl;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;

public class App {
    public static void main(String[] args) {
        Loader.load(opencv_java.class);
        avutil.av_log_set_level(-1);
        GrabberFrameService grabberFrameService = GrabberFrameServiceImpl.getInstant();
        grabberFrameService.createAndRunGrabberFrame("605373f13f727636dff06c0d");
        //grabberFrameService.createAndRunGrabberFrame("5ffebfc975969426f85324ff");
    }
}
