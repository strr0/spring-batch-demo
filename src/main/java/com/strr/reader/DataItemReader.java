package com.strr.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.util.ClassUtils;

import java.util.List;

/**
 * 读取器
 * @param <T>
 */
public abstract class DataItemReader<T> extends AbstractItemCountingItemStreamItemReader<T> {
    private static final Logger logger = LoggerFactory.getLogger(DataItemReader.class);
    private ExecutionContext executionContext;
    protected volatile List<T> results;
    private final Object lock = new Object();

    public DataItemReader() {
        this.setName(ClassUtils.getShortName(DataItemReader.class));
    }

    public ExecutionContext getExecutionContext() {
        return executionContext;
    }

    protected abstract void fetchData();

    @Override
    protected T doRead() throws Exception {
        synchronized (this.lock) {
            if (results == null || results.isEmpty()) {
                return null;
            }
            return results.remove(results.size() - 1);
        }
    }

    @Override
    protected void doOpen() throws Exception {
        fetchData();
        logger.info("DataItemReader doOpen");
    }

    @Override
    protected void doClose() throws Exception {
        logger.info("DataItemReader doClose");
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        super.open(executionContext);
        this.executionContext = executionContext;
    }
}