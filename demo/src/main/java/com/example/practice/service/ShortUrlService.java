package com.example.practice.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.practice.entity.ShortUrl;
import com.example.practice.repo.ShortUrlRepository;

@Service
public class ShortUrlService {
    private static final int SHORT_URL_VALIDITY_MINUTES = 5;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    public ShortUrl generateShortUrl(String originalUrl) {
        ShortUrl existingUrl = shortUrlRepository.findByOriginalUrl(originalUrl);
        if (existingUrl != null) {
            return existingUrl; // If the URL already exists, return the existing short URL
        }

        String shortUrl = generateUniqueShortUrl(originalUrl);
        ShortUrl newShortUrl = new ShortUrl();
        newShortUrl.setOriginalUrl(originalUrl);
        newShortUrl.setShortUrl(shortUrl);
        newShortUrl.setCreatedAt(LocalDateTime.now());
        shortUrlRepository.save(newShortUrl);
        return newShortUrl;
    }

    public ShortUrl getOriginalUrlByShortUrl(String shortUrl) {
        ShortUrl url = shortUrlRepository.findByShortUrl(shortUrl);
        if (url != null && !isShortUrlExpired(url)) {
            return url;
        } else {
            return null;
        }
    }

    private boolean isShortUrlExpired(ShortUrl url) {
        LocalDateTime expirationTime = url.getCreatedAt().plusMinutes(SHORT_URL_VALIDITY_MINUTES);
        return LocalDateTime.now().isAfter(expirationTime);
    }

    private String generateUniqueShortUrl(String originalUrl) {
    	
    	return originalUrl.substring(12, originalUrl.length()-4); // https://www.google.com
    }
}
