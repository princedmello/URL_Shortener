package com.example.UrlShortener.OAuth2Config;

import com.example.UrlShortener.User.User;
import com.example.UrlShortener.User.UserRepository;
import com.example.UrlShortener.User.UserService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private UserService userService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        System.out.println(oidcUser);

        try {
            return processOidcUser(userRequest, oidcUser);
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) {
        OAuth2ClientUserInfo userInfo = new OAuth2ClientUserInfo(oidcUser.getAttributes());
//        System.out.println(userRequest.getAccessToken().g());
        
        Optional<User> userOptional = userService.findUserByEmail(userInfo.getEmail());
        if (!userOptional.isPresent()) {
            User user = new User();
            user.setEmail(userInfo.getEmail());
            // set other needed data
            user.setId(userInfo.getId());

            userService.createUser(user);
        }

        return oidcUser;
    }
}