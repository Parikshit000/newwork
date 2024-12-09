package com.example.newwork.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests((requests) -> requests

            .requestMatchers("/registration/**").permitAll()
            .requestMatchers("/login/**").permitAll()

            // For admin we have to set role in database like
            // we have to create user roles in database
            // insert into roles (1,"ROLE_ADMIN");
            // insert into roles (2,"ROLE_USER");
            // insert into user_roles(Role_id,User_id)values(1, _YourUserId_ );

            .requestMatchers("/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
            .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")

            .anyRequest().authenticated())


        .formLogin((form) -> form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/")
            .permitAll())
        .logout(obj -> obj.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true).deleteCookies("JESSIONID"));
    http.exceptionHandling((hi) -> hi.accessDeniedPage("/accessDenied"));
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().requestMatchers("/");
  }
}