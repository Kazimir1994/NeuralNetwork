package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.model;

import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.handler.impl.MangerHandlerImpl;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.exception.FFmpegFrameGrabberRuntime;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.Fragment;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.Mat;

import java.io.File;
import java.util.List;

public class GrabberFrame implements Runnable {
    private final MangerHandlerImpl mangerHandler = MangerHandlerImpl.getInstants();
    public final OpenCVFrameConverter.ToOrgOpenCvCoreMat converterMap = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();
    private FFmpegFrameGrabber fFmpegFrameGrabber;
    private final DataConnect dataConnect;
    private final List<Fragment> fragments;
    private final String idSmartBoard;
    private boolean firstRunFlag = true;

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
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            firstRunFlag = false;
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

    private long startTime = System.currentTimeMillis();

    private void captureFramesAndPars() throws FrameGrabber.Exception {
        Frame grab = fFmpegFrameGrabber.grabImage();
        long endTime = System.currentTimeMillis();
        if (endTime - startTime > 60000 || firstRunFlag) {
            startTime = endTime;
            extracted(grab);
        }
    }

    private void extracted(Frame grab) throws FrameGrabber.Exception {
        if (grab != null) {
            parseFrame(grab);
        } else {
            reconnect();
        }
    }

    private void parseFrame(Frame grab) {
        Mat fullMap = converterMap.convert(grab);
        fragments.stream()
                .parallel()
                .forEach(fragment -> mangerHandler.performProcessing(fullMap, fragment));
        fullMap.release();
    }

    private void reconnect() throws FrameGrabber.Exception {
        System.err.println("An error occurred, make reconnection");
        fFmpegFrameGrabber.close();
        fFmpegFrameGrabber = buildFFmpegFrameGrabber(dataConnect);
        System.out.println("Reconnection successful");
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
