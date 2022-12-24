package com.ShopIT.Controllers;

import com.ShopIT.Models.User;
import com.ShopIT.Payloads.ApiResponse;
import com.ShopIT.Payloads.UserProfile;
import com.ShopIT.Repository.UserRepo;
import com.ShopIT.Security.CurrentUser;
import com.ShopIT.Service.StorageServices;
import com.ShopIT.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Cacheable("User")
@RequestMapping("/api/profile")
public class UserProfileController {
    private final UserRepo userRepo;
    private final StorageServices storageServices;
    private final UserService userService;
    private final ModelMapper modelMapper;
    @PutMapping("/updateProfilePhoto")
    public ResponseEntity<?> updateUserProfileProfile(@CurrentUser User user, @RequestBody UserProfile userProfile){
        return this.userService.updateUserProfile(user, userProfile);
    }
    @GetMapping("/getProfile")
    public ResponseEntity<?> getUserProfile(@CurrentUser User user){
        user.setProfilePhoto(ServletUriComponentsBuilder.fromCurrentContextPath().path("/image/").path(user.getProfilePhoto()).toUriString());
        return new ResponseEntity<>(this.modelMapper.map(user, UserProfile.class), OK);
    }
    @PatchMapping("/updateProfile")
    public ResponseEntity<?> uploadUserProfile(@CurrentUser User user, @RequestParam("photo") MultipartFile photo){
        if (!photo.getContentType().equals("image/png") && !photo.getContentType().equals("image/jpg") && !photo.getContentType().equals("image/jpeg")){
            return new ResponseEntity<>(new ApiResponse("File is not of image type(JPEG/ JPG or PNG)!!!", false), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        user.setProfilePhoto(this.storageServices.uploadFile(photo));
        this.userRepo.save(user);
        return new ResponseEntity<>(this.modelMapper.map(user, UserProfile.class), OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/getAllUsers")
    public ResponseEntity<?>  getAllUser(){
        return new ResponseEntity<>(this.userRepo.findAll(), OK);
    }
}
