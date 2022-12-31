package com.ShopIT.Config;

import com.twilio.Twilio;
import com.twilio.exception.AuthenticationException;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioInitializer {
    private final TwilioConfig twilioConfig;
    public TwilioInitializer(TwilioConfig twilioConfig) throws AuthenticationException {
        this.twilioConfig = twilioConfig;
        try {
            Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Twilio initilaized with account from number:\t"+ twilioConfig.getFromNumber());
    }
}
