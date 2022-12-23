package com.ShopIT.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface UserService {
    ResponseEntity<?> updateUserProfile();
}
