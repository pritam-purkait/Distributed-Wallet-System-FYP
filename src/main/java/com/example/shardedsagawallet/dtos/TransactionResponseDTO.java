package com.example.shardedsagawallet.dtos;

import java.math.BigDecimal;

import com.example.shardedsagawallet.entities.TransactionStatus;
import com.example.shardedsagawallet.entities.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDTO {
    private Long id;
    private String userName;
    private Long fromWalletId;
    private Long toWalletId;
    private String toUsername;
    private BigDecimal amount;
    private TransactionStatus status;
    private TransactionType type;
    private String description;
    private Long sagaInstanceId;
}
