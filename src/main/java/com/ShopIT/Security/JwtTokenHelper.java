package com.ShopIT.Security;

import com.ShopIT.Config.AppConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenHelper {
    //retrieve username from jwt token;
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims:: getSubject);
    }
    //retrieve expiration date from jwt token;
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims:: getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(AppConstants.secret).parseClaimsJws(token).getBody();
    }
    //check if the token has expired
    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    //generate token for user
    public String generateAccessToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return doGenerateAccessToken(claims, userDetails.getUsername());
    }
    public String generateRefreshToken(UserDetails userDetails){
        return doGenerateRefreshToken(userDetails.getUsername());
    }
    private String doGenerateAccessToken(Map<String, Object> claims, String subject){
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis()+ AppConstants.JWT_ACCESS_TOKEN_VALIDITY *1000 ))
        .signWith(SignatureAlgorithm.HS512, AppConstants.secret).compact();
    }
    private String doGenerateRefreshToken(String subject){
        return Jwts.builder().setSubject("#refresh"+subject).setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + AppConstants.JWT_REFRESH_TOKEN_VALIDITY *1000 ))
        .signWith(SignatureAlgorithm.HS512, AppConstants.secret).compact();
    }
    //validate toke
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public Boolean validateRefreshToken(String token, UserDetails userDetails){
        final String userName = getUsernameFromToken(token).substring(8);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
