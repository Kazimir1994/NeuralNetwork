package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.servise;

import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.ElementVision;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.Fragment;
import org.opencv.core.Mat;

import java.util.List;

public interface MachineVisionUtilService {
    List<Fragment> convertToElementMachineVision(List<ElementVision> elementVisions);

    Mat getWorkspace(Mat fullFrame, int[] minMaxXY);
}
