package com.strr.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * SpringBatch配置
 */
@Configuration
public class DataBatchConfig extends DefaultBatchConfiguration {
    @Autowired
    private DataSource dataSource;

    @Override
    protected DataSource getDataSource() {
        if (dataSource instanceof DynamicRoutingDataSource ds) {
            return ds.getDataSource("postgres");
        }
        return dataSource;
    }
}
