package com.gmail.eugene.shchemelyov.market.web.config.security;

import com.gmail.eugene.shchemelyov.market.web.config.security.handler.AppUrlAuthenticationSuccessHandler;
import com.gmail.eugene.shchemelyov.market.web.config.security.handler.LoginAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.ADMINISTRATOR;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfigurer(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new LoginAccessDeniedHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AppUrlAuthenticationSuccessHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/private/administrator/**")
                .hasAuthority(ADMINISTRATOR)
                .antMatchers("/", "/403", "/login", "/public/**")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(authenticationSuccessHandler())
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .csrf()
                .disable();
    }
}

