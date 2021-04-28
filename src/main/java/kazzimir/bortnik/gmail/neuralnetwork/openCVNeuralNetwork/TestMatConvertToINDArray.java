package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork;

import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_java;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.BaseImageLoader;
import org.datavec.image.loader.ImageLoader;
import org.datavec.image.loader.Java2DNativeImageLoader;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.ImageTransform;
import org.datavec.image.transform.ResizeImageTransform;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.enums.ImageResizeMethod;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.CustomOp;
import org.nd4j.linalg.api.ops.DynamicCustomOp;
import org.nd4j.linalg.api.ops.impl.image.ImageResize;
import org.nd4j.linalg.api.shape.LongShapeDescriptor;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.CropAndResizeDataSetPreProcessor;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.factory.ops.NDImage;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class TestMatConvertToINDArray {
    private static NativeImageLoader nativeImageLoader = new NativeImageLoader();
    private static OpenCVFrameConverter.ToOrgOpenCvCoreMat converter = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();
    private static OpenCVFrameConverter.ToMat converter2 = new OpenCVFrameConverter.ToMat();

    public static void main(String[] args) throws IOException, InterruptedException {
        Loader.load(opencv_java.class);
        avutil.av_log_set_level(-1);
        est2();
    }

    private static void est2() throws IOException, InterruptedException {
        int width = 5;
        int height = 5;
        int indexRowStart = 0;
        int indexRowEnd = 4;
        int indexColStart = 0;
        int indexColEnd = 4;
        String path = TestMatConvertToINDArray.class.getResource("/1044.jpg").getFile();
        Mat img = Imgcodecs.imread(path);
        Imgproc.resize(img, img, new Size(width, height));
        ImageRecordReader recordReader = new ImageRecordReader(height, width, 3);

        recordReader.initialize(new FileSplit(new File(path)));
        DataSetIterator iterator = new RecordReaderDataSetIterator.Builder(recordReader, 1)
                .build();
        DataSet next = iterator.next();
        INDArray features = next.asList().get(0).getFeatures();

        System.out.println(features);
        //  System.out.println(features);
   /*     INDArrayIndex[] INDArray = {NDArrayIndex.interval(0, 1)
                , NDArrayIndex.interval(0, 3)
                , NDArrayIndex.interval(indexRowStart, indexRowEnd), NDArrayIndex.interval(indexColStart, indexColEnd)};
        INDArray indArray = features.get(INDArray);*/
/*        System.out.println("+++++++++++++++++++++++++++++++++++");
        INDArray indArray2 = indArray.put(new INDArrayIndex[]{NDArrayIndex.interval(0, 1)
                , NDArrayIndex.interval(0, 3), NDArrayIndex.point(1), NDArrayIndex.point(0)}, 0);
        System.out.println(indArray2);*/
        BufferedImage bufferedImage = ImageLoader.toImage(features);
        Image scaledInstance = bufferedImage.getScaledInstance(2, 2, 3);

        Imgproc.resize(img, img, new Size(2, 2));
        System.out.println(img.dump());
        System.out.println("=====================");
        INDArray indArray1 = nativeImageLoader.asMatrix(img);
        System.out.println(indArray1);
        System.out.println("---------------------------");



    }
//.divi(255);

    private static INDArray resizeImage(int width, int height, INDArray image) throws IOException {
        NativeImageLoader nativeImageLoader2 = new NativeImageLoader(height, width, 3);
        nativeImageLoader2.asMatrix()
        org.bytedeco.opencv.opencv_core.Mat mat = nativeImageLoader2.asMat(image);
        return nativeImageLoader2.asMatrix(mat);
    }

}
