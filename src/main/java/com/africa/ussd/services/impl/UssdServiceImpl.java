package com.africa.ussd.services.impl;

import com.africa.ussd.dtos.UssdAccountOpeningRequest;
import com.africa.ussd.dtos.UssdRequest;
import com.africa.ussd.models.AppUser;
import com.africa.ussd.models.Transaction;
import com.africa.ussd.repositories.AppUserRepository;
import com.africa.ussd.repositories.TransactionRepository;
import com.africa.ussd.services.UssdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class UssdServiceImpl implements UssdService {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private static final String CHECK_BALANCE_CODE = "*123*1#";
    private static final String TRANSFER_FUNDS_CODE = "*123*2#";
    private static final String WITHDRAW_FUNDS_CODE = "*123*3#";
    private static final String OPEN_ACCOUNT_CODE = "*123*4#";
    private static final String DEPOSIT_FUNDS_CODE = "*123*5#";

//    @Override
//    public String processUssdRequest(String sessionId, String serviceCode, UssdRequest ussdRequest, String text) {
//        String[] userInputs = text.split("\\*");
//
//        switch (userInputs[0]) {
//            case "1":
//                return checkBalance(ussdRequest.getPhoneNumber());
//            case "2":
//                return transferFunds(phoneNumber, userInputs);
//            case "3":
//                return withdrawFunds(phoneNumber, userInputs);
//            case "4":
//                return openAccount(phoneNumber);
//            default:
//                return "Welcome to Fintech USSD Service. Please select an option:\n1. Check Balance\n2. Transfer Funds\n3. Withdraw Funds\n4. Open Account";
//        }
//    }

    @Override
    public String checkBalance(UssdRequest ussdRequest) {
        if (!ussdRequest.getServiceCode().equals(CHECK_BALANCE_CODE)) {
            return "Invalid service code for balance check.";
        }
        return handleBalance(ussdRequest.getPhoneNumber());
    }
    public String handleBalance(String phoneNumber){
        AppUser user = appUserRepository.findByPhoneNumber(phoneNumber).orElse(null);
        if (user == null) {
            return "User not found. Please open an account first.";
        }
        return "Your balance is " + user.getBalance().toString();
    }

    @Override
    public String withdrawFunds(UssdRequest ussdRequest) {
        if (!ussdRequest.getServiceCode().equals(WITHDRAW_FUNDS_CODE)) {
            return "Invalid service code for funds withdrawal.";
        }
        return handleWithdraw(ussdRequest.getPhoneNumber(), ussdRequest.getText().split("\\*"));
    }
        private String handleWithdraw (String phoneNumber, String[]inputs){
            if (inputs.length < 2) {
                return "Invalid input for funds withdrawal.";
            }

            BigDecimal amount;
            try {
                amount = new BigDecimal(inputs[1]);
            } catch (NumberFormatException e) {
                return "Invalid amount for withdrawal. Please enter a valid amount.";
            }

            AppUser user = appUserRepository.findByPhoneNumber(phoneNumber).orElse(null);
            if (user == null) {
                return "User not found. Please open an account first";
            }

            if (user.getBalance().compareTo(amount) < 0) {
                return "Insufficient balance.";
            }

            user.setBalance(user.getBalance().subtract(amount));
            appUserRepository.save(user);

            Transaction transaction = new Transaction();
            transaction.setAppUser(user);
            transaction.setAmount(amount);
            transaction.setType("WITHDRAWAL");
            transaction.setTimestamp(LocalDateTime.now());
            transactionRepository.save(transaction);

            return "Funds withdrawal successful. Your new balance is " + user.getBalance().toString() + ".";
        }

    @Override
    public String openAccount(UssdAccountOpeningRequest request) {
        if (!request.getServiceCode().equals(OPEN_ACCOUNT_CODE)) {
            return "Invalid service code for account opening.";
        }
        return handleAccountOpening(request);
    }
    private String handleAccountOpening(UssdAccountOpeningRequest request){
        if (appUserRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            return "Account already exists.";
        }

        AppUser user = new AppUser();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setBalance(BigDecimal.ZERO);
        user.setAccountName(request.getLastName() + " " + request.getFirstName());

        appUserRepository.save(user);

        return "Account opening successful. Welcome!";
    }

    @Override
    public String depositFunds(UssdRequest ussdRequest) {
        if (!ussdRequest.getServiceCode().equals(DEPOSIT_FUNDS_CODE)) {
            return "Invalid service code for funds deposit.";
        }
        return handleDeposit(ussdRequest.getPhoneNumber(), ussdRequest.getText().split("\\*"));
    }
    private String handleDeposit(String phoneNumber, String[] inputs) {
        if (inputs.length < 2) {
            return "Invalid input for funds deposit.";
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(inputs[1]);
        } catch (NumberFormatException e) {
            return "Invalid amount for deposit.";
        }

        AppUser user = appUserRepository.findByPhoneNumber(phoneNumber).orElse(null);
        if (user == null) {
            return "User not found.";
        }

        user.setBalance(user.getBalance().add(amount));
        appUserRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setAppUser(user);
        transaction.setAmount(amount);
        transaction.setType("DEPOSIT");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);


        return "Funds deposit successful. Your new balance is " + user.getBalance().toString() + ".";
    }

    @Override
    public String transferFunds(UssdRequest ussdRequest) {
        if (!ussdRequest.getServiceCode().equals(TRANSFER_FUNDS_CODE)) {
            return "Invalid service code for funds transfer.";
        }
        return handleTransfer(ussdRequest.getPhoneNumber(), ussdRequest.getText().split("\\*"));
    }
    private String handleTransfer(String phoneNumber, String[] inputs) {
        if (inputs.length < 3) {
            return "Invalid input for funds transfer.";
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(inputs[1]);
        } catch (NumberFormatException e) {
            return "Invalid amount for transfer.";
        }

        String targetPhoneNumber = inputs[2];

        AppUser sender = appUserRepository.findByPhoneNumber(phoneNumber).orElse(null);
        AppUser receiver = appUserRepository.findByPhoneNumber(targetPhoneNumber).orElse(null);

        if (sender == null || receiver == null) {
            return "Sender or receiver not found.";
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            return "Insufficient balance.";
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));
        appUserRepository.save(sender);
        appUserRepository.save(receiver);

        Transaction transaction = new Transaction();
        transaction.setAppUser(sender);
        transaction.setAmount(amount);
        transaction.setType("TRANSFER");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        return "Funds transfer successful. Your new balance is " + sender.getBalance().toString() + ".";
    }

}
