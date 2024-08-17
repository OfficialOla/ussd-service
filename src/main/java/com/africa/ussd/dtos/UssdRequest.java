package com.africa.ussd.dtos;

import lombok.Data;

//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
public class UssdRequest {
    @NotBlank(message = "Session ID is mandatory")
    private String sessionId;

    @NotBlank(message = "Service Code is mandatory")
    private String serviceCode;

    @NotBlank(message = "Phone Number is mandatory")
    @Pattern(regexp = "^\\d{10,15}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotBlank(message = "Text is mandatory")
    private String text;

}
