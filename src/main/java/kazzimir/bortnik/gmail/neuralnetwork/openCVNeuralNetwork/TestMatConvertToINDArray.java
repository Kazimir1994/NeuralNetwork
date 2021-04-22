package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork;

import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_java;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;

import static org.nd4j.linalg.indexing.NDArrayIndex.all;

public class TestMatConvertToINDArray {
    private static NativeImageLoader nativeImageLoader = new NativeImageLoader();
    private static OpenCVFrameConverter.ToOrgOpenCvCoreMat converter = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();

    public static void main(String[] args) throws IOException, InterruptedException {
        Loader.load(opencv_java.class);
        avutil.av_log_set_level(-1);
        est2();
/*
        String path = TestMatConvertToINDArray.class.getResource("/1044.jpg").getFile();
        Mat img = Imgcodecs.imread(path);
        Imgproc.resize(img, img, new Size(40, 40));
        test(img);*/
 /*       // convertMatToArray(img);

        //NativeImageLoader nativeImageLoader = new NativeImageLoader();
      *//*  INDArray indArray = nativeImageLoader.asMatrix(convert);
        System.out.println(indArray);*//*
         *//*       System.out.println(img.width() + "__" + img.height());
        NativeImageLoader nativeImageLoader = new NativeImageLoader();
        INDArray indArray = nativeImageLoader.asMatrix(img);
        org.bytedeco.opencv.opencv_core.Mat mat = new org.bytedeco.opencv.opencv_core.Mat();
        mat.*//*
        RecordReader recordReader = new ImageRecordReader(2, 5, 3);
        recordReader.initialize(new FileSplit(new File(path)));
        DataSetIterator iterator = new RecordReaderDataSetIterator.Builder(recordReader, 1)
                // .preProcessor(new ImagePreProcessingScaler())
                .build();
        DataSet next = iterator.next();
        System.out.println(next.asList().get(0));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        INDArray indArray = Nd4j.create(convertMatToArray(img));
        System.out.println(indArray);*/
    }

    /* public static double[] convertMapToArrayDouble2(Mat mat) {
         Mat newImageSize = new Mat();
         Core.divide(mat, new Scalar(255), newImageSize, 1, CvType.CV_64FC1);
         double[] arr = new double[newImageSize.cols() * newImageSize.rows()];
         newImageSize.get(0, 0, arr);
         return arr;
     }*/
    private static void est2() throws IOException, InterruptedException {
    /*   String path = TestMatConvertToINDArray.class.getResource("/1044.jpg").getFile();
       NativeImageLoader nativeImageLoader = new NativeImageLoader();
       INDArray indArray = nativeImageLoader.asMatrix(path);
       System.out.println(indArray);*/
        int width = 250;
        int height = 250;
        int indexRowStart = 0;
        int indexRowEnd = 2;
        int indexColStart = 0;
        int indexColEnd = 2;
        String path = TestMatConvertToINDArray.class.getResource("/1044.jpg").getFile();
        Mat img = Imgcodecs.imread(path);
        Imgproc.resize(img, img, new Size(width, height));
        RecordReader recordReader = new ImageRecordReader(height, width, 3);
        recordReader.initialize(new FileSplit(new File(path)));
        DataSetIterator iterator = new RecordReaderDataSetIterator.Builder(recordReader, 1)
                .build();
        DataSet next = iterator.next();
        INDArray features = next.asList().get(0).getFeatures();
        System.out.println(features);
        long l = System.nanoTime();
        INDArrayIndex[] INDArray = {NDArrayIndex.interval(0, 1)
                , NDArrayIndex.interval(0, 3)
                , NDArrayIndex.interval(indexRowStart, indexRowEnd), NDArrayIndex.interval(indexColStart, indexColEnd)};
        INDArray indArray = features.get(INDArray);
        long l2 = System.nanoTime();
        System.out.println(l2 - l);
        long l3 = System.nanoTime();
        Mat submat = img.submat(indexRowStart, indexRowEnd, indexColStart, indexColEnd);
        INDArray indArray1 = convertMatToINDArray4(submat);
        System.out.println(indArray1);

        System.out.println("+++++++++++++++++++++++++++++++++++");
/*        INDArray indArray2 = indArray1.put(new INDArrayIndex[]{NDArrayIndex.interval(0, 1)
                , NDArrayIndex.interval(0, 3)
                , NDArrayIndex.interval(indexRowStart, indexRowEnd), NDArrayIndex.interval(indexColStart, indexColEnd)}, 23);
        System.out.println(indArray2);*/
        double[] doubles = {1, 2, 3};
        INDArray indArray2 = indArray1.put(new INDArrayIndex[]{NDArrayIndex.interval(0, 1)
                , NDArrayIndex.interval(0, 3),NDArrayIndex.point(1),NDArrayIndex.point(0)},0);
        System.out.println(indArray2);
/*        long l4 = System.nanoTime();
        System.out.println(l4 - l3);
        System.out.println((l4 - l3) / (l2 - l));*/
    }

