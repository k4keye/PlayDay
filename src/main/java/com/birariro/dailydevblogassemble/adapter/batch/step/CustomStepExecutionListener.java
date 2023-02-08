package com.birariro.dailydevblogassemble.adapter.batch.step;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

@Slf4j
@StepScope
@Component
public class CustomStepExecutionListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {

        String stepName = stepExecution.getStepName();
        String format = String.format("[batch step start] [%s] start", stepName);
        log.info(format);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        String stepName = stepExecution.getStepName();
        ExitStatus exitStatus = stepExecution.getExitStatus();

        String format = String.format("[batch step end] [%s] %s", stepName, exitStatus.getExitCode());
        if(exitStatus.getExitCode().equals("FAILED")){
            log.error(format);
            //todo 알림
        }else{
            log.info(format);
        }

        return exitStatus;
    }
}