package kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.impl;

import kazzimir.bortnik.gmail.neuralnetwork.openCVNeuralNetwork.background.ManagementBackground;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;


public class ManagementBackgroundImpl implements ManagementBackground {
    private static final ManagementBackgroundImpl MANAGEMENT_BACKGROUND = new ManagementBackgroundImpl();
    private final ScheduledThreadPoolExecutor backgroundScheduledThreadPool;

    public static ManagementBackgroundImpl getInstant() {
        return MANAGEMENT_BACKGROUND;
    }

    private ManagementBackgroundImpl() {
        backgroundScheduledThreadPool = backgroundScheduledThreadPool();
    }

    @Override
    public void run(Runnable runnable) {
        backgroundScheduledThreadPool.execute(runnable);
    }

    public ScheduledThreadPoolExecutor backgroundScheduledThreadPool() {
        ScheduledThreadPoolExecutor handlerForInteractingMVInstancesScheduledPool =
                (ScheduledThreadPoolExecutor) Executors
                        .newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        handlerForInteractingMVInstancesScheduledPool.setRemoveOnCancelPolicy(true);
        return handlerForInteractingMVInstancesScheduledPool;
    }
}
