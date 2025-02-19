package com.example.UrlShortener.OAuth2Config;

import java.util.Map;

public class OAuth2ClientUserInfo {

    private Map<String, Object> attributes;

    public OAuth2ClientUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return (String) attributes.get("sub");
    }

    public String getName() {
        return (String) attributes.get("name");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String toString() {
        return "OAuth2ClientUserInfo{" +
                "attributes=" + attributes +
                '}';
    }
}