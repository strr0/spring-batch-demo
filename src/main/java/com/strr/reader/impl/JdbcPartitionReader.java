package com.strr.reader.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 读取器
 */
public class JdbcPartitionReader extends AbstractItemCountingItemStreamItemReader<Map<String, Object>> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcPartitionReader.class);
    private final Object lock = new Object();
    private DataSource dataSource;
    private String script;
    private ExecutionContext executionContext;
    protected volatile List<Map<String, Object>> results;

    public JdbcPartitionReader() {
        this.setName(ClassUtils.getShortName(JdbcPartitionReader.class));
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @Override
    protected Map<String, Object> doRead() throws Exception {
        synchronized (this.lock) {
            if (results == null || results.isEmpty()) {
                return null;
            }
            return results.remove(results.size() - 1);
        }
    }

    @Override
    protected void doOpen() throws Exception {
        logger.info("DataItemReaderImpl.doOpen invoke");
        fetchData();
    }

    private void fetchData() {
        if (dataSource == null || script == null) {
            return;
        }
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        results = jdbcTemplate.queryForList(script);
    }

    public long getCount() {
        if (dataSource == null || script == null) {
            return 0L;
        }
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.queryForObject(String.format("select count(1) from (%s) t", script), Long.class);
    }

    @Override
    protected void doClose() throws Exception {
        logger.info("DataItemReaderImpl.doClose invoke");
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        super.open(executionContext);
        this.executionContext = executionContext;
    }
}
