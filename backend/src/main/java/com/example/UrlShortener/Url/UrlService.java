package com.example.UrlShortener.Url;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UrlGenerator urlGenerator;

    public List<Url> getAllUrls(){
        List<Url> urls =  urlRepository.findAll();
        return urls;
    }

    public Optional<Url> getShortenedUrl(String shortUrl){
        return urlRepository.findByShortUrl(shortUrl);
    }

    public Url createShortenedUrl(Url url){
        String ans =  urlGenerator.generateRandomShortUrl(url.getLongUrl());
        url.setShortUrl(ans);
        url.setCreated_date(new Date());
        System.out.println(url);
        return urlRepository.save(url);
    }

    public Optional<List<Url>> getUrlsByEmail(String email){
        return urlRepository.findByEmail(email);
    }

    public Optional<Url> getExistingUrl(String longUrl,String email){
        return urlRepository.findByLongUrlAndEmail(longUrl,email);
    }

}
