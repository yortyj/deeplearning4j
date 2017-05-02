package org.deeplearning4j.datasets.iterator;

import lombok.NonNull;
import org.deeplearning4j.datasets.iterator.callbacks.FileCallback;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple iterator working with list of files.
 * File -> DataSet conversion will be handled via provided FileCallback implementation
 *
 * @author raver119@gmail.com
 */
public class FileSplitDataSetIterator implements DataSetIterator {
    private DataSetPreProcessor preProcessor;

    private List<File> files;
    private int numFiles;
    private AtomicInteger counter = new AtomicInteger(0);
    private FileCallback callback;

    public FileSplitDataSetIterator(@NonNull List<File> files, @NonNull FileCallback callback) {
        this.files = files;
        this.numFiles = files.size();
        this.callback = callback;
    }


    @Override
    public DataSet next(int num) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int totalExamples() {
        return numFiles;
    }

    @Override
    public int inputColumns() {
        return 0;
    }

    @Override
    public int totalOutcomes() {
        return 0;
    }

    @Override
    public boolean resetSupported() {
        return true;
    }

    @Override
    public boolean asyncSupported() {
        return true;
    }

    @Override
    public void reset() {
        counter.set(0);
    }

    @Override
    public int batch() {
        return 0;
    }

    @Override
    public int cursor() {
        return counter.get();
    }

    @Override
    public int numExamples() {
        return numFiles;
    }

    @Override
    public void setPreProcessor(DataSetPreProcessor preProcessor) {
        this.preProcessor = preProcessor;
    }

    @Override
    public DataSetPreProcessor getPreProcessor() {
        return preProcessor;
    }

    @Override
    public List<String> getLabels() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return counter.get() < numFiles;
    }

    @Override
    public DataSet next() {
        DataSet ds = callback.call(files.get(counter.getAndIncrement()));

        if (preProcessor != null && ds != null)
            preProcessor.preProcess(ds);

        return ds;
    }

    @Override
    public void remove() {
        // no-op
    }
}
