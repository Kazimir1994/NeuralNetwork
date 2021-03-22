package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.model.impl;

import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.model.Processor;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.Fragment;
import org.opencv.core.Core;
import org.opencv.core.Mat;

public class FlipHorizontallyProcessor implements Processor {
    @Override
    public void process(Mat workspace, Fragment fragment) {
        Mat flipHorizontally = new Mat();
        Core.flip(workspace, flipHorizontally, 0);
        lightControl(flipHorizontally,
                fragment.getIdSmartBoard().toString(),
                fragment.getName(),
                -6, 6);
    }
}
