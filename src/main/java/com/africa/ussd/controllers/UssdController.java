package com.africa.ussd.controllers;

import com.africa.ussd.dtos.UssdAccountOpeningRequest;
import com.africa.ussd.dtos.UssdRequest;
import com.africa.ussd.services.UssdService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ussd")
public class UssdController {
    @Autowired
    private UssdService ussdService;

//    @PostMapping
//    public String handleUssdRequest(@RequestParam Map<String, String> params) {
//        String sessionId = params.get("sessionId");
//        String serviceCode = params.get("serviceCode");
//        String phoneNumber = params.get("phoneNumber");
//        String text = params.get("text");
//
//        return ussdService.processUssdRequest(sessionId, serviceCode, phoneNumber, text);
//    }

    @PostMapping("/balance")
    public ResponseEntity<String> checkBalance(@Valid @RequestBody UssdRequest ussdRequest) {
        return ResponseEntity.ok(ussdService.checkBalance(ussdRequest));
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(@Valid @RequestBody UssdRequest ussdRequest) {
        return ResponseEntity.ok(ussdService.transferFunds(ussdRequest));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawFunds(@Valid @RequestBody UssdRequest ussdRequest) {
        return ResponseEntity.ok(ussdService.withdrawFunds(ussdRequest));
    }
    @PostMapping("/open-account")
    public ResponseEntity<String> openAccount(@Valid @RequestBody UssdAccountOpeningRequest ussdRequest) {
        return ResponseEntity.ok(ussdService.openAccount(ussdRequest));
    }
    @PostMapping("/deposit")
    public ResponseEntity<String> depositFunds(@Valid @RequestBody UssdRequest ussdRequest) {
        return ResponseEntity.ok(ussdService.depositFunds(ussdRequest));
    }

}
