package com.ShopIT.Service;
import com.ShopIT.Payloads.*;
import com.ShopIT.Security.JwtAuthRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> LoginAPI(JwtAuthRequest request);
    ResponseEntity<?> registerEmail(EmailDto emailDto) throws Exception;
    ResponseEntity<?> verifyToRegister(OtpDto otpDto);
    ResponseEntity<?> signupUser(UserDto userDto);
    ResponseEntity<?> registerMerchant(RegisterMerchant registerMerchant) throws Exception;
    ResponseEntity<?> signGoogle(String Token) throws JsonProcessingException, NullPointerException;
    ResponseEntity<?> verifyOTPPasswordChange(OtpDto otpDto);
    void updateUserPass(ForgetPassword password);
    ResponseEntity<?> resetPassword(ForgetPassword forgetPassword);
    ResponseEntity<?> sendOTPForget(EmailDto emailDto) throws Exception;
}
