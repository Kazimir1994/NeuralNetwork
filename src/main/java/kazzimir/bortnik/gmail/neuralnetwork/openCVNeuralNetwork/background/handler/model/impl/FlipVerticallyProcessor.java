package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.model.impl;

import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.model.Processor;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.Fragment;
import org.opencv.core.Core;
import org.opencv.core.Mat;

public class FlipVerticallyProcessor implements Processor {
    @Override
    public void process(Mat workspace, Fragment fragment, String namePage) {
        Mat flipVertically = new Mat();
        Core.flip(workspace, flipVertically, 1);
        lightControl(flipVertically,
                fragment.getIdSmartBoard().toString(),
                fragment.getName(),
                -6, 6, namePage, "1");
    }
}
