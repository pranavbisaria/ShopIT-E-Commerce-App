package com.ShopIT.Service;

import com.ShopIT.Models.User;
import com.ShopIT.Payloads.AddressDto;
import com.ShopIT.Payloads.OtpDto;
import com.ShopIT.Payloads.TwilioCacheDto;
import com.ShopIT.Payloads.UserProfile;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> updateUserProfile(User user, UserProfile userProfile);

    ResponseEntity<?> sendPhoneOTP(User user, TwilioCacheDto twilioCacheDto);

    ResponseEntity<?> verifyResetPhoneOTP(User user, TwilioCacheDto twilioCacheDto);

    ResponseEntity<?> sendEmailOTP(User user, String email) throws Exception;

    ResponseEntity<?> verifyResetEmailOTP(User user, OtpDto otpDto);

    ResponseEntity<?> getAllAddress(User user);

    ResponseEntity<?> addAddress(User user, AddressDto addressDto);

    ResponseEntity<?> updateAddress(User user, AddressDto addressDto, Long addressId);

    ResponseEntity<?> removeAddress(User user, Long addressId);
}