    /*        double[][] doubles = {{1, 2, 3}, {4, 5, 6}};
            INDArray indArray1 = Nd4j.create(doubles);
            System.out.println();
            System.out.println(indArray1);
            System.out.println();
            System.out.println(indArray1.get(NDArrayIndex.interval(0, 2), NDArrayIndex.interval(1, 3)));*/
    private static void test(Mat mat) throws IOException {
        Frame frame = converter.convert(mat);
        for (int i = 0; i < 10000; i++) {
            INDArray indArray3 = convertMatToINDArray4(mat);
/*            System.out.println(indArray3);
            System.out.println("+++++++++++++++++++++++++++++++++");*/
            INDArray indArray2 = convertMatToINDArray3(frame);
/*            System.out.println(indArray2);
            System.out.println("+++++++++++++++++++++++++++++++++");*/
            INDArray indArray1 = convertMatToINDArray2(mat);
/*            System.out.println(indArray1);
            System.out.println("+++++++++++++++++++++++++++++++++");*/
            INDArray indArray = convertMatToINDArray(mat);
            // System.out.println(indArray);
        }
        long l = System.nanoTime();
        INDArray indArray3 = convertMatToINDArray4(mat);
        //    System.out.println(indArray3);
        long l1 = System.nanoTime();
        System.out.println(l1 - l);

        long l2 = System.nanoTime();
        INDArray indArray2 = convertMatToINDArray3(frame);
        //    System.out.println(indArray2);
        long l3 = System.nanoTime();
        System.out.println(l3 - l2);

        long l4 = System.nanoTime();
        INDArray indArray1 = convertMatToINDArray2(mat);
        //   System.out.println(indArray1);
        long l5 = System.nanoTime();
        System.out.println(l5 - l4);

        long l6 = System.nanoTime();
        INDArray indArray = convertMatToINDArray(mat);
        //    System.out.println(indArray);
        long l7 = System.nanoTime();
        System.out.println(l7 - l6);
    }

    private static INDArray convertMatToINDArray4(Mat mat) throws IOException {
        INDArray indArray = nativeImageLoader.asMatrix(mat);
        return indArray;//indArray.divi(255);
    }

    private static INDArray convertMatToINDArray3(Frame frame) throws IOException {
        INDArray indArray = nativeImageLoader.asMatrix(frame);
        return indArray.divi(255);
    }

    private static INDArray convertMatToINDArray2(Mat mat) throws IOException {
        Mat newImageSize = new Mat();
        Core.divide(mat, new Scalar(255, 255, 255), newImageSize, 1, CvType.CV_64FC1);
        return nativeImageLoader.asMatrix(newImageSize);
    }

    private static INDArray convertMatToINDArray(Mat mat) {
        Mat newImageSize = new Mat();
        Core.divide(mat, new Scalar(255, 255, 255), newImageSize, 1, CvType.CV_64FC1);
        double[] arr = new double[newImageSize.cols() * newImageSize.rows() * newImageSize.channels()];
        newImageSize.get(0, 0, arr);
        double[][][] doubles = new double[newImageSize.channels()][mat.rows()][mat.cols()];
        int indexBias = mat.cols() * mat.channels();
        for (int row = 0; row < mat.rows(); row++) {
            for (int col = 0; col < mat.cols(); col++) {
                double r = arr[row * indexBias + (3 * col)];
                double g = arr[row * indexBias + (3 * col) + 1];
                double b = arr[row * indexBias + (3 * col) + 2];
                doubles[0][row][col] = r;
                doubles[1][row][col] = g;
                doubles[2][row][col] = b;
            }
        }
        return Nd4j.create(doubles);
    }
}
