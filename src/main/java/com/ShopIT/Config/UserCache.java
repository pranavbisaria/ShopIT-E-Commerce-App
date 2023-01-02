package com.ShopIT.Config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class UserCache {
    private LoadingCache<String, Object> newUserCache;

    public UserCache() {
        this.newUserCache = CacheBuilder.newBuilder().expireAfterWrite(AppConstants.EXPIRE_MINs, TimeUnit.MINUTES).build(new CacheLoader<>() {
            public Object load(String key) {
                return null;
            }
        });
    }
    public void setUserCache(String username, Object object) {
        try {
            this.newUserCache.put(username, object);
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }
    public Object getCache(String key) {
        try {
            return this.newUserCache.get(key);
        } catch (Exception var3) {
            return null;
        }
    }
    public Boolean isCachePresent(String username) {
        Object object = this.newUserCache.getIfPresent(username);
        return object != null;
    }
    public void clearCache(String username) {
        this.newUserCache.invalidate(username);
    }
}
