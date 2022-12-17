package com.ShopIT.Controllers;

import com.ShopIT.Payloads.*;
import com.ShopIT.Security.JwtAuthRequest;
import com.ShopIT.Service.JWTTokenGenerator;
import com.ShopIT.Service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController @RequiredArgsConstructor
@RequestMapping(path ="/api/auth")
public class AuthController {
    private final AuthService userService;
    private final JWTTokenGenerator jwtTokenGenerator;
// User as well as the host login API and          -------------------------/TOKEN GENERATOR/-----------------------
    @PostMapping("/login")
    public ResponseEntity<?> createToken(@Valid @RequestBody JwtAuthRequest request) {
        return this.userService.LoginAPI(request);
    }
//Regenerate refresh token
    @GetMapping("/regenerateToken")
    public ResponseEntity<?> refreshToken(@RequestParam String token) {
        return this.jwtTokenGenerator.getRefreshTokenGenerate(token);
    }
//Register Email
    @PostMapping("/signupEmail")
    public ResponseEntity<?> registerEmail(@Valid @RequestBody EmailDto emailDto) {
        return this.userService.registerEmail(emailDto);
    }
//Verify OTP for activation of user/host account
    @PostMapping("/verifyotp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpDto otpDto) {
        return this.userService.verifyToRegister(otpDto);
    }
//SignUP API for user
    @PostMapping("/signupUser")
    public ResponseEntity<?> registerUserDetails(@Valid @RequestBody UserDto userDto) {
        return this.userService.signupUser(userDto);
    }
//Signup API for Host
    @PostMapping("/signupHost")
    public ResponseEntity<?> registerMerchant(@Valid @RequestBody RegisterMerchant registerMerchant) {
        return this.userService.registerMerchant(registerMerchant);
    }
//Sign-in/Signup using google
    @PostMapping("/signGoogle")
    public ResponseEntity<?> signWithGoogle(@Valid @RequestParam String TokenG) throws JsonProcessingException {
        return this.userService.signGoogle(TokenG);
    }
//Forget Password and otp generator API
    @PostMapping("/forget")
    public ResponseEntity<?> sendOTP(@Valid @RequestBody EmailDto emailDto) {
        return userService.sendOTPForget(emailDto);
    }
//Verify OTP for Password Change
    @PostMapping("/verifyPassOtp")
    public ResponseEntity<?> verifyOtpPassChange(@Valid @RequestBody OtpDto otpDto) {
        return userService.verifyOTPPasswordChange(otpDto);
    }
//Reset Password OTP to change the password
    @PostMapping("/resetpass")
    public ResponseEntity<?> resetPass(@Valid @RequestBody ForgetPassword forgetPassword) {
        return this.userService.resetPassword(forgetPassword);
    }
}