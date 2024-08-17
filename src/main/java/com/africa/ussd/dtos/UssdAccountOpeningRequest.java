package com.africa.ussd.dtos;

import lombok.Data;

@Data
public class UssdAccountOpeningRequest {
    private String sessionId;
    private String serviceCode;
    private String phoneNumber;
    private String lastName;
    private String firstName;
    private String text;

}
