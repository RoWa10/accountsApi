package com.challenge.accounts.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "ACCOUNT_EVENT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEvent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNT_EVENT_GENERATOR")
    @SequenceGenerator(name = "ACCOUNT_EVENT_GENERATOR", allocationSize = 1)
    @Column(name = "ACCOUNT_EVENT_ID")
    private Long id;

    @Column(name = "ACCOUNT_EVENT_TYPE")
    private String accountEventType;

    @JoinColumn(name = "ACCOUNT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Column(name = "ACCOUNT_EVENT_HAPPENED_AT", updatable = false, nullable = false)
    private Instant accountEventHappenedAt;

    @CreationTimestamp
    @Column(name = "ACCOUNT_EVENT_CREATED_AT", updatable = false, nullable = false)
    private Instant accountEventCreatedAt;

    @Column(name = "ACCOUNT_EVENT_EXPIRED")
    @Builder.Default
    private Boolean accountEventExpired = Boolean.FALSE;

    @PrePersist
    private void prePersist() {
        this.accountEventCreatedAt = Instant.now();
    }

}

