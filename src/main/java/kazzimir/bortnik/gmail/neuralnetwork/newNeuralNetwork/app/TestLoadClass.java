package kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.app;

import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.NeuralNetwork;
import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model.Processing;
import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model.TestProcessing;
import kazzimir.bortnik.gmail.neuralnetwork.newNeuralNetwork.model.TestProcessing2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;

public class TestLoadClass {
    public static void main(String[] args) {
    /*    TestProcessing testProcessing = new TestProcessing();
        TestProcessing2 testProcessing2 = new TestProcessing2();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Test2" + Instant.now() + ".dat"))) {
            oos.writeObject(testProcessing2);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }*/
        Processing processing;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Test2021-03-28T21:21:08.991965823Z.dat"))) {
            System.out.println("Load NeuralNetwork");
            processing = (Processing) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }
        String run = processing.run();
        System.out.println(run);
    }
}
