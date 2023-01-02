package com.ShopIT.Service.Impl;

import com.ShopIT.Config.AppConstants;
import com.ShopIT.Config.UserCache;
import com.ShopIT.Exceptions.ResourceNotFoundException;
import com.ShopIT.Models.MerchantProfile;
import com.ShopIT.Models.Profile;
import com.ShopIT.Models.Role;
import com.ShopIT.Models.User;
import com.ShopIT.Payloads.*;
import com.ShopIT.Repository.RoleRepo;
import com.ShopIT.Repository.UserRepo;
import com.ShopIT.Security.JwtAuthRequest;
import com.ShopIT.Security.JwtTokenHelper;
import com.ShopIT.Service.AuthService;
import com.ShopIT.Service.JWTTokenGenerator;
import com.ShopIT.Service.OTPService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

@Service @RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final UserCache userCache;
    private final JWTTokenGenerator jwtTokenGenerator;
    private final UserDetailsService userDetailsService;
    private final JwtTokenHelper jwtTokenHelper;
    private final OTPService otpService;
    @Override
    public ResponseEntity<?> LoginAPI(JwtAuthRequest request) {
        request.setEmail(request.getEmail().trim().toLowerCase());
        User user = this.userRepo.findByEmail(request.getEmail()).orElseThrow(() ->new ResourceNotFoundException("User", "Email: " + request.getEmail(), 0));
        if(user.getPassword()==null){
            return new ResponseEntity<>(new ApiResponse("Please reset your password first!!!", false), HttpStatus.UNAUTHORIZED);
        }
        if (user.isEnabled()) {
            JwtAuthResponse response = this.jwtTokenGenerator.getTokenGenerate(request.getEmail(), request.getPassword());
            if (response == null) {
                return new ResponseEntity<>(new ApiResponse("Invalid Password", true), HttpStatus.UNAUTHORIZED);
            } else {
                response.setFirstname(user.getFirstname());
                response.setLastname(user.getLastname());
                response.setRoles(user.getRoles());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        if (this.userCache.isCachePresent(request.getEmail())) {
            this.userCache.clearCache(request.getEmail());
        }
        OtpDto otpDto = new OtpDto(request.getEmail(), this.otpService.OTPRequest(request.getEmail()), null, false);
        this.userCache.setUserCache(request.getEmail(), otpDto);
        return new ResponseEntity<>(new ApiResponse("OTP has been successfully sent on the registered email id!!", true), HttpStatus.ACCEPTED);
    }
    @Override
    public ResponseEntity<?> registerEmail(EmailDto emailDto, String type) throws Exception {
        String email = emailDto.getEmail().trim().toLowerCase();
        Role newRole = this.roleRepo.findById(AppConstants.ROLE_NORMAL).get();
        if (type.equals("merchant")) {
            newRole = this.roleRepo.findById(1003).get();
            if (this.emailExists(email)) {
                User user = this.userRepo.findByEmail(email).orElseThrow(() ->new ResourceNotFoundException("User", "Email: " + email, 0));
                if (user.getRoles().contains(newRole)) {
                    return new ResponseEntity<>(new ApiResponse("User already exist with the entered email id", false), HttpStatus.CONFLICT);
                }
                newRole = this.roleRepo.findById(AppConstants.ROLE_NORMAL).get();
            }
        } else if (this.emailExists(email)) {
            User user = this.userRepo.findByEmail(email).orElseThrow(() ->new ResourceNotFoundException("User", "Email: " + email, 0));
            if (user.getRoles().contains(newRole)) {
                return new ResponseEntity<>(new ApiResponse("User already exist with the entered email id", false), HttpStatus.CONFLICT);
            }
            newRole = (Role)this.roleRepo.findById(1003).get();
        }
        try {
            if (this.userCache.isCachePresent(email)) {
                this.userCache.clearCache(email);
            }
            OtpDto otpDto = new OtpDto(email, this.otpService.OTPRequest(email), newRole, false);
            this.userCache.setUserCache(email, otpDto);
            return new ResponseEntity<>(new ApiResponse("OTP Sent Success on the entered Email", true), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse("Can't able to make your request", false), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    @Override
    public ResponseEntity<?> verifyToRegister(OtpDto otpDto) throws ExecutionException {
        String email = otpDto.getEmail().trim().toLowerCase();
        if (!this.userCache.isCachePresent(email)) {
            return new ResponseEntity<>(new ApiResponse("Invalid Request", false), HttpStatus.FORBIDDEN);
        } else {
            OtpDto storedOtpDto = (OtpDto)this.userCache.getCache(otpDto.getEmail());
            if (storedOtpDto.getOne_time_password() == otpDto.getOne_time_password()) {
                return new ResponseEntity<>(new ApiResponse("OTP Successfully Verified", true), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("Invalid OTP!!", false), HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }
    @Override
    public ResponseEntity<?> signupUser(UserDto userDto, String type) throws ExecutionException {
        userDto.setFirstname(userDto.getFirstname().trim());
        userDto.setLastname(userDto.getLastname().trim());
        String email = userDto.getEmail().trim().toLowerCase();
        Role newRole = this.roleRepo.findById(AppConstants.ROLE_NORMAL).get();
        if (type.equals("merchant")) {
            newRole = this.roleRepo.findById(AppConstants.ROLE_MERCHANT).get();
        }
        if (this.emailExists(email)) {
            User user = this.userRepo.findByEmail(email).orElseThrow(() ->new ResourceNotFoundException("User", "Email: " + email, 0));
            if (user.getRoles().contains(newRole)) {
                return new ResponseEntity<>(new ApiResponse("Invalid Action", false), HttpStatus.SERVICE_UNAVAILABLE);
            }
        }
        if (!this.userCache.isCachePresent(email)) {
            return new ResponseEntity<>(new ApiResponse("Session Time-Out, please try again", false), HttpStatus.REQUEST_TIMEOUT);
        } else {
            OtpDto storedOtpDto = (OtpDto)this.userCache.getCache(email);
            if (storedOtpDto.getOne_time_password() == userDto.getOne_time_password()) {
                this.userCache.clearCache(email);
                User user = new User();
                user.setEmail(email);
                user.setFirstname(userDto.getFirstname());
                user.setLastname(userDto.getLastname());
                if ((userDto.getGender().equals("f"))) {
                    user.setProfilePhoto(AppConstants.femalePhoto);
                    user.setGender("female");
                } else {
                    user.setProfilePhoto(AppConstants.malePhoto);
                    user.setGender("male")  ;
                }
                if (type.equals("merchant")) {
                    MerchantProfile merchantProfile = new MerchantProfile();
                    user.setMerchantProfile(merchantProfile);
                }
                else{
                    Profile profile = new Profile();
                    user.setProfile(profile);
                }
                user.getRoles().add(newRole);
                user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
                this.userRepo.save(user);
                this.otpService.SuccessRequest(user.getEmail(), user.getFirstname());
                return new ResponseEntity<>(new ApiResponse("User ID Successfully Created", true), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("Invalid OTP input", false), HttpStatus.UNAUTHORIZED);
            }
        }
    }
    @Override
    public ResponseEntity<?> signGoogle(String Token) throws JsonProcessingException, NullPointerException {
        Token = new String(Base64.decodeBase64(Token.split("\\.")[1]), StandardCharsets.UTF_8);
        ObjectMapper mapper = (new ObjectMapper()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        GoogleSignModel payload = null;
        try {
            payload = mapper.readValue(Token, GoogleSignModel.class);
            if (payload != null) {
                if (this.verifyGoogleToken(payload)) {
                    JwtAuthResponse jwtAuthResponse = null;
                    String email = payload.email();
                    User user;
                    if (this.emailExists(email)) {
                        user = (User)this.userRepo.findByEmail(email).orElseThrow(() ->new ResourceNotFoundException("User", "Email: " + email, 0));
                        UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getUsername());
                        String myAccessToken = this.jwtTokenHelper.generateAccessToken(userDetails);
                        String myRefreshToken = this.jwtTokenHelper.generateRefreshToken(userDetails);
                        jwtAuthResponse = new JwtAuthResponse(myAccessToken, myRefreshToken, user.getFirstname(), user.getLastname(), user.getRoles());
                    } else {
                        user = new User();
                        user.setEmail(payload.email());
                        user.setFirstname(payload.given_name());
                        user.setLastname(payload.family_name());
                        user.setPassword("Google");
                        user.setProfilePhoto(payload.picture());
                        Role role = this.roleRepo.findById(AppConstants.ROLE_NORMAL).get();
                        user.getRoles().add(role);
                        this.userRepo.save(user);
                        UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getUsername());
                        String myAccessToken = this.jwtTokenHelper.generateAccessToken(userDetails);
                        String myRefreshToken = this.jwtTokenHelper.generateRefreshToken(userDetails);
                        jwtAuthResponse = new JwtAuthResponse(myAccessToken, myRefreshToken, user.getFirstname(), user.getLastname(), user.getRoles());
                    }
                    return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ApiResponse("Either the token is expired or the token is not authorized", false), HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>(new ApiResponse("Invalid Action", false), HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException | JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse("Invalid Token Input", false), HttpStatus.FORBIDDEN);
        }
    }
    @Override
    public ResponseEntity<?> verifyOTPPasswordChange(OtpDto otpDto) throws ExecutionException {
        String email = otpDto.getEmail().trim().toLowerCase();
        User userOTP = (User)this.userRepo.findByEmail(email).orElseThrow(() ->new ResourceNotFoundException("User", "Email: " + email, 0));
        if (!userOTP.isEnabled() || userOTP.getPassword()==null) {
            if (!this.userCache.isCachePresent(email)) {
                return new ResponseEntity<>(new ApiResponse("Session Time-Out, please try again", false), HttpStatus.REQUEST_TIMEOUT);
            } else {
                OtpDto storedOtpDto = (OtpDto)this.userCache.getCache(email);
                return storedOtpDto.getOne_time_password() == otpDto.getOne_time_password() ? new ResponseEntity<>(new ApiResponse("OTP Successfully Verified", true), HttpStatus.OK) : new ResponseEntity<>(new ApiResponse("Invalid OTP!!", false), HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse("INVALID ACTION!!!", false), HttpStatus.BAD_REQUEST);
        }
    }
    public boolean emailExists(String email) {
        return this.userRepo.findByEmail(email).isPresent();
    }
    @Override
    public ResponseEntity<?> resetPassword(ForgetPassword forgetPassword) {
        String email = forgetPassword.getEmail().trim().toLowerCase();
        User userRP = (User)this.userRepo.findByEmail(email).orElseThrow(() -> {
            return new ResourceNotFoundException("User", "Email :" + email, 0L);
        });
        if (!userRP.isEnabled()) {
            if (!this.userCache.isCachePresent(email)) {
                return new ResponseEntity<>(new ApiResponse("Session Time-Out, please try again", false), HttpStatus.REQUEST_TIMEOUT);
            } else {
                OtpDto storedOtpDto = (OtpDto)this.userCache.getCache(email);
                if (storedOtpDto.getOne_time_password() == forgetPassword.getOtp()) {
                    userRP.setPassword(this.passwordEncoder.encode(forgetPassword.getPassword()));
                    userRP.setEnable(true);
                    this.userRepo.save(userRP);
                    return new ResponseEntity<>(new ApiResponse("Password Reset SUCCESS", true), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ApiResponse("Invalid OTP!!!", false), HttpStatus.FORBIDDEN);
                }
            }
        } else {
            return new ResponseEntity<>(new ApiResponse("Invalid Action!!", false), HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @Override
    public ResponseEntity<?> sendOTPForget(EmailDto emailDto) throws Exception {
        String email = emailDto.getEmail().trim().toLowerCase();
        User user = (User)this.userRepo.findByEmail(email).orElseThrow(() ->new ResourceNotFoundException("User", "Email: " + email, 0));
        try {
            OtpDto otp = new OtpDto(email, this.otpService.OTPRequest(email), (Role)null, true);
            user.setEnable(false);
            user.setPassword(null);
            if (this.userCache.isCachePresent(email)) {
                this.userCache.clearCache(email);
            }
            this.userCache.setUserCache(email, otp);
            this.userRepo.save(user);
            return new ResponseEntity<>(new ApiResponse("OTP Sent Success", true), HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception("Cannot able to send the mail to the registered account", e);
        }
    }
    Boolean verifyGoogleToken(GoogleSignModel googleSignModel) {
        return googleSignModel.azp().equals(AppConstants.GOOGLE_CLIENT_ID) && googleSignModel.iss().equals(AppConstants.GOOGLE_ISSUER) && googleSignModel.exp() * 1000L >= System.currentTimeMillis();
    }
}
