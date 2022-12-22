package com.ShopIT.Controllers;

import com.ShopIT.Models.User;
import com.ShopIT.Repository.UserRepo;
import com.ShopIT.Security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserProfileController {
    private final UserRepo userRepo;
    @Cacheable("User")
    @GetMapping("/getProfile")
    public ResponseEntity<?> getUserProfile(@CurrentUser User user){
        return new ResponseEntity<>(user, OK);
    }
    @GetMapping("/getAllUsers")
    public ResponseEntity<?>  getAllUser(){
        return new ResponseEntity<>(this.userRepo.findAll(), OK);
    }
}
