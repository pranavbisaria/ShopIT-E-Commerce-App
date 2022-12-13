package com.ShopIT.Service;

import com.ShopIT.Exceptions.Apiexception;
import com.ShopIT.Exceptions.ResourceNotFoundException;
import com.ShopIT.Models.User;
import com.ShopIT.Payloads.ApiResponse;
import com.ShopIT.Payloads.JwtAccessTokenResponse;
import com.ShopIT.Payloads.JwtAuthResponse;
import com.ShopIT.Repository.UserRepo;
import com.ShopIT.Security.JwtTokenHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Service @RequiredArgsConstructor
public class JWTTokenGenerator {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenHelper jwtTokenHelper;
    private final UserDetailsService userDetailsService;
    private final UserRepo userRepo;

    private void authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try{
            this.authenticationManager.authenticate(authenticationToken);
        }
        catch (BadCredentialsException e) {
            System.out.println("Invalid Details");
            throw new Apiexception("Invalid Username or Password");
        }
    }
    public JwtAuthResponse getTokenGenerate(String email, String Password){
        this.authenticate(email, Password);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
        String myAccessToken = this.jwtTokenHelper.generateAccessToken(userDetails);
        String myRefreshToken = this.jwtTokenHelper.generateRefreshToken(userDetails);
        JwtAuthResponse response = new JwtAuthResponse();
        response.setAccessToken(myAccessToken);
        response.setRefreshToken(myRefreshToken);
        return response;
    }
}
