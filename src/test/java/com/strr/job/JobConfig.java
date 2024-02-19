package com.strr.job;

import com.strr.partition.DataPartitioner;
import com.strr.processor.DataItemProcessor;
import com.strr.reader.impl.DataItemReaderImpl;
import com.strr.writer.DataItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

//@Configuration
public class JobConfig {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public Job job(Step partStep) {
        return new JobBuilder("testJob", jobRepository)
                .start(partStep)
                .build();
    }

    @Bean
    public Step partStep(Step step) {
        return new StepBuilder("testPartStep", jobRepository)
                .allowStartIfComplete(true)
                .partitioner("testPart", new DataPartitioner())
                .step(step)
                .build();
    }

    @Bean
    public Step step() {
        return new StepBuilder("testStep", jobRepository)
                .allowStartIfComplete(true)
                .<Map<String, Object>, Map<String, Object>>chunk(10, transactionManager)
                .reader(new DataItemReaderImpl(null))
                .processor(new DataItemProcessor())
                .writer(new DataItemWriter<>())
                .build();
    }
}
