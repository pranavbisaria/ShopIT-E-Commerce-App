package com.ShopIT.Service.Service;

import com.ShopIT.Models.User;
import com.ShopIT.Payloads.UserProfile;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> updateUserProfile(User user, UserProfile userProfile);
}
