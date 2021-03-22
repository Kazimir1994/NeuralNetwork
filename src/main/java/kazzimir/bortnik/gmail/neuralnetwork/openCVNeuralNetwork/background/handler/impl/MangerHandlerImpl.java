package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.impl;

import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.ManagerHandler;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.model.Processor;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.model.impl.FlipHorizontallyProcessor;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.model.impl.FlipHorizontallyRotateProcessor;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.model.impl.FlipVerticallyProcessor;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.model.impl.ProcessorOriginal;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.Fragment;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.servise.MachineVisionUtilService;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.servise.impl.MachineVisionUtilServiceImpl;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class MangerHandlerImpl implements ManagerHandler {
    private static final MangerHandlerImpl MANGER_HANDLER = new MangerHandlerImpl();
    private final MachineVisionUtilService machineVisionUtilService = MachineVisionUtilServiceImpl.getInstant();
    private final List<Processor> processors;

    public static MangerHandlerImpl getInstants() {
        return MANGER_HANDLER;
    }

    private MangerHandlerImpl() {
        this.processors = registrationProcessor();
    }

    @Override
    public void performProcessing(Mat mat, Fragment fragment) {
        System.out.println("Manger_Handler mat ->" + mat + " fragment ->" + fragment);
        Mat workspace = getWorkspace(mat, fragment);
        processors.stream().parallel()
                .forEach(processor -> processor.process(workspace, fragment));
        workspace.release();
    }

    private Mat getWorkspace(Mat mat, Fragment fragment) {
        return machineVisionUtilService.getWorkspace(mat, fragment.getMinMaxXY());
    }

    private List<Processor> registrationProcessor() {
        List<Processor> processors = new ArrayList<>();
        processors.add(new ProcessorOriginal());
        processors.add(new FlipHorizontallyProcessor());
        processors.add(new FlipHorizontallyRotateProcessor());
        processors.add(new FlipVerticallyProcessor());
        return processors;
    }
}
