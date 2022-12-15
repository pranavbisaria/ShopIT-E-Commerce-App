package com.ShopIT.Service;
import com.ShopIT.Payloads.*;
import com.ShopIT.Security.JwtAuthRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<?> LoginAPI(JwtAuthRequest request);
    ResponseEntity<?> registerEmail(EmailDto emailDto);
    ResponseEntity<?> verifyToRegister(OtpDto otpDto);
    ResponseEntity<?> signupUser(UserDto userDto);
    ResponseEntity<?> registerMerchant(RegisterMerchant registerMerchant);
    ResponseEntity<?> verifyOTPPasswordChange(OtpDto otpDto);
    String updateUserProfile(EditUserDto editUserDto);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    void DeleteUser(Integer userId);
    void updateUserPass(ForgetPassword password);
    boolean emailExists(String email);
    ResponseEntity<?> resetPassword(ForgetPassword forgetPassword);
    ResponseEntity<?> sendOTPForget(EmailDto emailDto);
//    List<CategoryDTO> getAllCategory();
//    List<CategoryDTO> getAllTrendingCategory();
}
