package com.example.shardedsagawallet.controllers;
import com.example.shardedsagawallet.entities.Transaction;
import com.example.shardedsagawallet.entities.User;
import com.example.shardedsagawallet.entities.Wallet;
import com.example.shardedsagawallet.repositories.UserRepository;
import com.example.shardedsagawallet.services.UserService;
import com.example.shardedsagawallet.services.WalletService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.shardedsagawallet.dtos.TransactionResponseDTO;
import com.example.shardedsagawallet.dtos.TransferRequestDTO;
import com.example.shardedsagawallet.dtos.TransferResponseDTO;
import com.example.shardedsagawallet.services.TransactionService;
import com.example.shardedsagawallet.services.TransferSagaService;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/transactions")
@Tag(name = "Transaction API", description = " create transaction, get all transactions" )
public class TransactionController {

    @Autowired
    private TransferSagaService transferSagaService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping
    public ResponseEntity<TransferResponseDTO> createTransaction(@RequestBody TransferRequestDTO transferRequestDTO) {

       try {
            System.out.println("Transfer request received: " + transferRequestDTO);
            System.out.println("From wallet: " + transferRequestDTO.getFromWalletId());
            System.out.println("To wallet: " + transferRequestDTO.getToWalletId());
            System.out.println("Amount: " + transferRequestDTO.getAmount());

            Long sagaInstanceId = transferSagaService.initiateTransfer(
                transferRequestDTO.getFromWalletId(), 
                transferRequestDTO.getToWalletId(), 
                transferRequestDTO.getAmount(), 
                transferRequestDTO.getDescription());

            return ResponseEntity.status(HttpStatus.CREATED).body(
                TransferResponseDTO.builder()
                    .sagaInstanceId(sagaInstanceId)
                    .build()
            );
       } catch (Exception e) {
            System.out.println("Transfer failed with error: " + e.getMessage());
            e.printStackTrace();
            log.error("Error creating transaction", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
       }
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        try {
            Authentication authentication = SecurityContextHolder
                    .getContext().getAuthentication();
            String username = authentication.getName();
            System.out.println("Getting transactions for user: " + username);

            User user = userRepository.findByName(username);
            if (user == null) {
                System.out.println("User not found: " + username);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Long userId = user.getId();
            log.info("User ID: {}", userId);
            String userName = userService.getUserById(userId).getName();
            log.info("User name: {}", userName);

            List<Wallet> userWallets = walletService.getWalletsByUserId(userId);
            System.out.println("Found " + userWallets.size() + " wallets for user");

            List<Transaction> allTransactions = new ArrayList<>();

            for (Wallet wallet : userWallets) {
                List<Transaction> walletTransactions = transactionService.getTransactionByWalletId(wallet.getId());
                System.out.println("Found " + walletTransactions.size() + " transactions for wallet " + wallet.getId());
                allTransactions.addAll(walletTransactions);
            }

            // Convert Transaction entities to TransactionResponseDTO with username
            List<TransactionResponseDTO> transactionResponseDTOs = allTransactions.stream()
                .map(transaction -> {
                    // Get the recipient's username
                    String recipientUsername = "";
                    try {
                        Wallet toWallet = walletService.getWalletById(transaction.getToWalletId());
                        if (toWallet != null) {
                            User toUser = userService.getUserById(toWallet.getUserId());
                            if (toUser != null) {
                                recipientUsername = toUser.getName();
                            }
                        }
                    } catch (Exception e) {
                        log.error("Error getting recipient username", e);
                    }

                    // Determine transaction type based on user perspective
                    com.example.shardedsagawallet.entities.TransactionType transactionType;
                    boolean isUserSender = userWallets.stream().anyMatch(wallet -> wallet.getId().equals(transaction.getFromWalletId()));
                    
                    if (isUserSender) {
                        transactionType = com.example.shardedsagawallet.entities.TransactionType.TRANSFER;
                    } else {
                        transactionType = com.example.shardedsagawallet.entities.TransactionType.RECEIVED;
                    }

                    return TransactionResponseDTO.builder()
                        .id(transaction.getId())
                        .userName(userName)
                        .fromWalletId(transaction.getFromWalletId())
                        .toWalletId(transaction.getToWalletId())
                        .toUsername(recipientUsername)
                        .amount(transaction.getAmount())
                        .status(transaction.getStatus())
                        .type(transactionType)
                        .description(transaction.getDescription())
                        .sagaInstanceId(transaction.getSagaInstanceId())
                        .build();
                })
                .collect(java.util.stream.Collectors.toList());

            System.out.println("Returning " + transactionResponseDTOs.size() + " transactions");
            return ResponseEntity.ok(transactionResponseDTOs);
        } catch (Exception e) {
            System.out.println("Error getting transactions: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
