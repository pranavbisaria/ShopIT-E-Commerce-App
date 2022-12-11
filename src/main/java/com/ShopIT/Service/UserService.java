package com.ShopIT.Service;
import com.ShopIT.Payloads.EditUserDto;
import com.ShopIT.Payloads.ForgetPassword;
import com.ShopIT.Payloads.RegisterMerchant;
import com.ShopIT.Payloads.UserDto;

import java.util.List;

public interface UserService {

    void registerNewUser(UserDto user, int otp);
    void registerNewMerchant(RegisterMerchant user, int otp);
    String updateUserProfile(EditUserDto editUserDto);
    boolean isOTPValid(String email);

    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    void DeleteUser(Integer userId);
    void updateUserPass(ForgetPassword password);
    boolean emailExists(String email);
//    List<CategoryDTO> getAllCategory();
//    List<CategoryDTO> getAllTrendingCategory();
}
