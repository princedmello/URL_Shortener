package com.example.UrlShortener.config;

import com.example.UrlShortener.Account.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    public static final String SECRET_KEY="14709625245cmprt3nttq7461f7o8s12rvpgpnsd7iappsgoogleusercontentcom";

    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    //EXTRACT A SINGLE TOKEN

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Check whether token is valid
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token,Claims::getExpiration).before(new Date());
    }


    //Generate a token without extra claims
    public String generateToken(UserDetails userdetails){
        return generateToken(new HashMap<>(),userdetails);
    }
    //Generate a token
    public String generateToken(
            Map<String,Object> extraClaims,
            UserDetails userDetails
    ) {
        try {
            String token = Jwts.builder()
                .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000000 * 60 * 60 * 24))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        }
        catch(Exception e){
            System.out.println(e);
            return "Exception";
        }
    }

    //extract claims from a token

    public Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);

    }
}
