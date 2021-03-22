package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository;

import com.mongodb.client.MongoCollection;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.EnumTableName;

public interface CollectionRepository {
    <T> MongoCollection<T> getCollection(EnumTableName collectionName);
}
