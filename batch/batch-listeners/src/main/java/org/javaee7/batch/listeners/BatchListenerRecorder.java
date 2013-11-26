package org.javaee7.batch.listeners;

/**
 * @author Roberto Cortez
 */
public class BatchListenerRecorder {
    public static boolean jobListenerBeforeJob;
    public static boolean jobListenerAfterJob;

    public static boolean stepListenerBeforeStep;
    public static boolean stepListenerAfterStep;

    public static boolean chunkListenerBeforeChunk;
    public static boolean chunkListenerAfterChunk;

    public static boolean readerListenerBeforeRead;
    public static boolean readerListenerAfterRead;
    public static boolean readerListenerOnReadError;

    public static boolean processorListenerBeforeProcess;
    public static boolean processorListenerAfterProcess;
    public static boolean processorListenerOnProcessError;

    public static boolean writerListenerBeforeWrite;
    public static boolean writerListenerAfterWrite;
    public static boolean writerListenerOnWriteError;

    public static boolean isJobListenerExecuted() {
        return jobListenerBeforeJob && jobListenerAfterJob;
    }

    public static boolean isStepListenerExecuted() {
        return stepListenerBeforeStep && stepListenerAfterStep;
    }

    public static boolean isChunkListenerExecuted() {
        return chunkListenerBeforeChunk && chunkListenerAfterChunk;
    }

    public static boolean isReadListenerExecutedWithoutErrors() {
        return readerListenerBeforeRead && readerListenerAfterRead && !readerListenerOnReadError;
    }

    public static boolean isWriteListenerExecutedWithoutErrors() {
        return writerListenerBeforeWrite && writerListenerAfterWrite && !writerListenerOnWriteError;
    }

    public static boolean isProcessListenerExecutedWithoutErrors() {
        return processorListenerBeforeProcess && processorListenerAfterProcess && !processorListenerOnProcessError;
    }
}
