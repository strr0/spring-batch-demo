package com.strr.job;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.strr.partition.impl.JdbcRownumPartitioner;
import com.strr.processor.impl.CamelCaseProcessor;
import com.strr.reader.impl.JdbcPartitionReader;
import com.strr.writer.impl.JdbcBatchWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
public class JobPartitionConfig {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private DynamicRoutingDataSource dataSource;

    @Bean
    public Job job() {
        return new JobBuilder("testJob", jobRepository)
                .start(createPartStep())
                .build();
    }

    public Step createPartStep() {
        // 读取器
        JdbcPartitionReader reader = new JdbcPartitionReader();
        reader.setDataSource(dataSource.getDataSource("postgres"));
        reader.setScript("select test_key, test_value from test_demo");
        // 处理器
        CamelCaseProcessor processor = new CamelCaseProcessor();
        // 写出器
        JdbcBatchWriter writer = new JdbcBatchWriter();
        writer.setDataSource(dataSource.getDataSource("mysql"));
        writer.setScript("insert into test_demo(test_key, test_value) values(:testKey, :testValue)");
        // 分区器
        JdbcRownumPartitioner partitioner = new JdbcRownumPartitioner();
        partitioner.setTotal(reader.getCount());
        partitioner.setPageSize(10L);
        return new StepBuilder("testPartStep", jobRepository)
                .allowStartIfComplete(true)
                .partitioner("testPart", partitioner)
                .step(createStep(reader, processor, writer))
                .build();
    }

    public Step createStep(ItemReader<Map<String, Object>> reader, ItemProcessor<Map<String, Object>, Map<String, Object>> processor, ItemWriter<Map<String, Object>> writer) {
        return new StepBuilder("testStep", jobRepository)
                .allowStartIfComplete(true)
                .<Map<String, Object>, Map<String, Object>>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
