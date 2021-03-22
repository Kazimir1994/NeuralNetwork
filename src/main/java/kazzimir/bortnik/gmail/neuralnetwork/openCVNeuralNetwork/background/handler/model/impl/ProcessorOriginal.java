package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.model.impl;

import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.model.Processor;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.Fragment;
import org.opencv.core.Mat;

public class ProcessorOriginal implements Processor {
    @Override
    public void process(Mat workspace, Fragment fragment) {
        saveImage(workspace, fragment.getIdSmartBoard().toString(), fragment.getName(), "_ORIGINAL");
        lightControl(workspace,
                fragment.getIdSmartBoard().toString(),
                fragment.getName(),
                -6, 6);
    }
}
