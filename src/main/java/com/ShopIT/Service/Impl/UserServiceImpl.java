package com.ShopIT.Service.Impl;

import com.ShopIT.Models.User;
import com.ShopIT.Payloads.UserProfile;
import com.ShopIT.Repository.UserRepo;
import com.ShopIT.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    @Override
    public ResponseEntity<?> updateUserProfile(User user, UserProfile userProfile) {
        user.setGender(userProfile.getGender());
        user.setFirstname(userProfile.getFirstname());
        user.setLastname(userProfile.getLastname());
        this.userRepo.save(user);
        userProfile = this.modelMapper.map(user, UserProfile.class);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }
}
