package com.ShopIT.Config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "twilio")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwilioConfig {
    private String accountSid;
    private String authToken;
    private String fromNumber;
}
