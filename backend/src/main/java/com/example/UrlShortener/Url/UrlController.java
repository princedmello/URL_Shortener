package com.example.UrlShortener.Url;

import com.example.UrlShortener.Account.Account;
import com.example.UrlShortener.Account.AccountService;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@CrossOrigin()
@RestController
@RequestMapping("/")

public class UrlController {
    @Autowired
    private UrlService urlService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UrlGenerator urlGenerator;

    @GetMapping("/api/v1/urls/")
    public ResponseEntity<?> getAllUrls(){
        try{
            List<Url> urls = urlService.getAllUrls();
            if(urls.size()==0){
                throw new IllegalArgumentException();
            }
            for (Url url:urls) {
                url.setShortUrl("http://ec2-54-201-194-26.us-west-2.compute.amazonaws.com:8080/"+url.getShortUrl());
            }

            return new ResponseEntity<>(urls,HttpStatus.OK);
        }
        catch(IllegalArgumentException i){
            return new ResponseEntity<>("Could not find urls",HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/v1/urls/")
    public ResponseEntity<?> createShortenedUrl(@RequestBody Url url){
        try{
            Optional<Url> existing_url = urlService.getExistingUrl(url.getLongUrl(),url.getEmail());
            if(existing_url.isPresent()){
                existing_url.get()
                        .setShortUrl("http://ec2-54-201-194-26.us-west-2.compute.amazonaws.com:8080/"+
                                existing_url.get().getShortUrl());
                System.out.println(existing_url.get());
                return new ResponseEntity<>(existing_url,HttpStatus.OK);
            }
            Url created_url = urlService.createShortenedUrl(url);
            System.out.println(created_url);
            created_url.setShortUrl("http://ec2-54-201-194-26.us-west-2.compute.amazonaws.com:8080/" + created_url.getShortUrl());
            System.out.println(created_url);
            return new ResponseEntity<>(created_url,HttpStatus.CREATED);
        }
        catch(DataIntegrityViolationException d){
            return  new ResponseEntity<>(" Long URL value too long, need to lower the URL value!", HttpStatusCode.valueOf(HTTPResponse.SC_BAD_REQUEST));

        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{shortUrl}")
    public RedirectView redirectShortenedURLtoLongURL(@PathVariable String shortUrl, Model model){
        try {

            Optional<Url> url = urlService.getShortenedUrl(shortUrl);
            if (url.isPresent()) {
                String long_url = url.get().getLongUrl();
                return new RedirectView(long_url);
            }
            return new RedirectView();

        }

        catch(Exception e){
            System.out.println(e);
            return new RedirectView("/api/v1/urls/");
        }
    }

    @GetMapping("/api/v1/urls/all-urls/{email}")
    public ResponseEntity<?> getUrlsByEmail(@PathVariable String email) {
        try {
            Optional<Account> account = accountService.getAccountByEmail(email);
            if (account.isEmpty()) {
                throw new UsernameNotFoundException("User email not found");
            }
            Optional<List<Url>> urls = urlService.getUrlsByEmail(email);
            if (urls.isPresent() && (urls.get().size() != 0)) {
                for (Url url : urls.get()) {
                    url.setShortUrl("http://ec2-54-201-194-26.us-west-2.compute.amazonaws.com:8080/" + url.getShortUrl());
                }
                return new ResponseEntity<>(urls, HttpStatus.OK);
            } else if (urls.get().size() == 0) {
                throw new IllegalArgumentException("No urls found by the user!");
            }
            throw new Exception();

        } catch (UsernameNotFoundException u) {
            return new ResponseEntity<>(u.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (IllegalArgumentException i) {
            return new ResponseEntity<>(i.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
