package com.strr.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class JobConfig1 {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private DataSource dataSource;

    @Bean
    public Job pageJob(Step pageStep) throws Exception {
        return new JobBuilder("pageJob", jobRepository)
                .start(pageStep)
                .build();
    }

    @Bean
    public Step pageStep() throws Exception {
        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("id, test_key, value");
        provider.setFromClause("test_demo");
        provider.setSortKeys(new HashMap<>(){{put("id", Order.ASCENDING);}});
        JdbcPagingItemReader reader = new JdbcPagingItemReader<>();
        reader.setName("pageReader");
        reader.setDataSource(dataSource);
        reader.setPageSize(10);
        reader.setQueryProvider(provider);
        reader.setRowMapper(new ColumnMapRowMapper());
        reader.afterPropertiesSet();
        return new StepBuilder("pageStep", jobRepository)
                .allowStartIfComplete(true)
                .chunk(100, transactionManager)
                .reader(reader)
                .writer(chunk -> {
                    chunk.getItems().forEach(System.out::println);
                })
                .build();
    }
}
