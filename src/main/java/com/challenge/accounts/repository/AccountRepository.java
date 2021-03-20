package com.challenge.accounts.repository;

import com.challenge.accounts.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account getAccountByAccountName(String accountName);

    @Modifying(clearAutomatically = true)
    @Query("update Account a set a.accountName =:newAccountName where a.accountName =:oldAccountName")
    int updateAccountName(@Param("newAccountName") String newAccountName, @Param("oldAccountName") String oldAccountName);
}
