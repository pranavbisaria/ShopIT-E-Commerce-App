package com.ShopIT.Config;

import com.ShopIT.Payloads.OtpDto;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserCache {
    private LoadingCache<String, OtpDto> newUserCache;

    public UserCache() {
        this.newUserCache = CacheBuilder.newBuilder().expireAfterWrite(AppConstants.EXPIRE_MINs, TimeUnit.MINUTES).build(new CacheLoader<>() {
            public OtpDto load(String username) {
                return null;
            }
        });
    }

    public void setUserCache(String username, OtpDto otpDto) {
        try {
            this.newUserCache.put(username, otpDto);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public OtpDto getOTP(String username) throws ExecutionException {
        try {
            return (OtpDto)this.newUserCache.get(username);
        } catch (Exception var3) {
            return null;
        }
    }

    public Boolean isOTPPresent(String username) {
        OtpDto otpDto = (OtpDto)this.newUserCache.getIfPresent(username);
        return otpDto != null;
    }

    public void clearOTP(String username) {
        this.newUserCache.invalidate(username);
    }

    public LoadingCache<String, OtpDto> getNewUserCache() {
        return this.newUserCache;
    }

    public void setNewUserCache(final LoadingCache<String, OtpDto> newUserCache) {
        this.newUserCache = newUserCache;
    }
}
