package com.strr.partition.impl;

import com.strr.partition.DataPartitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class JdbcRownumPartitioner implements DataPartitioner {
    private Long total = 0L;
    private Long pageSize = 5000L;

    public void setTotal(Long total) {
        this.total = total;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> contextMap = new HashMap<>();
        int idx = 0;
        for (int row = 0; row < total; row += pageSize) {
            ExecutionContext context = new ExecutionContext();
            context.put("startRow", row);
            context.put("pageSize", pageSize);
            contextMap.put(String.valueOf(idx++), context);
        }
        return contextMap;
    }
}
