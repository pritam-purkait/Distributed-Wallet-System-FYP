package com.example.shardedsagawallet.controllers;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shardedsagawallet.dtos.CreateWalletRequestDTO;
import com.example.shardedsagawallet.dtos.CreditWalletRequestDTO;
import com.example.shardedsagawallet.dtos.DebitWalletRequestDTO;
import com.example.shardedsagawallet.entities.Wallet;
import com.example.shardedsagawallet.services.WalletService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/wallets")
@Slf4j
@Tag(name = "Wallet API", description = " create wallet, get wallet by id, get wallet balance" )
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestBody CreateWalletRequestDTO request) {
        try {
            if (request.getUserId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            try {
                Wallet existingWallet = walletService.getWalletByUserId(request.getUserId());
                if (existingWallet != null) {
                    log.info("Wallet already exists for user {}", request.getUserId());
                    return ResponseEntity.status(HttpStatus.OK).body(existingWallet);
                }
            } catch (Exception e) {

                log.info("No wallet found for user {}, creating new wallet", request.getUserId());
            }

            Wallet newWallet = walletService.createWallet(request.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(newWallet);
        } catch (Exception e) {
            log.error("Error creating wallet", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wallet> getWalletById(@PathVariable Long id) {
        Wallet wallet = walletService.getWalletById(id);
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getWalletBalance(@PathVariable Long id) {
        BigDecimal balance = walletService.getWalletBalance(id);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/{userId}/debit")
    public ResponseEntity<Wallet> debitWallet(@PathVariable Long userId, @RequestBody DebitWalletRequestDTO request) {
        walletService.debit(userId, request.getAmount());
        Wallet wallet = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(wallet);
    }

    @PostMapping("/{userId}/credit")
    public ResponseEntity<Wallet> creditWallet(@PathVariable Long userId, @RequestBody CreditWalletRequestDTO request) {
        walletService.credit(userId, request.getAmount());
        Wallet wallet = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(wallet);
    }


}
