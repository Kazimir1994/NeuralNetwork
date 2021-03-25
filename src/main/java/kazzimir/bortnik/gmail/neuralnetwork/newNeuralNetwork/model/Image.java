package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model;

import org.opencv.core.Mat;

public class Image {
    private Mat mat;
    private final String name;
    private final String parameter;
    private final double[] answer;
    private double[] inputData;

    public Image(String name, String parameter, Mat mat, double... answer) {
        this.name = name;
        this.parameter = parameter;
        this.mat = mat;
        this.answer = answer;
    }

    public Mat getMat() {
        return mat;
    }

    public String getName() {
        return name;
    }

    public String getParameter() {
        return parameter;
    }

    public void setMat(Mat mat) {
        this.mat = mat;
    }

    public double[] getAnswer() {
        return answer;
    }

    public double[] getInputData() {
        return inputData;
    }

    public void setInputData(double[] inputData) {
        this.inputData = inputData;
    }

    @Override
    public String toString() {
        return "Image{" +
                "mat=" + mat +
                ", name='" + name + '\'' +
                ", parameter='" + parameter + '\'' +
                '}';
    }
}
