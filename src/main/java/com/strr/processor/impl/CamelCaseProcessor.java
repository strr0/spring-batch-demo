package com.strr.processor.impl;

import com.strr.processor.DataItemProcessor;
import com.strr.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 下划线转驼峰处理器
 */
public class CamelCaseProcessor implements DataItemProcessor<Map<String, Object>, Map<String, Object>> {
    @Override
    public Map<String, Object> process(Map<String, Object> item) throws Exception {
        if (item == null || item.isEmpty()) {
            return null;
        }
        Map<String, Object> out = new HashMap<>();
        for (Map.Entry<String, Object> entry : item.entrySet()) {
            out.put(StringUtils.toCamelCase(entry.getKey()), entry.getValue());
        }
        return out;
    }
}
