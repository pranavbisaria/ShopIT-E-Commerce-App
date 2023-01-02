package com.ShopIT.Controllers;

import com.ShopIT.Exceptions.ResourceNotFoundException;
import com.ShopIT.Models.Images;
import com.ShopIT.Models.Sponsor;
import com.ShopIT.Payloads.ApiResponse;
import com.ShopIT.Repository.SponsorRepo;
import com.ShopIT.Service.StorageServices;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
@RestController
@RequestMapping("/sponsor")
@RequiredArgsConstructor
public class SponsorController {
    private final SponsorRepo sponsorRepo;
    private final StorageServices storageServices;
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createSponsor(@RequestPart("images") MultipartFile[] images){
        Sponsor sponsor = new Sponsor();
        return getResponseEntity(images, sponsor);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/add/{sponsorId}")
    public ResponseEntity<?> addSponsor(@PathVariable("sponsorId") Integer sponsorId, @RequestPart("images") MultipartFile[] images){
        Sponsor sponsor = this.sponsorRepo.findById(sponsorId).orElseThrow(()-> new ResourceNotFoundException("Sponsor", "sponsorId", sponsorId));
        return getResponseEntity(images, sponsor);
    }
    @GetMapping("/get/{sponsorId}")
    public ResponseEntity<?> getSponsor(@PathVariable("sponsorId") Integer sponsorId){
        Sponsor sponsor = this.sponsorRepo.findById(sponsorId).orElseThrow(()-> new ResourceNotFoundException("Sponsor", "sponsorId", sponsorId));
        return new ResponseEntity<>(sponsor, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{sponsorId}")
    public ResponseEntity<?> deleteSponsor(@PathVariable("sponsorId") Integer sponsorId){
        Sponsor sponsor = this.sponsorRepo.findById(sponsorId).orElseThrow(()-> new ResourceNotFoundException("Sponsor", "sponsorId", sponsorId));
        List<Images> images = sponsor.getImages();
        images.forEach(multipartFile -> {
            this.storageServices.deleteFile(multipartFile.getImageUrl().substring(multipartFile.getImageUrl().lastIndexOf("/") + 1));
        });
        this.sponsorRepo.delete(sponsor);
        return new ResponseEntity<>(new ApiResponse("All the advertisements has been successfully deleted", true), HttpStatus.OK);
    }
    @NotNull
    private ResponseEntity<?> getResponseEntity(@RequestPart("images") MultipartFile[] images, Sponsor sponsor) {
        Arrays.stream(images).forEach(multipartFile -> {
            Images images1 = new Images();
            images1.setImageUrl(this.storageServices.uploadFile(multipartFile));
            sponsor.getImages().add(images1);
        });
        this.sponsorRepo.saveAndFlush(sponsor);
        return new ResponseEntity<>(sponsor, HttpStatus.OK);
    }
}
