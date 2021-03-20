package com.challenge.accounts.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ACCOUNT")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNT_ID_GENERATOR")
    @SequenceGenerator(name = "ACCOUNT_ID_GENERATOR", allocationSize = 1)
    @Column(name = "ACCOUNT_ID")
    private Long id;

    @Column(name = "ACCOUNT_NAME", unique = true, nullable = false)
    private String accountName;

    @CreationTimestamp
    @Column(name = "ACCOUNT_CREATED_AT", updatable = false, nullable = false)
    private Instant accountCreatedAt;

    @CreationTimestamp
    @Column(name = "ACCOUNT_LAST_UPDATED_AT", updatable = false, nullable = false)
    private Instant accountLastUpdatedAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
    @Builder.Default
    @ToString.Exclude
    private List<AccountEvent> accountEvents = new ArrayList<>();

    @PrePersist
    private void prePersist() {
        Instant now = Instant.now();
        this.accountCreatedAt = now;
        this.accountLastUpdatedAt = now;
    }

    @PreUpdate
    private void preUpdate() {
        this.accountLastUpdatedAt = Instant.now();
    }

}
