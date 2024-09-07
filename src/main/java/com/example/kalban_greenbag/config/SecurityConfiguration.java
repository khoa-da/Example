package com.example.kalban_greenbag.config;

import com.example.kalban_greenbag.exception.GlobalExceptionHandler;
import com.example.kalban_greenbag.service.impl.JWTServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration implements WebMvcConfigurer {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final GlobalExceptionHandler globalExceptionHandler;
//    private final JWTServiceImpl jwtService;
//
//    public SecurityConfiguration(JWTServiceImpl jwtService) {
//        this.jwtService = jwtService;
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request -> request.anyRequest().permitAll()
                .authorizeHttpRequests(request -> request.requestMatchers("/api/v1/auth/**",
                                "/api/v1/account/create",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "swagger-resources/**",
                                "/v3/api-docs/**",
                                "webjars/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handling -> handling
                        .accessDeniedHandler(globalExceptionHandler::handleAccessDeniedException)
                        .authenticationEntryPoint(globalExceptionHandler::handleAuthenticationException)
                );
        return http.build();
    }


//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable);
//        http.authorizeHttpRequests(request -> request.requestMatchers("/api/v1/auth/**",
//                                "/api/v1/account/create",
//                                "/swagger-ui/**",
//                                "/swagger-ui.html",
//                                "swagger-resources/**",
//                                "/v3/api-docs/**",
//                                "webjars/**")
//                        .permitAll()
//                        .anyRequest().authenticated());
//        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())));
//        return http.build();
//    }
//    @Bean
//    JwtDecoder jwtDecoder() {
//        SecretKeySpec secretKeySpec = new SecretKeySpec(jwtService.getSigninKey().getEncoded(), "HmacSHA256");
//        return NimbusJwtDecoder.withSecretKey(secretKeySpec).build();
//    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable())
//                .exceptionHandling(handling -> handling
//                        .accessDeniedHandler(globalExceptionHandler::handleAccessDeniedException)
//                        .authenticationEntryPoint(globalExceptionHandler::handleAuthenticationException))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth ->
//                        auth.requestMatchers("/api/v1/auth/**",
//                                "/api/v1/account/create",
//                                "/swagger-ui/**",
//                                "/swagger-ui.html",
//                                "swagger-resources/**",
//                                "/v3/api-docs/**",
//                                "webjars/**").permitAll()
//                                .anyRequest().authenticated()
//                );
//
//        http.authenticationProvider(authenticationProvider);
//
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")  // specify your front-end origin
                .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS", "HEAD")
                .allowedHeaders("*")
                .exposedHeaders("X-Get-Header");
    }
}