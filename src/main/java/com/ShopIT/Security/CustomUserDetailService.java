package com.ShopIT.Security;

import com.ShopIT.Exceptions.ResourceNotFoundException;
import com.ShopIT.Repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User ", "email : "+username, 0));
    }
}
