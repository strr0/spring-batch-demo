package com.strr.reader;

import org.springframework.batch.item.ItemReader;

/**
 * 读取器
 * @param <T>
 */
public interface DataItemReader<T> extends ItemReader<T> {
}