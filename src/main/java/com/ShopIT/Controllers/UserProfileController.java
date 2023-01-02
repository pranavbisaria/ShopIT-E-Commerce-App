package com.ShopIT.Controllers;

import com.ShopIT.Config.AppConstants;
import com.ShopIT.Models.User;
import com.ShopIT.Payloads.*;
import com.ShopIT.Repository.UserRepo;
import com.ShopIT.Security.CurrentUser;
import com.ShopIT.Service.StorageServices;
import com.ShopIT.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.OK;

@RestController
@Cacheable("User")
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class UserProfileController {
    private final UserRepo userRepo;
    private final StorageServices storageServices;
    private final UserService userService;
    private final ModelMapper modelMapper;
    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateUserProfileProfile(@CurrentUser User user, @Valid @RequestBody UserProfile userProfile){
        return this.userService.updateUserProfile(user, userProfile);
    }
    @GetMapping("/getProfile")
    public ResponseEntity<?> getUserProfile(@CurrentUser User user){
        return new ResponseEntity<>(this.modelMapper.map(user, UserProfile.class), OK);
    }
    @PatchMapping("/updateProfilePhoto")
    public ResponseEntity<?> uploadUserProfile(@CurrentUser User user, @RequestParam("photo") MultipartFile photo){
        if (!photo.getContentType().equals("image/png") && !photo.getContentType().equals("image/jpg") && !photo.getContentType().equals("image/jpeg")){
            return new ResponseEntity<>(new ApiResponse("File is not of image type(JPEG/ JPG or PNG)!!!", false), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        if(user.getProfilePhoto().equals(AppConstants.malePhoto) || user.getProfilePhoto().equals(AppConstants.femalePhoto)){
            this.storageServices.deleteFile(user.getProfilePhoto().substring(user.getProfilePhoto().lastIndexOf("/") + 1));
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
    @PostMapping("/sendMobileOTP")
    public ResponseEntity<?> sendMobileOTP(@CurrentUser User user, @Valid @RequestBody TwilioCacheDto twilioCacheDto){
        return this.userService.sendPhoneOTP(user, twilioCacheDto);
    }
    @PatchMapping("/resetPhoneNumber")
    public ResponseEntity<?> ResetPhoneNumber(@CurrentUser User user, @Valid @RequestBody TwilioCacheDto twilioCacheDto){
        return this.userService.verifyResetPhoneOTP(user, twilioCacheDto);
    }
    @PostMapping("/sendEmailOTP")
    public ResponseEntity<?> SendEmailOTP(@CurrentUser User user, @Valid @RequestBody EmailDto emailDto) throws Exception {
        return this.userService.sendEmailOTP(user, emailDto.getEmail().trim().toLowerCase());
    }
    @PatchMapping("/resetEmailID")
    public ResponseEntity<?> SendEmailOTP(@CurrentUser User user, @Valid @RequestBody OtpDto otpDto){
        return this.userService.verifyResetEmailOTP(user, otpDto);
    }
    @GetMapping("/address/get")
    public ResponseEntity<?> getAllAddress(@CurrentUser User user){
        return this.userService.getAllAddress(user);
    }
    @PostMapping("/addAddress")
    public ResponseEntity<?> addAddress(@CurrentUser User user, @Valid @RequestBody AddressDto addressDto){
        return this.userService.addAddress(user, addressDto);
    }
    @PutMapping("/updateAddress/{addressId}")
    public ResponseEntity<?> updateAddress(@CurrentUser User user, @Valid @RequestBody AddressDto addressDto, @PathVariable("addressId") Long addressId){
        return this.userService.updateAddress(user, addressDto, addressId);
    }
    @DeleteMapping("/removeAddress/{addressId}")
    public ResponseEntity<?> removeAddress(@CurrentUser User user, @PathVariable("addressId") Long addressId) {
        return this.userService.removeAddress(user, addressId);
    }
}
