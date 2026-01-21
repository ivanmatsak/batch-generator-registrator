package com.example.registrar.configuration;

import com.example.registrar.model.RawEvent;
import com.example.registrar.model.RegisteredEvent;
import com.example.registrar.repository.RawEventRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.listener.ItemWriteListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.UUID;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class BatchConfig extends DefaultBatchConfiguration {
    private final RawEventRepository rawRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final KafkaTemplate<String, UUID> kafkaTemplate;

    @Bean
    public JpaPagingItemReader<RawEvent> reader(EntityManagerFactory emf) {
        return new JpaPagingItemReaderBuilder<RawEvent>()
                .name("rawEventReader")
                .entityManagerFactory(emf)
                .queryString("SELECT r FROM RawEvent r")
                .pageSize(10)
                .build();
    }

    @Bean
    public ItemProcessor<RawEvent, RegisteredEvent> processor() {
        return raw -> new RegisteredEvent(
                raw.getEventId(),
                raw.getSource(),
                raw.getType(),
                raw.getTimestamp()
        );
    }

    @Bean
    public JpaItemWriter<RegisteredEvent> writer(EntityManagerFactory emf) {
        var writer = new JpaItemWriter<RegisteredEvent>(emf);
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Step processStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JpaPagingItemReader<RawEvent> reader,
            ItemProcessor<RawEvent, RegisteredEvent> processor,
            JpaItemWriter<RegisteredEvent> writer
    ) {
        return new StepBuilder("processStep", jobRepository)
                .<RawEvent, RegisteredEvent>chunk(10)
                .transactionManager(transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(listener())
                .build();
    }

    @Bean
    public Job processJob(JobRepository jobRepository, Step processStep) {
        return new JobBuilder("processEventsJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(processStep)
                .build();
    }

    @Bean
    public ItemWriteListener<RegisteredEvent> listener() {
        return new ItemWriteListener<>() {
            @Override
            public void afterWrite(Chunk<? extends RegisteredEvent> items) {
                for (RegisteredEvent item : items) {
                    kafkaTemplate.send("confirmations-topic", item.getId());
                    log.info("Событие отправлено: {}", item);
                    rawRepository.deleteByEventId(item.getId());
                    log.info("Подтверждение отправлено для события: {}", item.getId());
                }
            }
        };
    }
}