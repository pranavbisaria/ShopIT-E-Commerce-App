package com.ShopIT.Service;

import com.ShopIT.Models.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> updateUserProfile(User user, UserProfile userProfile);
}
