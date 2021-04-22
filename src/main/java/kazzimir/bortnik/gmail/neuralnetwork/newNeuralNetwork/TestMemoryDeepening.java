package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class TestMemoryDeepening {

    public static void main(String[] args) throws IOException, InterruptedException {
        MultiLayerNetwork model = MultiLayerNetwork.load(new File("N2021-04-05T11:39:52.574Z.h5"), true);
        model.init();
     /*   System.out.println(model.summary());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ModelSerializer.writeModel(model, stream, true);
        byte[] bytes = stream.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        MultiLayerNetwork multiLayerNetwork = ModelSerializer.restoreMultiLayerNetwork(bais, true);
        System.out.println(multiLayerNetwork.summary());*/
    }
}
