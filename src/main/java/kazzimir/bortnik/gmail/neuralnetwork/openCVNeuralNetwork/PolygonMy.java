package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static java.awt.geom.Rectangle2D.intersect;

public class PolygonMy {

    public static void main(String[] args) {
        INDArray indArray = Nd4j.create(3, 3, 40, 40);
        System.out.println(indArray);
        INDArray indArray3 = Nd4j.create(3, 40, 40);
        indArray.put(new INDArrayIndex[]{NDArrayIndex.point(0)}, indArray3);

    }
}