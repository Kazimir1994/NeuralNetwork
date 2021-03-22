package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model;

import org.bson.types.ObjectId;

import java.awt.*;
import java.util.Arrays;

public class Fragment {
    private final ObjectId id;
    private final ObjectId idSmartBoard;
    private final String name;
    private final int[] minMaxXY;
    private final Polygon polygon;

    public Fragment(ObjectId id, ObjectId idSmartBoard, String name, Polygon polygon, int[] minMaxXY) {
        this.id = id;
        this.idSmartBoard = idSmartBoard;
        this.name = name;
        this.minMaxXY = minMaxXY;
        this.polygon = polygon;
    }

    public ObjectId getId() {
        return id;
    }

    public ObjectId getIdSmartBoard() {
        return idSmartBoard;
    }

    public String getName() {
        return name;
    }

    public int[] getMinMaxXY() {
        return minMaxXY;
    }

    @Override
    public String toString() {
        return "Fragment{" +
                "id=" + id +
                ", idSmartBoard=" + idSmartBoard +
                ", name='" + name + '\'' +
                ", minMaxXY=" + Arrays.toString(minMaxXY) +
                ", polygon=" + polygon +
                '}';
    }
}
