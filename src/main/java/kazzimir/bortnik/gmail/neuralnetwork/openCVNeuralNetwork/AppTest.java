package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork;

import java.util.stream.IntStream;

public class AppTest {
    public static void main(String[] args) {
        IntStream.rangeClosed(-6, 6).forEach(value -> {
            System.out.println(value*10);
        });
    /*    Loader.load(opencv_java.class);
        avutil.av_log_set_level(-1);
        String path = AppTest.class.getResource("/1.jpg").getFile();
        Mat img = Imgcodecs.imread(path);
        if (img.empty()) {
            System.out.println("Не удалось загрузить изображение");
            return;
        }
        List<Mat> channels = new ArrayList<>();
        Core.split(img, channels);
        Mat img2 = new Mat();
        Core.flip(img, img2, 0);
        Mat img3 = new Mat();
        Core.flip(img, img3, 1);
        Mat img4 = new Mat();
        Core.flip(img, img4, -1);
        Imgcodecs.imwrite("frame.jpg", img);
        Imgcodecs.imwrite("frame0.jpg", img2);
        Imgcodecs.imwrite("frame1.jpg", img3);
        Imgcodecs.imwrite("frame-1.jpg", img4);*/
    /*    ArrayList<Mat> listH = new ArrayList<Mat>();
        listH.add(img);
        listH.add(img3);
        ArrayList<Mat> listV = new ArrayList<Mat>();
        listV.add(img);
        listV.add(img2);
        Mat imgH = new Mat();
        Core.hconcat(listH, imgH);
        Mat imgV = new Mat();
        Core.vconcat(listV, imgV);
        Imgcodecs.imwrite("imgH.jpg", imgH);
        Imgcodecs.imwrite("imgV-1.jpg", imgV);
        Mat imgRepeat = new Mat();
        Core.repeat(img, 13, 1, imgRepeat);
        Imgcodecs.imwrite("imgRepeat.jpg", imgRepeat);

        Mat img2Size = new Mat();
        Imgproc.resize(img, img2Size, new Size(300, 300));
        Imgcodecs.imwrite("img2Size.jpg", img2Size);

        Mat imgHSV = new Mat();
        Imgproc.cvtColor(img, imgHSV, Imgproc.COLOR_BGR2HSV);
        Core.add(imgHSV, new Scalar(0, 0, 60), imgHSV);
        Imgproc.cvtColor(imgHSV, img, Imgproc.COLOR_HSV2BGR);
        Imgcodecs.imwrite("imgHSV.jpg", img);*/
    }
}
