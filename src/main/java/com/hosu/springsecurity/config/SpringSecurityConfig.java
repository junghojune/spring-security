package com.hosu.springsecurity.config;

import com.hosu.springsecurity.user.User;
import com.hosu.springsecurity.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security 설정 Config
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig  {

    private final UserService userService;

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception{

        http.csrf();
        http.httpBasic().disable();
        http.rememberMe();
        http.formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll();
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/");
        http.authorizeHttpRequests()
                .requestMatchers("/", "/home", "/signup").permitAll()
                .requestMatchers("/note").hasRole("USER")
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/notice").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/notice").hasRole("ADMIN")
                .anyRequest().authenticated();
        return http.build();
    }



    public void configure(WebSecurity web) {
        // 정적 리소스 spring security 대상에서 제외
//        web.ignoring().antMatchers("/images/**", "/css/**"); // 아래 코드와 같은 코드입니다.
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * UserDetailsService 구현
     *
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException(username);
            }
            return user;
        };
    }
}
