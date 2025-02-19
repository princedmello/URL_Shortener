package com.example.UrlShortener.Account;

import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository <Account,String> {
    Optional<Account> findByEmail(String email);
}
