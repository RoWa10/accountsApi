package com.challenge.accounts.repository;

import com.challenge.accounts.domain.AccountEvent;
import com.challenge.accounts.dto.AccountStatisticsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AccountEventRepository extends JpaRepository<AccountEvent, Long> {

    @Query("SELECT new com.challenge.accounts.dto.AccountStatisticsResponse(e.accountEventType, e.accountEventHappenedAt, COUNT(e.accountEventType))" +
            " FROM AccountEvent e WHERE e.account.accountName =:accountName GROUP BY e.accountEventHappenedAt, e.accountEventType ORDER BY e.accountEventHappenedAt")
    List<AccountStatisticsResponse> countEventTypesByDay(@Param("accountName") String accountName);

    @Modifying
    @Query("UPDATE AccountEvent e SET e.accountEventExpired = TRUE WHERE e.accountEventCreatedAt >= :validTillInstant AND e.accountEventExpired = FALSE")
    Integer updateAccountEventExpired(@Param("validTillInstant") Instant validTillInstant);
}
