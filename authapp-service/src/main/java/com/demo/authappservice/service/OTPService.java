package com.demo.authappservice.service;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class OTPService {
	private static final Integer EXPIRE_MINS = 15;
	private LoadingCache<String, Integer> OTPCache;

	public OTPService() {
		super();
		OTPCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}

	public void generateOTP(String key) {
		OTPCache.put(key, (int) ((Math.random() * 5000) + 100000));
	}

	public int getOtp(String key) {
		try {
			return OTPCache.get(key);
		} catch (Exception e) {
			return 0;
		}
	}

	public void clearOTP(String key) {
		OTPCache.invalidate(key);
	}
}
