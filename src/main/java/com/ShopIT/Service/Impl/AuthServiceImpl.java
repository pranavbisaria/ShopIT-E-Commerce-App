package com.ShopIT.Service.Impl;
import com.ShopIT.Config.AppConstants;
import com.ShopIT.Exceptions.Apiexception;
import com.ShopIT.Exceptions.ResourceNotFoundException;
import com.ShopIT.Models.Role;
import com.ShopIT.Models.User;
import com.ShopIT.Payloads.*;
import com.ShopIT.Repository.RoleRepo;
import com.ShopIT.Repository.UserRepo;
import com.ShopIT.Security.JwtAuthRequest;
import com.ShopIT.Security.JwtTokenHelper;
import com.ShopIT.Service.JWTTokenGenerator;
import com.ShopIT.Service.OTPService;
import com.ShopIT.Service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service @RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final JWTTokenGenerator jwtTokenGenerator;
    private final UserDetailsService userDetailsService;
    private final JwtTokenHelper jwtTokenHelper;
    private final OTPService otpService;
//LoginAPI
    @Override
    public ResponseEntity<?> LoginAPI(JwtAuthRequest request){
        request.setEmail(request.getEmail().trim().toLowerCase());
        User user = this.userRepo.findByEmail(request.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "Email: " + request.getEmail(), 0));
        if (!user.getTwoStepVerification() || user.isActiveTwoStep()) {
            if (user.isActive()) {
                user.setTwoStepVerification(false);
                user.setActiveTwoStep(false);
                this.userRepo.save(user);
                JwtAuthResponse response = jwtTokenGenerator.getTokenGenerate(request.getEmail(), request.getPassword());
                response.setFirstname(user.getFirstname());
                response.setLastname(user.getLastname());
                response.setRoles(user.getRoles());
                return new ResponseEntity<>(response, OK);
            }
            else {
                return new ResponseEntity<>(new ApiResponse("Please verify your email first", false), HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else {
            user.setOtp(otpService.OTPRequest(request.getEmail()));
            user.setOtpRequestedTime(new Date(System.currentTimeMillis() + AppConstants.OTP_VALID_DURATION));
            this.userRepo.save(user);
            return new ResponseEntity<>(new ApiResponse("OTP has been successfully sent on the registered email id!!", true), HttpStatus.CONTINUE);
        }
    }
//Register Email
    @Override
    public ResponseEntity<?> registerEmail(EmailDto emailDto){
        emailDto.setEmail(emailDto.getEmail().trim().toLowerCase());
        if (emailExists(emailDto.getEmail())) {
            return getResponseEntityRegisterEmail(emailDto);
        }
        else {
            User user = new User();
            user.setFirstname(emailDto.getEmail().substring(0, emailDto.getEmail().indexOf("@")));
            user.setOtp(otpService.OTPRequest(emailDto.getEmail()));
            user.setEmail(emailDto.getEmail());
            user.setActive(false);
            user.setTwoStepVerification(false);
            user.setActiveTwoStep(false);
            user.setProfilePhoto("default.png");
            user.setOtpRequestedTime(new Date(System.currentTimeMillis()+AppConstants.OTP_VALID_DURATION));
            Role role = this.roleRepo.findById(AppConstants.ROLE_NORMAL).get();
            user.getRoles().add(role);
            this.userRepo.save(user);
            return new ResponseEntity<>(new ApiResponse("OTP Sent Success on the entered Email", true), HttpStatus.CREATED);
        }
    }
//Verify To register
    @Override
    public ResponseEntity<?> verifyToRegister(OtpDto otpDto){
        otpDto.setEmail(otpDto.getEmail().trim().toLowerCase());
        User userOTP = this.userRepo.findByEmail(otpDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "Email :" + otpDto.getEmail(), 0));
        if (isOTPValid(otpDto.getEmail()) && userOTP.getOtp() != null) {
            if (userOTP.getOtp() == otpDto.getOne_time_password()) {
                return new ResponseEntity<>(new ApiResponse("OTP Successfully Verified", true), OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("Invalid OTP!!", false), HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            throw new Apiexception("INVALID ACTION!!!");
        }
    }
//Register User after OTP verification
    @Override
    public ResponseEntity<?> signupUser(UserDto userDto){
        userDto.setFirstname(userDto.getFirstname().trim());
        userDto.setLastname(userDto.getLastname().trim());
        userDto.setEmail(userDto.getEmail().trim().toLowerCase());
        User user =this.userRepo.findByEmail(userDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "Email: "+userDto.getEmail(), 0));
       if (isOTPValid(user.getEmail()) && user.getOtp() != null && user.getPassword()==null) {

                user.setFirstname(userDto.getFirstname());
                user.setLastname(userDto.getLastname());
                user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
                user.setActive(true);
                user.setActiveTwoStep(true);
                user.setOtp(null);
                user.setOtpRequestedTime(null);
                this.userRepo.save(user);
                this.otpService.SuccessRequest(user.getEmail(), user.getFirstname());
                return new ResponseEntity<>(new ApiResponse("User ID Successfully Created", true), CREATED);
            }
            else {
                return new ResponseEntity<>(new ApiResponse("Invalid OTP!!", false), HttpStatus.NOT_ACCEPTABLE);
            }

    }
//Register Merchant
    @Override
    public ResponseEntity<?> registerMerchant(RegisterMerchant registerMerchant){
        registerMerchant.setCompanyEmail(registerMerchant.getCompanyEmail().trim().toLowerCase());
        if (emailExists(registerMerchant.getCompanyEmail())) {
            return getResponseEntityMerchant(registerMerchant);
        }
        else {
            User user = new User();
            user.setFirstname(registerMerchant.getCompanyEmail().substring(0, registerMerchant.getCompanyEmail().indexOf("@")));
            user.setEmail(registerMerchant.getCompanyEmail());
            user.setPassword(this.passwordEncoder.encode(registerMerchant.getPassword()));
            user.setTwoStepVerification(false);
            user.setActiveTwoStep(false);
            user.setOtp(otpService.OTPRequest(registerMerchant.getCompanyEmail()));
            user.setActive(false);
            user.setProfilePhoto("default.png");
            user.setOtpRequestedTime(new Date(System.currentTimeMillis()+AppConstants.OTP_VALID_DURATION));
            Role role = this.roleRepo.findById(AppConstants.ROLE_MERCHANT).get();
            user.getRoles().add(role);
            this.userRepo.save(user);
            this.otpService.SuccessRequest(user.getEmail(), user.getFirstname());
            return new ResponseEntity<>(new ApiResponse("OTP Sent Success on the entered Email", true), HttpStatus.CREATED);
        }
    }
//sign-in or sign-up with Google
    @Override
    public ResponseEntity<?> signGoogle(String Token) throws JsonProcessingException {
        Token =  new String (Base64.decodeBase64(Token.split("\\.")[1]), StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        GoogleSignModel payload = null;

    }
//Verify OTp without database alteration
    @Override
    public ResponseEntity<?> verifyOTPPasswordChange(OtpDto otpDto){
        otpDto.setEmail(otpDto.getEmail().trim().toLowerCase());
        User userOTP = this.userRepo.findByEmail(otpDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "Email :" + otpDto.getEmail(), 0));
        if (isOTPValid(otpDto.getEmail()) && userOTP.getOtp() != null && userOTP.isActive()) {
            if (userOTP.getOtp() == otpDto.getOne_time_password()) {
                userOTP.setActive(true);
                userOTP.setActiveTwoStep(true);
                this.userRepo.save(userOTP);
                return new ResponseEntity<>(new ApiResponse("OTP Successfully Verified", true), OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("Invalid OTP!!", false), HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            throw new Apiexception("INVALID ACTION!!!");
        }
    }
    public boolean isOTPValid(String email) {
        User userOTP = this.userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "Email :"+email, 0));
        if (userOTP.getOtp() == null) {
            return false;
        }
        long currentTimeInMillis = System.currentTimeMillis();
        long otpRequestedTimeInMillis = userOTP.getOtpRequestedTime().getTime();
        return otpRequestedTimeInMillis >= currentTimeInMillis;
    }
    @Override
    public void updateUserPass(ForgetPassword password) {
        User user = this.userRepo.findByEmail(password.getEmail()).orElseThrow(()-> new ResourceNotFoundException("User", "Email :"+password.getEmail(), 0));
        user.setOtp(null);
        user.setOtpRequestedTime(null);
        user.setPassword(this.passwordEncoder.encode(password.getPassword()));
        this.userRepo.save(user);
    }
    public boolean emailExists(String email) {
        return userRepo.findByEmail(email).isPresent();
    }
    private ResponseEntity<?> getResponseEntityRegisterEmail(@RequestBody @Valid EmailDto userDto) {
        User user = this.userRepo.findByEmail(userDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "Email: " + userDto.getEmail(), 0));
        if (user.isActive() && user.getRoles().contains(this.roleRepo.findById(AppConstants.ROLE_NORMAL).get())) {
            return new ResponseEntity<>(new ApiResponse("User already exist with the entered email id", false), HttpStatus.CONFLICT);
        } else {
            Role oldRole = this.roleRepo.findById(AppConstants.ROLE_MERCHANT).get();
            if (user.getRoles().contains(oldRole)) {
                Role newRole = this.roleRepo.findById(AppConstants.ROLE_NORMAL).get();
                user.getRoles().add(newRole);
            }
            this.userRepo.save(user);
            return sendOTPForget(new EmailDto(userDto.getEmail()));
        }
    }
    @Override
    public ResponseEntity<?> resetPassword(ForgetPassword forgetPassword){
        forgetPassword.setEmail(forgetPassword.getEmail().trim().toLowerCase());
        User userRP = this.userRepo.findByEmail(forgetPassword.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "Email :" + forgetPassword.getEmail(), 0));
        if (userRP.getOtp() != null) {
            if (Objects.equals(userRP.getOtp(), forgetPassword.getOtp())) {
                if (isOTPValid(forgetPassword.getEmail())) {
                    updateUserPass(forgetPassword);
                    return new ResponseEntity<>(new ApiResponse("Password Reset SUCCESS", true), OK);
                } else {
                    return new ResponseEntity<>(new ApiResponse("Session Expired...........", false), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(new ApiResponse("Invalid OTP!!!", false), HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse("Invalid Action!!", false), HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @Override
    public ResponseEntity<?> sendOTPForget(EmailDto emailDto) {
        emailDto.setEmail(emailDto.getEmail().trim().toLowerCase());
        if (emailExists(emailDto.getEmail())) {
            User user = this.userRepo.findByEmail(emailDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "email: " + emailDto.getEmail(), 0));
            user.setOtp(otpService.OTPRequest(emailDto.getEmail()));
            user.setOtpRequestedTime(new Date(System.currentTimeMillis() + AppConstants.OTP_VALID_DURATION));
            this.userRepo.save(user);
        } else {
            return new ResponseEntity<>(new ApiResponse("User does not exist with the entered email id", false), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse("OTP Sent Success", true), OK);
    }
    private ResponseEntity<?> getResponseEntityMerchant(@RequestBody @Valid RegisterMerchant userDto) {
        User user = this.userRepo.findByEmail(userDto.getCompanyEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "Email: " + userDto.getCompanyEmail(), 0));
        if (user.isActive() && user.getRoles().contains(this.roleRepo.findById(AppConstants.ROLE_MERCHANT).get())) {
            return new ResponseEntity<>(new ApiResponse("User already exist with the entered email id", false), HttpStatus.CONFLICT);
        } else {
            user.setFirstname(userDto.getCompanyEmail().substring(0, userDto.getCompanyEmail().indexOf("@")));
            user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
            Role oldRole = this.roleRepo.findById(AppConstants.ROLE_NORMAL).get();
            if (user.getRoles().contains(oldRole)) {
                Role newRole = this.roleRepo.findById(AppConstants.ROLE_MERCHANT).get();
                user.getRoles().add(newRole);
            }
            this.userRepo.save(user);
            return sendOTPForget(new EmailDto(userDto.getCompanyEmail()));
        }
    }
}
