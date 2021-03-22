package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler;

import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.Fragment;
import org.opencv.core.Mat;

public interface ManagerHandler {
    void performProcessing(Mat mat, Fragment fragment);
}
