package com.example.UrlShortener.Url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UrlGenerator {

    @Autowired
    private UrlService urlService;


    private static int SHORT_URL_CHAR_SIZE=7;
    public static String convert(String longURL) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(longURL.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(Integer.toHexString(0xFF & b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public String generateRandomShortUrl(String longURL) {
        String hash=UrlGenerator.convert(longURL);
        int numberOfCharsInHash=hash.length();
        int counter=0;
        while(counter < numberOfCharsInHash-SHORT_URL_CHAR_SIZE){
            Optional<Url> url = urlService
                    .getShortenedUrl
                            (hash.substring(counter, counter+SHORT_URL_CHAR_SIZE));

            if(!url.isPresent()){
                {
                    return hash.substring(counter, counter+SHORT_URL_CHAR_SIZE);

                }
            }
            counter++;
        }
        return hash;
    }

}