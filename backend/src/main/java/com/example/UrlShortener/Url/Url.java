package com.example.UrlShortener.Url;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Table(name="urls")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Url {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String email;

    private String shortUrl;
    private String longUrl;

    private Date created_date;


    public Url(String shortUrl, String longUrl){
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl(){
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getEmail
            () {
        return email;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Url url = (Url) o;
        return Objects.equals(shortUrl, url.shortUrl) && Objects.equals(longUrl, url.longUrl) && Objects.equals(created_date, url.created_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortUrl, longUrl, created_date);
    }

    @Override
    public String toString() {
        return "Urls{" +
                "shortUrl='" + shortUrl + '\'' +
                ", longUrl='" + longUrl + '\'' +
                ", created_date=" + created_date +
                '}';
    }
}
