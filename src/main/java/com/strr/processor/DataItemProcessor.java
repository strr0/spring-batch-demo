package com.strr.processor;

import org.springframework.batch.item.ItemProcessor;

import java.util.Map;

/**
 * 处理器
 */
public class DataItemProcessor implements ItemProcessor<Map<String, Object>, Map<String, Object>> {
    @Override
    public Map<String, Object> process(Map<String, Object> item) throws Exception {
        return item;
    }
}
