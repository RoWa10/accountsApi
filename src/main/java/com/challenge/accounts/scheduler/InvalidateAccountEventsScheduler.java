package com.challenge.accounts.scheduler;

import com.challenge.accounts.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class InvalidateAccountEventsScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(InvalidateAccountEventsScheduler.class);

    private AccountService accountService;

    @Autowired
    InvalidateAccountEventsScheduler(AccountService accountService) {
        this.accountService = accountService;
    }

   @Scheduled(cron = "${invalidateEventsSchedulerCronExpression}")
    public void archiveOldEvents() {
       Instant invalidateEventsBefore= Instant.now().minus(30, ChronoUnit.DAYS);
        Integer invalidatedEventsCount = accountService.invalidateOlderEvents(invalidateEventsBefore);
        LOG.info("Total of {} events invalidated", invalidatedEventsCount);
    }

}
