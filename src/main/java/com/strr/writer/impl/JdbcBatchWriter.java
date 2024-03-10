package com.strr.writer.impl;

import com.strr.writer.DataItemWriter;
import org.springframework.batch.item.Chunk;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 写出器
 */
public class JdbcBatchWriter implements DataItemWriter<Map<String, Object>> {
    private DataSource dataSource;
    private String script;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @Override
    public void write(Chunk<? extends Map<String, Object>> chunk) throws Exception {
        if (dataSource == null || script == null) {
            return;
        }
        List<? extends Map<String, Object>> items = chunk.getItems();
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        for (Map<String, Object> item : items) {
            jdbcTemplate.update(script, item);
        }
    }
}
