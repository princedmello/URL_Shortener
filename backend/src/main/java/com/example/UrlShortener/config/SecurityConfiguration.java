package com.example.UrlShortener.config;


import com.example.UrlShortener.OAuth2Config.CustomOidcUserService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    @Autowired
    private Environment env;

    private final CustomOidcUserService customOidcUserService;

    private  final JwtAuthenticationFilter jwtAuthFilter;

    private  final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeHttpRequests((authz) -> authz
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(withDefaults());
//        http.oauth2Login()
//                .defaultSuccessUrl("http://localhost:5050/urls/", true);
//
//        return http.build();
        http.csrf().
                disable().
                authorizeHttpRequests((authz)->
                        authz
//                .requestMatchers("/api/v1/auth/**").hasAnyRole()
                .requestMatchers("/api/v1/**")
                .authenticated()

                .requestMatchers("/**")
                .permitAll()
                )
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .cors()
                .configurationSource(request -> new CorsConfiguration()
                        .applyPermitDefaultValues())
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(customOidcUserService))
                .defaultSuccessUrl("http://localhost:3000/myurls", true));
        ;
        return http.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/api/v1/users/**", "/api/v1/accounts/**",
//                "/api/v1/urls/**");
//    }



}