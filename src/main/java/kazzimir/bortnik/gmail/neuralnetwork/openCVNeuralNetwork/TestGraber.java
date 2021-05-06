package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork;

import org.bytedeco.ffmpeg.avcodec.AVCodec;
import org.bytedeco.ffmpeg.avcodec.AVCodecContext;
import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.avformat.AVStream;
import org.bytedeco.ffmpeg.avutil.AVFrame;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.Mat;
import org.bytedeco.opencv.opencv_java;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;


import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_alloc_context3;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_decode_video2;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_find_decoder;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_receive_frame;
import static org.bytedeco.ffmpeg.global.avcodec.avcodec_send_packet;
import static org.bytedeco.ffmpeg.global.avutil.AVMEDIA_TYPE_VIDEO;
import static org.bytedeco.ffmpeg.global.avutil.av_frame_alloc;
import static org.bytedeco.javacv.OpenCVFrameConverter.getMatDepth;
import static org.bytedeco.opencv.global.opencv_core.CV_MAKETYPE;

public class TestGraber {
    //rtsp://admin:BDC123bdc!@192.168.142.194:554/Streaming/Channels/101
    private static OpenCVFrameConverter.ToOrgOpenCvCoreMat converter2 = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();

    public static void main(String[] args) throws FFmpegFrameGrabber.Exception {
        Loader.load(opencv_java.class);
        avutil.av_log_set_level(-1);
         AVFrame frm = av_frame_alloc();
        FFmpegFrameGrabber fFmpegFrameGrabber = buildFFmpegFrameGrabber("admin", "BDC123bdc!", "192.168.142.194", "554");
        AVPacket avPacket = fFmpegFrameGrabber.grabPacket();

    }
    /*     CanvasFrame frame = new CanvasFrame("Some Title", CanvasFrame.getDefaultGamma() / fFmpegFrameGrabber.getGamma());

         while (true) {
             Frame frame1 = fFmpegFrameGrabber.grabImage();
             frame.showImage(frame1);
         }*/
/*        AVPacket pkt = fFmpegFrameGrabber.grabPacket();
        AVCodec avCodec = avcodec_find_decoder(AV_CODEC_ID_H264);
        AVCodecContext codec_ctx = avcodec_alloc_context3(avCodec);
        avcodec_send_packet(codec_ctx, pkt);
        avcodec_receive_frame(codec_ctx, frm);
        frm.*/
    private static FFmpegFrameGrabber buildFFmpegFrameGrabber(String log, String ps, String ip, String port) throws FFmpegFrameGrabber.Exception {
        String url = String.format("rtsp://%s:%s@%s:%s/Streaming/Channels/101", log, ps, ip, port);
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(url);
        grabber.setOption("rtsp_transport", "tcp");
        grabber.start();
        System.out.println("FFmpegFrameGrabber start");
        return grabber;
    }

    private static String getUrl(String log, String ps, String ip, String port) {
        return String.format("rtsp://%s:%s@%s:%s/Streaming/Channels/101", log, ps, ip, port);
    }

    public static Mat convert(Frame frame) {
        int depth = getMatDepth(frame.imageDepth);
        return depth < 0 ? null : new Mat(frame.imageHeight,
                frame.imageWidth,
                CV_MAKETYPE(depth, frame.imageChannels),
                (ByteBuffer) frame.image[0],
                frame.imageStride * Math.abs(frame.imageDepth) / 8
        );
    }
}
