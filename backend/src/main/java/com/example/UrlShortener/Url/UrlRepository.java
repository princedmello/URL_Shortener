package com.example.UrlShortener.Url;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UrlRepository extends JpaRepository<Url, UUID> {
    Optional<Url> findByShortUrl(String url);
    Optional<List<Url>> findByEmail(String email);
    Optional<Url> findByLongUrlAndEmail(String url, String email);
}
