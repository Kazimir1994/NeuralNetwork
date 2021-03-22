package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.servise.impl;

import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.ElementVision;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.Fragment;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.Point;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.servise.MachineVisionUtilService;
import org.opencv.core.Mat;

import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MachineVisionUtilServiceImpl implements MachineVisionUtilService {
    private static final MachineVisionUtilServiceImpl MACHINE_VISION_UTIL_SERVICE = new MachineVisionUtilServiceImpl();

    private MachineVisionUtilServiceImpl() {
    }

    public static MachineVisionUtilServiceImpl getInstant() {
        return MACHINE_VISION_UTIL_SERVICE;
    }

    public List<Fragment> convertToElementMachineVision(List<ElementVision> elementVisions) {
        return elementVisions.stream()
                .map(this::buildFragment)
                .collect(Collectors.toList());
    }

    public Mat getWorkspace(Mat fullFrame, int[] minMaxXY) {
        return fullFrame.submat(minMaxXY[0], minMaxXY[1], minMaxXY[2], minMaxXY[3]);
    }

    private Fragment buildFragment(ElementVision elementVision) {
        int[] ints = minMaxXY(elementVision.getPoints());
        Polygon polygon = buildPolygon(elementVision.getPoints());
        return new Fragment(elementVision.getId(),
                elementVision.getIdSmartBoard(),
                elementVision.getName(),
                polygon,
                ints);
    }

    public Polygon buildPolygon(List<Point> points) {
        Polygon poly = new Polygon();
        points.forEach(point -> poly.addPoint(point.getX(), point.getY()));
        return poly;
    }

    private int[] minMaxXY(List<Point> points) {
        return new int[]{
                Collections.min(points, Comparator.comparing(Point::getY)).getY(),
                Collections.max(points, Comparator.comparing(Point::getY)).getY(),
                Collections.min(points, Comparator.comparing(Point::getX)).getX(),
                Collections.max(points, Comparator.comparing(Point::getX)).getX()
        };
    }
}
