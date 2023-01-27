package com.ShopIT.Service.Impl;

import com.ShopIT.Config.UserCache;
import com.ShopIT.Exceptions.ResourceNotFoundException;
import com.ShopIT.Models.Address;
import com.ShopIT.Models.User;
import com.ShopIT.Payloads.*;
import com.ShopIT.Repository.AddressRepo;
import com.ShopIT.Repository.UserRepo;
import com.ShopIT.Security.JwtTokenHelper;
import com.ShopIT.Service.OTPService;
import com.ShopIT.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.Objects;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.OK;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final OTPService otpService;
    private final JwtTokenHelper jwtTokenHelper;
    private final UserCache userCache;
    private final UserRepo userRepo;
    private final UserDetailsService userDetailsService;
    private final AddressRepo addressRepo;
    @Override
    public ResponseEntity<?> updateUserProfile(User user, UserProfile userProfile) {
        if (userProfile.getGender().equals("f")) {
            user.setGender("female");
        } else {
            user.setGender("male");
        }
        user.setFirstname(userProfile.getFirstname());
        user.setLastname(userProfile.getLastname());
        this.userRepo.save(user);
        userProfile = this.modelMapper.map(user, UserProfile.class);
        return new ResponseEntity<>(userProfile, OK);
    }
    @Override
    public ResponseEntity<?> sendPhoneOTP(User user, TwilioCacheDto twilioCacheDto) {
        String email = user.getEmail();
        if (this.userRepo.existsUserByPhoneNumber(twilioCacheDto.getPhoneNumber())) {
            return new ResponseEntity<>(new ApiResponse("Phone Number already linked with other account", false), NOT_ACCEPTABLE);
        }
        twilioCacheDto.setOne_time_password(this.otpService.OTPRequestThroughNumber(twilioCacheDto.getPhoneNumber(), user.getFirstname()));
        if (this.userCache.isCachePresent(email)) {
            this.userCache.clearCache(email);
        }
        this.userCache.setUserCache(email, twilioCacheDto);
        return new ResponseEntity<>(new ApiResponse("OTP has been successfully generated", true), OK);
    }
    @Override
    public ResponseEntity<?> verifyResetPhoneOTP(User user, TwilioCacheDto twilioCacheDto){
        if (!this.userCache.isCachePresent(user.getEmail())) {
            return new ResponseEntity<>(new ApiResponse("Invalid Request", false), HttpStatus.FORBIDDEN);
        }
        TwilioCacheDto storedOtpDto = (TwilioCacheDto)this.userCache.getCache(user.getEmail());
        if (Objects.equals(storedOtpDto.getOne_time_password(), twilioCacheDto.getOne_time_password())) {
            this.userCache.clearCache(user.getEmail());
            user.setPhoneNumber(twilioCacheDto.getPhoneNumber());
            this.userRepo.save(user);
            return new ResponseEntity<>(new ApiResponse("OTP successfully verified, phone number has been updated", true), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse("Invalid OTP or Action not required!!", false), HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @Override
    public ResponseEntity<?> sendEmailOTP(User user, String email) throws Exception {
        if(this.userRepo.findByEmail(email).isPresent()){
            return new ResponseEntity<>(new ApiResponse("Entered email already linked with other account", false), NOT_ACCEPTABLE);
        }
        try {
            if (this.userCache.isCachePresent(email)) {
                this.userCache.clearCache(email);
            }
            OtpDto otpDto = new OtpDto(email, this.otpService.OTPRequest(email), null, false);
            this.userCache.setUserCache(email, otpDto);
            return new ResponseEntity<>(new ApiResponse("OTP Sent Success on the entered Email", true), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse("Can't able to make your request", false), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    @Override
    public ResponseEntity<?> verifyResetEmailOTP(User user, OtpDto otpDto){
        if (!this.userCache.isCachePresent(otpDto.getEmail())) {
            return new ResponseEntity<>(new ApiResponse("Invalid Request", false), HttpStatus.FORBIDDEN);
        }
        OtpDto storedOtpDto = (OtpDto)this.userCache.getCache(otpDto.getEmail());
        if (Objects.equals(storedOtpDto.getOne_time_password(), otpDto.getOne_time_password())) {
            this.userCache.clearCache(otpDto.getEmail());
            user.setEmail(otpDto.getEmail());
            this.userRepo.saveAndFlush(user);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(otpDto.getEmail());
            JwtAuthResponse response = new JwtAuthResponse(
                    this.jwtTokenHelper.generateAccessToken(userDetails),
                    this.jwtTokenHelper.generateRefreshToken(userDetails),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getRoles()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse("Invalid OTP or Action not required!!", false), HttpStatus.NOT_ACCEPTABLE);
    }
//-----------------------------------------------Address----------------------------------------------------------------------------

    @Override
    public ResponseEntity<?> getAllAddress(User user){
        Set<Address> addresses = user.getProfile().getAddress();
        return new ResponseEntity<>(addresses, OK);
    }
    @Override
    public ResponseEntity<?> addAddress(User user, AddressDto addressDto){
        Address address = this.modelMapper.map(addressDto, Address.class);
        user.getProfile().getAddress().add(address);
        this.userRepo.saveAndFlush(user);
        return new ResponseEntity<>(user.getProfile().getAddress(), OK);
    }
    @Override
    public ResponseEntity<?> updateAddress(User user, AddressDto addressDto, Long addressId){
        Address address = this.addressRepo.findById(addressId).orElseThrow(()-> new ResourceNotFoundException("Address", "addressId", addressId));
        if(!user.getProfile().getAddress().contains(address)){
            return new ResponseEntity<>(new ApiResponse("User not authorize to perform the action", false), HttpStatus.FORBIDDEN);
        }
        address.setType(addressDto.getType());
        address.setName(addressDto.getName());
        address.setMobile(addressDto.getMobile());
        address.setPincode(addressDto.getPincode());
        address.setLocality(addressDto.getLocality());
        address.setAddressLine(addressDto.getAddressLine());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setLandmark(addressDto.getLandmark());
        address.setMobile_alternative(addressDto.getMobile_alternative());
        this.addressRepo.saveAndFlush(address);
        return new ResponseEntity<>(user.getProfile().getAddress(), OK);
    }
    @Override
    public ResponseEntity<?> removeAddress(User user, Long addressId){
        Address address = this.addressRepo.findById(addressId).orElseThrow(()-> new ResourceNotFoundException("Address", "addressId", addressId));
        if(!user.getProfile().getAddress().contains(address)){
            return new ResponseEntity<>(new ApiResponse("User not authorize to perform the action", false), HttpStatus.FORBIDDEN);
        }
        user.getProfile().getAddress().remove(address);
        this.userRepo.save(user);
        this.addressRepo.delete(address);
        return new ResponseEntity<>(new ApiResponse("Address has been successfully deleted", true), OK);
    }
}
