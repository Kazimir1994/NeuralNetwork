package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.model.impl;

import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.model.Processor;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.Fragment;
import org.opencv.core.Core;
import org.opencv.core.Mat;

public class FlipHorizontallyRotateProcessor implements Processor {
    @Override
    public void process(Mat workspace, Fragment fragment, String namePage) {
        Mat flipHorizontallyRotate = new Mat();
        Core.flip(workspace, flipHorizontallyRotate, -1);
        lightControl(flipHorizontallyRotate, fragment.getIdSmartBoard().toString(), fragment.getName(), -6, 6, "-1_" + namePage,"-1");
    }
}
