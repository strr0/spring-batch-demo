package com.strr.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

/**
 * 写出器
 * @param <T>
 */
public class DataItemWriter<T> implements ItemWriter<T> {
    @Override
    public void write(Chunk<? extends T> chunk) throws Exception {
        //
    }
}