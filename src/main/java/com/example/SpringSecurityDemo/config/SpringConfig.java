package com.example.SpringSecurityDemo.config;

import com.example.SpringSecurityDemo.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringConfig {


    private final MyUserDetailsService userDetailService;

    @Autowired
    SpringConfig(MyUserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers("login-page","create-user")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())

                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
         DaoAuthenticationProvider dao =  new DaoAuthenticationProvider();
        dao.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
         dao.setUserDetailsService(userDetailService);

         return dao ;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    

//    @Bean
//    public UserDetailsService userDetailsService() {
//
//                .username("raj")
//                .password("123")
//                .roles("Admin")
//                .build();
//        UserDetails user2= User.withDefaultPasswordEncoder()
//                .username("manoj")
//                .password("123")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1, user2);
//
//
//    }

}
