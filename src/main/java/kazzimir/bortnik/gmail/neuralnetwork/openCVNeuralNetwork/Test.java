package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork;


import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        Loader.load(opencv_java.class);
        avutil.av_log_set_level(-1);

        String path = Test.class.getResource("/full.jpg").getFile();
        Mat img = Imgcodecs.imread(path);
        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        Polygon polygon1 = new Polygon();
        polygon1.addPoint(635, 353);
        polygon1.addPoint(723, 351);
        polygon1.addPoint(705, 508);
        polygon1.addPoint(635, 545);
        polygon1.addPoint(625, 484);
        polygon1.addPoint(686, 471);
        polygon1.addPoint(688, 386);
        polygon1.addPoint(625, 383);

        Polygon polygon2 = new Polygon();
        polygon2.addPoint(635, 395);
        polygon2.addPoint(680, 395);
        polygon2.addPoint(680, 458);
        polygon2.addPoint(623, 454);

        Polygon polygon3 = new Polygon();
        polygon3.addPoint(657, 544);
        polygon3.addPoint(718, 510);
        polygon3.addPoint(728, 360);
        polygon3.addPoint(768, 358);
        polygon3.addPoint(753, 518);
        polygon3.addPoint(679, 557);
        polygon3.addPoint(657, 544);

        List<Polygon> collisions = List.of(polygon1,polygon2);
        System.out.println(collisions);
        Mat mask2 = getMask(polygon3, collisions);
        Mat mat2 = getMat(img, polygon3);
        mat2.setTo(new Scalar(255, 255, 255), mask2);
        Imgcodecs.imwrite("mat.jpg", mat2);
    }
/*    Mat mask2 = getMask(polygon1);
    Mat mat2 = getMat(img, polygon1);
        mat2.setTo(new Scalar(255, 255, 255), mask2);*/

    /* Imgcodecs.imwrite("mat.jpg", mat);*/
/*        Mat mask = getMask2(img, polygon1);
        Mat mat = getMat(img, polygon1);
        long l2 = System.nanoTime();
        Core.addWeighted(mat, 1, mask, 1, 1, mat);
        long l3 = System.nanoTime();
        System.out.println(l3 - l2);*/
    /*    Imgcodecs.imwrite("mask.jpg", mask);
        Imgcodecs.imwrite("mat.jpg", mat);*/

    public static Mat getMask(Polygon mainPolygon, List<Polygon> collisions) {
        Mat submat = new Mat((int) mainPolygon.getBounds2D().getHeight(), (int) mainPolygon.getBounds2D().getWidth(), CvType.CV_8UC1);
        for (int x = 0, r = submat.cols(); x < r; x++) {
            for (int y = 0, c = submat.rows(); y < c; y++) {
                int xx = (int) mainPolygon.getBounds2D().getMinX() + x;
                int yy = (int) mainPolygon.getBounds2D().getMinY() + y;
                if (collisions.stream().anyMatch(polygon -> polygon.contains(xx, yy))) {
                    submat.put(y, x, 1);
                } else {
                    submat.put(y, x, 0);
                }
            }
        }
        return submat;
    }

/*    public static Mat getMask2(Mat matBlackWhit, Polygon polygon) {
        Mat submat = getMat(matBlackWhit, polygon);
        for (int x = 0, r = submat.cols(); x < r; x++) {
            for (int y = 0, c = submat.rows(); y < c; y++) {
                int xx = (int) polygon.getBounds2D().getMinX() + x;
                int yy = (int) polygon.getBounds2D().getMinY() + y;
                if (!polygon.contains(xx, yy)) {
                    submat.put(y, x, 255, 255, 255);
                } else {
                    submat.put(y, x, 0, 0, 0);
                }
            }
        }
        return submat;
    }*/

    private static Mat getMat(Mat matBlackWhit, Polygon polygon) {
        return matBlackWhit.submat((int) polygon.getBounds2D().getMinY(),
                (int) polygon.getBounds2D().getMaxY(), (int) polygon.getBounds2D().getMinX(), (int) polygon.getBounds2D().getMaxX()).clone();
    }

}

