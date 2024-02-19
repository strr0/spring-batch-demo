package com.strr.reader.impl;

import com.strr.model.DataScript;
import com.strr.reader.DataItemReader;
import org.springframework.batch.item.ExecutionContext;

import java.util.Map;
import java.util.Optional;

/**
 * 读取器
 */
public class DataItemReaderImpl extends DataItemReader<Map<String, Object>> {
    private DataScript script;

    public DataItemReaderImpl(DataScript script) {
        super();
        this.script = script;
    }

    @Override
    protected void fetchData() {
        ExecutionContext context = getExecutionContext();
        Optional.ofNullable(context).ifPresent(ctx -> {
            System.out.printf("index: %s%n", ctx.get("index"));
        });
        this.results = null;
    }
}
