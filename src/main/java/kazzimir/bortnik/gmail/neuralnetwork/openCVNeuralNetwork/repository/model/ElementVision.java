package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model;

import org.bson.types.ObjectId;

import java.util.List;

public class ElementVision {
    private ObjectId id;
    private ObjectId idSmartBoard;
    private String name;
    private List<Point> points;

    public ObjectId getId() {
        return id;
    }

    public ObjectId getIdSmartBoard() {
        return idSmartBoard;
    }

    public String getName() {
        return name;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setIdSmartBoard(ObjectId idSmartBoard) {
        this.idSmartBoard = idSmartBoard;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Tool{" +
                "id=" + id +
                ", idSmartBoard=" + idSmartBoard +
                ", name='" + name + '\'' +
                ", points=" + points +
                '}';
    }
}
