package com.ShopIT.Payloads;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class OtpDto {
    @Email
    private String email;
    @Min(value=100000, message="OTP should be 6 digit number")
    @Digits(message="OTP should be 6 digit number", fraction = 0, integer = 6)
    private int one_time_password;
}