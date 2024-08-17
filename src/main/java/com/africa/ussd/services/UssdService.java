package com.africa.ussd.services;

import com.africa.ussd.dtos.UssdAccountOpeningRequest;
import com.africa.ussd.dtos.UssdRequest;

public interface UssdService {
   // String processUssdRequest(String sessionId, String serviceCode, String phoneNumber, String text);

    String checkBalance(UssdRequest ussdRequest);
    String withdrawFunds(UssdRequest ussdRequest);
    String openAccount(UssdAccountOpeningRequest request);
    String depositFunds(UssdRequest ussdRequest);
    String transferFunds(UssdRequest ussdRequest);


}
