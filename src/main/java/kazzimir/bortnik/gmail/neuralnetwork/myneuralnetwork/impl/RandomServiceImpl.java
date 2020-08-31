package kazzimir.bortnik.gmail.neuralnetwork.myneuralnetwork.impl;

import kazzimir.bortnik.gmail.neuralnetwork.myneuralnetwork.RandomService;

import java.util.Random;

public class RandomServiceImpl implements RandomService {
    private static volatile RandomServiceImpl randomService;
    private final Random random = new Random();

    public synchronized static RandomServiceImpl getInstants() {
        if (randomService == null) {
            randomService = new RandomServiceImpl();
        }
        return randomService;
    }

    private RandomServiceImpl() {
    }

    @Override
    public double getRandomDouble(double minValue, double maxValue) {
        return minValue + random.nextDouble() * maxValue;
    }
}
