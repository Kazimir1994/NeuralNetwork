package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.model;

import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.exception.FFmpegFrameGrabberRuntime;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.Fragment;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.servise.MachineVisionUtilService;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.servise.impl.MachineVisionUtilServiceImpl;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.time.Instant;
import java.util.List;

public class GrabberFrame implements Runnable {
    private final MachineVisionUtilService machineVisionUtilService = MachineVisionUtilServiceImpl.getInstant();
    private FFmpegFrameGrabber fFmpegFrameGrabber;
    public final OpenCVFrameConverter.ToOrgOpenCvCoreMat converterMap = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();
    private final DataConnect dataConnect;
    private final List<Fragment> fragments;
    private final String idSmartBoard;

    public GrabberFrame(String idSmartBoard, DataConnect dataConnect, List<Fragment> fragments) throws FFmpegFrameGrabber.Exception {
        this.dataConnect = dataConnect;
        this.fragments = fragments;
        this.idSmartBoard = idSmartBoard;
    }

    @Override
    public void run() {
        configuration();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                captureFramesAndPars();
            } catch (FrameGrabber.Exception e) {
                Thread.currentThread().interrupt();
                System.err.println(e.getMessage());
                break;
            }
        }
    }

    private void configuration() {
        new File("frame/" + idSmartBoard + "/").mkdirs();
        try {
            this.fFmpegFrameGrabber = buildFFmpegFrameGrabber(dataConnect);
        } catch (FFmpegFrameGrabber.Exception e) {
            System.err.println(e.getMessage());
            throw new FFmpegFrameGrabberRuntime(e);
        }
    }

    private FFmpegFrameGrabber buildFFmpegFrameGrabber(DataConnect dataConnect) throws FFmpegFrameGrabber.Exception {
        String url = String.format("rtsp://%s:%s@%s:%s/Streaming/Channels/101",
                dataConnect.getLogin(), dataConnect.getPassword(), dataConnect.getIp(), dataConnect.getPort());
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(url);
        grabber.setOption("rtsp_transport", "tcp");
        grabber.start();
        System.out.println("FFmpegFrameGrabber start");
        return grabber;
    }

    private void captureFramesAndPars() throws FrameGrabber.Exception {
        Frame grab = fFmpegFrameGrabber.grabImage();
        if (grab != null) {
            System.out.println(grab);
            parseFrame(grab);
        } else {
            reconnect();
        }
    }

    private void reconnect() throws FrameGrabber.Exception {
        System.err.println("An error occurred, make reconnection");
        fFmpegFrameGrabber.close();
        fFmpegFrameGrabber = buildFFmpegFrameGrabber(dataConnect);
        System.out.println("Reconnection successful");
    }

    private void parseFrame(Frame grab) {
        Mat fullMap = converterMap.convert(grab);
        fragments.stream()
                .parallel()
                .forEach(fragment -> saveFragment(fullMap, fragment));
        fullMap.release();
    }

    private void saveFragment(Mat mat, Fragment fragment) {
        Mat workspace = machineVisionUtilService.getWorkspace(mat, fragment.getMinMaxXY());
        Mat image = processingMap(workspace);
        Imgcodecs.imwrite("frame/" + idSmartBoard + "/" + fragment.getName() + Instant.now() + ".jpg", image);
        image.release();
    }

    private Mat processingMap(Mat original) {
        Mat blackWhiteMat = new Mat();
        Imgproc.cvtColor(original, blackWhiteMat, Imgproc.COLOR_BGR2GRAY);
        original.release();
        return blackWhiteMat;
    }

    @Override
    public String toString() {
        return "GrabberFrame{" +
                "dataConnect=" + dataConnect +
                ", fragments=" + fragments +
                ", idSmartBoard='" + idSmartBoard + '\'' +
                '}';
    }
}
