package com.example.UrlShortener.User;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    private String email;
    @Nullable
    private String iD;

    public User(String email) {
        this.email = email;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        this.iD = id;
    }
    public String getId() {
        return iD;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
