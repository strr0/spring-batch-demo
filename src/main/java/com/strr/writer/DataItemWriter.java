package com.strr.writer;

import org.springframework.batch.item.ItemWriter;

/**
 * 写出器
 * @param <T>
 */
public interface DataItemWriter<T> extends ItemWriter<T> {
}