package com.ShopIT.Controllers;

import com.ShopIT.Payloads.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path ="/api")
public class ExampleController {
    @GetMapping("/example")
    public ResponseEntity<?> Example(){
        return new ResponseEntity<>(new ApiResponse("API CHAL GAI CHAL GAI!!!!!!!!", true), OK);
    }
}