package com.example.registrar.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobScheduler {

    private final JobOperator jobOperator;
    private final Job processEventsJob;

    @Scheduled(fixedRate = 10000)
    public void run() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobOperator.start(processEventsJob, params);

            log.info("Batch Job успешно запущен");
        } catch (Exception e) {
            log.error("Ошибка запуска: {}", e.getMessage());
        }
    }
}