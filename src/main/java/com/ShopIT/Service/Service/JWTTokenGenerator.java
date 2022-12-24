package com.ShopIT.Service.Service;

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

    private boolean authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            this.authenticationManager.authenticate(authenticationToken);
            return true;
        } catch (BadCredentialsException var5) {
            System.out.println("Invalid Password");
            return false;
        }
    }
    public JwtAuthResponse getTokenGenerate(String email, String Password){
        if (this.authenticate(email, Password)) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
            String myAccessToken = this.jwtTokenHelper.generateAccessToken(userDetails);
            String myRefreshToken = this.jwtTokenHelper.generateRefreshToken(userDetails);
            JwtAuthResponse response = new JwtAuthResponse();
            response.setAccessToken(myAccessToken);
            response.setRefreshToken(myRefreshToken);
            return response;
        } else {
            return null;
        }
    }

    public ResponseEntity<?> getRefreshTokenGenerate(String token){
        if(token != null){
            try {
                String username = this.jwtTokenHelper.getUsernameFromToken(token);
                if(username.startsWith("#refresh")) {
                    String finalUsername = username.substring(8);
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(finalUsername);
                    User user = userRepo.findByEmail(finalUsername).orElseThrow(() -> new ResourceNotFoundException("User", "Email: " + finalUsername, 0));
                    if (this.jwtTokenHelper.validateRefreshToken(token, userDetails)) {
                        String myAccessToken = this.jwtTokenHelper.generateAccessToken(userDetails);
                        return new ResponseEntity<>(new JwtAccessTokenResponse(myAccessToken, user.getFirstname(), user.getLastname(), user.getRoles()), OK);
                    }
                    else {
                        return new ResponseEntity<>(new ApiResponse("Refresh Token Expired!!", false), HttpStatus.REQUEST_TIMEOUT);
                    }
                }
                else{
                    return new ResponseEntity<>(new ApiResponse("Not a Refresh Token", false), BAD_REQUEST);
                }
            }
            catch(IllegalArgumentException e){
                return new ResponseEntity<>(new ApiResponse("Unable to get the JWT token!!", false), HttpStatus.BAD_REQUEST);
            }
            catch(ExpiredJwtException e){
                return new ResponseEntity<>(new ApiResponse("Refresh Token Expired!!", false), HttpStatus.REQUEST_TIMEOUT);
            }
            catch(MalformedJwtException e){
                return new ResponseEntity<>(new ApiResponse("Invalid jwt token", false), BAD_REQUEST);
            }
        }
        else {
            return new ResponseEntity<>(new ApiResponse("Invalid jwt token", false), BAD_REQUEST);
        }
    }
}
