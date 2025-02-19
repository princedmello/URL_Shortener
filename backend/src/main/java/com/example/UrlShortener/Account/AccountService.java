package com.example.UrlShortener.Account;

import com.example.UrlShortener.config.JwtService;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
@Transactional
public class AccountService {

   @Autowired
    private AccountRepository accountRepository;

   @Autowired
   private JwtService jwtService;

   @Autowired
   private AuthenticationManager authenticationManager;

    public List<Account> getAccounts(){
        System.out.println("its here");
        return accountRepository.findAll();
    }

    public String createAccount(Account account) {
        int salt = 10;
        BCryptPasswordEncoder bCryptPasswordEncoder = new
                BCryptPasswordEncoder(salt,new SecureRandom());
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));

        accountRepository.save(account);
        String token =  jwtService.generateToken(account);
        System.out.println(token);
        return token;
    }

    public Optional<Account> getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

//    public String loginAccount(Account account) {
//        try{
//            Optional<Account> userDetails =  accountRepository.findByEmail(account.getEmail());
//            if (userDetails.isPresent()){
//                BCryptPasswordE account.getPassword()
//            }
//
//
//        }
//        catch(Exception e){
//            return "User not found";
//        }
//        return null;
//    }
}
