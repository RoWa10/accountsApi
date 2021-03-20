package com.challenge.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResource implements Serializable {
    private String accountIdentifier;
    private String accountName;
    private String accountCreationDate;

}
