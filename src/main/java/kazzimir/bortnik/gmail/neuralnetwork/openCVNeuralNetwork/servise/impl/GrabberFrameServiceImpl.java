package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.servise.impl;

import com.mongodb.client.MongoCollection;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.ManagementBackground;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.impl.ManagementBackgroundImpl;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.model.DataConnect;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.model.GrabberFrame;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.exception.FFmpegFrameGrabberRuntime;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.exception.SmartBoardNotFundRuntime;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.CollectionRepository;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.impl.CollectionRepositoryImpl;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.EnumTableName;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.Fragment;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.SmartBoard;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.ElementVision;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.servise.GrabberFrameService;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.servise.MachineVisionUtilService;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bytedeco.javacv.FFmpegFrameGrabber;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class GrabberFrameServiceImpl implements GrabberFrameService {
    private static final GrabberFrameServiceImpl GRABBER_FRAME_SERVICE = new GrabberFrameServiceImpl();
    private final CollectionRepository collectionRepository = CollectionRepositoryImpl.getInstance();
    private final ManagementBackground managementBackground = ManagementBackgroundImpl.getInstant();
    private final MachineVisionUtilService machineVisionUtilService = MachineVisionUtilServiceImpl.getInstant();

    public static GrabberFrameServiceImpl getInstant() {
        return GRABBER_FRAME_SERVICE;
    }

    private GrabberFrameServiceImpl() {
    }

    @Override
    public void createAndRunGrabberFrame(String idSmartBoard) {
        GrabberFrame grabberFrame = getSmartBoardById(idSmartBoard)
                .map(this::buildDataConnect)
                .map(dataConnect -> getGrabberFrame(idSmartBoard, dataConnect))
                .orElseThrow(() -> new SmartBoardNotFundRuntime("board not found by this ID->" + idSmartBoard));
        System.out.println("Create grabber_frame -> " + grabberFrame);
        managementBackground.run(grabberFrame);
        System.out.println("Launch successful -> " + grabberFrame);
    }

    private Optional<SmartBoard> getSmartBoardById(String idSmartBoar) {
        MongoCollection<SmartBoard> collectionSmartBoard = collectionRepository.getCollection(EnumTableName.SMART_BOARD);
        SmartBoard smartBoard = collectionSmartBoard.find(eq("_id", new ObjectId(idSmartBoar))).first();
        return Optional.ofNullable(smartBoard);
    }

    private DataConnect buildDataConnect(SmartBoard smartBoard) {
        return new DataConnect(smartBoard.getLogin(), smartBoard.getPassword(), smartBoard.getIp(), smartBoard.getPort());
    }

    private GrabberFrame getGrabberFrame(String idSmartBoard, DataConnect dataConnect) {
        List<ElementVision> elementVisionsTools = getToolsByIdSmartBoard(idSmartBoard);
        List<Fragment> fragmentTools = machineVisionUtilService.convertToElementMachineVision(elementVisionsTools);

        List<ElementVision> borderByIdSmartBoard = getBorderByIdSmartBoard(idSmartBoard);
        List<Fragment> fragmentsBorder = machineVisionUtilService.convertToElementMachineVision(borderByIdSmartBoard);
        try {
            return new GrabberFrame(idSmartBoard, dataConnect, fragmentTools, fragmentsBorder);
        } catch (FFmpegFrameGrabber.Exception e) {
            throw new FFmpegFrameGrabberRuntime(e);
        }
    }

    private List<ElementVision> getToolsByIdSmartBoard(String idSmartBoard) {
        MongoCollection<ElementVision> collectionTool = collectionRepository.getCollection(EnumTableName.TOOL);
        return collectionTool.find(new Document("idSmartBoard", new ObjectId(idSmartBoard))).into(new ArrayList<>());
    }

    private List<ElementVision> getBorderByIdSmartBoard(String idSmartBoard) {
        MongoCollection<ElementVision> collectionTool = collectionRepository.getCollection(EnumTableName.BORDER);
        return collectionTool.find(new Document("idSmartBoard", new ObjectId(idSmartBoard))).into(new ArrayList<>());
    }
}
