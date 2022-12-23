package com.ShopIT.Service.Impl;

import com.ShopIT.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Override
    public ResponseEntity<?> updateUserProfile() {
        return null;
    }
}
