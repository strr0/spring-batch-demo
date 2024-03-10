package com.strr.processor;

import org.springframework.batch.item.ItemProcessor;

/**
 * 处理器
 */
public interface DataItemProcessor<I, O> extends ItemProcessor<I, O> {
}
