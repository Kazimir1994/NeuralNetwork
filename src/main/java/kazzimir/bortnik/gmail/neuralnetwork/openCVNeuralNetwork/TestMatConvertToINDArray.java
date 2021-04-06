package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork;

import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_java;
import org.datavec.image.loader.NativeImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class TestMatConvertToINDArray {
    private static NativeImageLoader nativeImageLoader = new NativeImageLoader();
    private static OpenCVFrameConverter.ToOrgOpenCvCoreMat converter = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();

    public static void main(String[] args) throws IOException, InterruptedException {
        Loader.load(opencv_java.class);
        avutil.av_log_set_level(-1);
        String path = TestMatConvertToINDArray.class.getResource("/1044.jpg").getFile();
        Mat img = Imgcodecs.imread(path);
         Imgproc.resize(img, img, new Size(40, 40));
        test(img);
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
        return indArray.divi(255);
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
