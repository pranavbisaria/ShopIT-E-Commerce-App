package com.ShopIT.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
@Service @RequiredArgsConstructor
public class OTPService {
    private final EmailService emailService;
    public int OTPRequest(String email){
        Random rand = new Random();
        int otpCheck = rand.nextInt(899999) +100000;
        String subject = "OTP Verification";
        String message = "Dear User," +
                "\nThe One Time Password (OTP) to verify your Email Address is " + otpCheck +
                "\nThe One Time Password is valid for the next 10 minutes."+
                "\n(This is an auto generated email, so please do not reply back.)" +
                "\nRegards," +
                "\nTeam ShopIT";
        String to = email;
        this.emailService.sendEmail(subject, message, to);
        return otpCheck;
    }
    public void SuccessRequest(String email, String name){
        String subject = "Successfully registered on ShopIT";
        String message = "Dear " + name + "," +
                "\nThank you for registering on ShopIT" +
                "\nNow enjoy the lowest price on product that you will never get before and enjoy 24/7 shopping experience."+
                "\nPlace you first order now and enjoy the shopping on ShopIT" +
                "\n(This is an auto generated email, so please do not reply back.)" +
                "\nRegards," +
                "\nTeam ShopIT";
        String to = email;
        this.emailService.sendEmail(subject, message, to);
    }
}