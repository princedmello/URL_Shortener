package com.example.UrlShortener.Account;

import com.example.UrlShortener.User.User;
import com.example.UrlShortener.User.UserService;
import com.example.UrlShortener.config.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtService jwtService;


    @Autowired
    private UserService userService;
    @GetMapping("/")
    public ResponseEntity<?> getAccounts(){
        try{
            List<Account> accounts = accountService.getAccounts();
            if (accounts.size()>0) {
                return new ResponseEntity<>(accounts, HttpStatus.OK);
            }
            else{
                throw new IllegalArgumentException("Do not have Accounts");
            }
        }
        catch(IllegalArgumentException i){
            return new ResponseEntity<>(i.getMessage(),HttpStatus.BAD_REQUEST);

        }
        catch(Exception e){
            return new
                    ResponseEntity<>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> createAccount(@RequestBody Account account){
        try{
            Optional<Account> existing_account =
                    accountService.getAccountByEmail(account.getEmail());
            System.out.println(existing_account);
            if (!existing_account.isPresent()){
                String token =  accountService.createAccount(account);
                System.out.println(token);
                String email = account.getEmail();
                User new_user = userService.createUser(new User(account.getEmail()));
                return new ResponseEntity<>(token,HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>("Email address already used!!",HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginAccount(@RequestBody Account account){
        try {
            Optional<Account> existing_account = accountService.getAccountByEmail(account.getEmail());
            if(!existing_account.isPresent()){
                throw new  UsernameNotFoundException("User Email not found");

            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean isPasswordMatches = encoder.matches(account.getPassword(),
                    existing_account.get().getPassword());
            if(isPasswordMatches){
                String jwtToken = jwtService.generateToken(existing_account.get());
                return new ResponseEntity<>(jwtToken,HttpStatus.OK);
            }
            else{
                throw new BadCredentialsException("Password does not match!");
            }
        }
        catch(UsernameNotFoundException u){
            return new ResponseEntity<>(u.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch(BadCredentialsException b){
            return new ResponseEntity<>(b.getMessage(),HttpStatus.UNAUTHORIZED);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }

    @GetMapping("/get-account/{email}")
    public ResponseEntity<?> getAccountByEmail(@PathVariable String email){
        try{
            Optional<Account> account = accountService.getAccountByEmail(email);

            if(account.isPresent()) {
                return new ResponseEntity<>(account, HttpStatus.OK);
            }
            else{
                throw new UsernameNotFoundException("User email not found!");
            }
        }
        catch(UsernameNotFoundException u){
            return  new ResponseEntity<>(u.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping("/get-jwt-token")
    public ResponseEntity<?> getJwtToken(@RequestBody Account account) {
        try {
            Optional<Account> existing_account = accountService.getAccountByEmail(account.getEmail());

            if (existing_account.isPresent()) {
                String token = jwtService.generateToken(existing_account.get());
                MyResponse myResponse = new MyResponse(token, account.getEmail());
                return new ResponseEntity<>(myResponse, HttpStatus.OK);
            } else {
                String token = accountService.createAccount(account);
                MyResponse myResponse = new MyResponse(token, account.getEmail());
                return new ResponseEntity<>(myResponse, HttpStatus.CREATED);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>("Could not generate jwt token", HttpStatus.BAD_REQUEST);

        }
    }




}
