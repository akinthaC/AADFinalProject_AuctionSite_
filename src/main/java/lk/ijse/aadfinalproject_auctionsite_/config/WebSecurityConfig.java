package lk.ijse.aadfinalproject_auctionsite_.config;


import lk.ijse.aadfinalproject_auctionsite_.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JwtFilter jwtFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/authenticate",
                                "/api/v1/user/register",
                                "/api/v1/user//getUserByEmail",
                                "/api/v1/user//getUserById",
                                "/api/v1/AddToCart/**",
                                "/api/v1/Payment/**",
                                "/payhere/**",
                                "/api/v1/Inquiry/send",
                                "/api/v1/Purchase/**",
                                "/api/v1/Purchase/orders-by-date",
                                "/api/v1/Purchase/latestPurchaseId",
                                "/api/v1/PlaceOrder/**",
                                "/api/v1/PlaceOrder/getAllWin/**",
                                "/api/v1/WatchItem/**",
                                "/api/v1/Delivery/**",
                                "/api/v1/Delivery/Shipment/**",
                                "/api/v1/AddToCart/getByid/**",
                                "/api/v1/WatchItem/getByid/**",
                                "/api/listings",
                                "/api/listings/Vehicle/**",
                                "/api/listings/Vehicle/getById/**",
                                "/api/listings/Land",
                                "/api/listings/Land/**",
                                "/api/listings/Land/getById/**",
                                "/api/listings/Delete/**",
                                "/api/listings/update/**",
                                "/api/listings/getAll",
                                "/api/listings/active",
                                "/api/listings/pending",
                                "/api/listings/getById/",
                                "/api/listings/getById/**",
                                "/api/listings/pending-auction-items",
                                "/api/listings/ended-auction-items",
                                "/api/listings/active-auction-items",
                                "/api/car-listings",
                                "/api/v1/user/details",
                                "/api/v1/user/users-sellers-buyers",
                                "/api/v1/user/update-profile/**",
                                "/api/v1/user/**",
                                "/api/v1/admin/**",
                                "/api/v1/ToDo/**",
                                "/api/v1/auth/refreshToken",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }



}
