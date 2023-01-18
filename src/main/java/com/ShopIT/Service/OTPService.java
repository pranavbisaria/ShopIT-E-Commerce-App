package com.ShopIT.Service;

import com.ShopIT.Config.TwilioConfig;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service @RequiredArgsConstructor
@Slf4j
public class OTPService {
    private final EmailService emailService;
    private final TwilioConfig twilioConfig;
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
    public int OTPRequestThroughNumber(String phoneNumber, String name){
        String number = "+91"+phoneNumber;
        Random rand = new Random();
        int otpCheck = rand.nextInt(899999) + 100000;
        try {
            String myMessage = "Dear " + name + "," +
                    "\nThe One Time Password (OTP) to verify your mobile number is " + otpCheck +
                    "\nThe One Time Password is valid for the next 10 minutes." +
                    "\n(This is an auto generated sms, so please do not reply back.)" +
                    "\nRegards," +
                    "\nTeam ShopIT" +
                    "\nshopitanywhere@gmail.com";
            Message message = Message.creator(new com.twilio.type.PhoneNumber(number), new com.twilio.type.PhoneNumber(twilioConfig.getFromNumber()), myMessage).create();
            System.out.println(message);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return otpCheck;
    }
}