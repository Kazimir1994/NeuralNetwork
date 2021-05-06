package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork;

import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.Frame;
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
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import static org.bytedeco.javacv.OpenCVFrameConverter.getMatDepth;
import static org.bytedeco.opencv.global.opencv_core.CV_MAKETYPE;


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
        int width = 500;
        int height = 500;
        NativeImageLoader nativeImageLoader = new NativeImageLoader(height, width);
        int indexRowStart = 0;
        int indexRowEnd = 110;
        int indexColStart = 0;
        int indexColEnd = 110;
        String path = TestMatConvertToINDArray.class.getResource("/1044.jpg").getFile();
        ImageRecordReader recordReader = new ImageRecordReader(height, width, 3);
        recordReader.initialize(new FileSplit(new File(path)));
        DataSetIterator iterator = new RecordReaderDataSetIterator.Builder(recordReader, 1)
                .build();
        DataSet next = iterator.next();

        INDArray features = next.asList().get(0).getFeatures();
        Frame frame = nativeImageLoader.asFrame(features);
        NativeImageLoader nativeImageLoader2 = new NativeImageLoader(40, 40, 3);
        long l = System.nanoTime();
        INDArray mat = mat(frame, nativeImageLoader2, indexRowStart, indexRowEnd, indexColStart, indexColEnd);
        long l1 = System.nanoTime();
        System.out.println(l1 - l);
        long l2 = System.nanoTime();
        INDArray indArray = iNDArray(frame, nativeImageLoader, indexRowStart, indexRowEnd, indexColStart, indexColEnd, 40, 40);
        long l3 = System.nanoTime();
        System.out.println(l3 - l2);


    }

    private static INDArray mat(Frame frame, NativeImageLoader nativeImageLoader, int indexRowStart, int indexRowEnd, int indexColStart, int indexColEnd) throws IOException {
        Mat convert = converter.convert(frame);
        Mat submat = convert.submat(indexRowStart, indexRowEnd, indexColStart, indexColEnd);
        return nativeImageLoader.asMatrix(submat);
    }

    private static INDArray iNDArray(Frame frame, NativeImageLoader nativeImageLoader, int indexRowStart, int indexRowEnd, int indexColStart, int indexColEnd, int width, int height) throws IOException {
        INDArray features = nativeImageLoader.asMatrix(frame);
        INDArrayIndex[] INDArray = {NDArrayIndex.interval(0, 1)
                , NDArrayIndex.interval(0, 3)
                , NDArrayIndex.interval(indexRowStart, indexRowEnd), NDArrayIndex.interval(indexColStart, indexColEnd)};
        INDArray indArray = features.get(INDArray);
        NativeImageLoader nativeImageLoader2 = new NativeImageLoader(height, width, 3);
        org.bytedeco.opencv.opencv_core.Mat mat = nativeImageLoader2.asMat(indArray);
        return nativeImageLoader2.asMatrix(mat);
    }

    public static Mat convert(Frame frame) {
        int depth = getMatDepth(frame.imageDepth);
        return depth < 0 ? null : new org.opencv.core.Mat(frame.imageHeight,
                frame.imageWidth,
                CV_MAKETYPE(depth, frame.imageChannels),
                (ByteBuffer) frame.image[0],
                frame.imageStride * Math.abs(frame.imageDepth) / 8
        );
    }

//.divi(255);

    private static INDArray resizeImage(int width, int height, INDArray image) throws IOException {
        NativeImageLoader nativeImageLoader2 = new NativeImageLoader(height, width, 3);
        org.bytedeco.opencv.opencv_core.Mat mat = nativeImageLoader2.asMat(image);
        return nativeImageLoader2.asMatrix(mat);
    }

}
