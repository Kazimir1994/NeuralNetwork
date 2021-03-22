package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.impl;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.CollectionRepository;
import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.repository.model.EnumTableName;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.List;

public class CollectionRepositoryImpl implements CollectionRepository {
    private static final CollectionRepositoryImpl COLLECTION_REPOSITORY = new CollectionRepositoryImpl();
    private final MongoDatabase databaseMachineVision;

    private CollectionRepositoryImpl() {
        MongoClient mongoClient = mongoClientBuild();
        this.databaseMachineVision = mongoClient.getDatabase("MAKEIT_MACHINE_VISiON");
    }

    public static CollectionRepositoryImpl getInstance() {
        return COLLECTION_REPOSITORY;
    }

    @Override
    public <T> MongoCollection<T> getCollection(EnumTableName collectionName) {
        return (MongoCollection<T>) databaseMachineVision.getCollection(collectionName.getName(), collectionName.getTypeClass());
    }

    private MongoClient mongoClientBuild() {
        List<ServerAddress> seeds = getServerAddresses();
        MongoCredential credential = getMongoCredential();
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        return MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder -> builder.hosts(seeds))
                        .credential(credential)
                        .codecRegistry(codecRegistry)
                        .build()
        );
    }

    private List<ServerAddress> getServerAddresses() {
        List<ServerAddress> seeds = new ArrayList<>();
        seeds.add(new ServerAddress("localhost", 27099));
        return seeds;
    }

    private MongoCredential getMongoCredential() {
        return MongoCredential.createScramSha1Credential(
                "mongoMachineVision",
                "admin",
                "mongoMachineVision".toCharArray()
        );
    }
}
