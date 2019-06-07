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
import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.CUSTOMER_USER;
import static com.gmail.eugene.shchemelyov.market.service.constant.SecurityConstant.SALE_USER;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

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
                .antMatchers(
                        "/private/users/**",
                        "/private/reviews",
                        "/private/reviews/*",
                        "/private/reviews/*/delete")
                .hasAuthority(ADMINISTRATOR)
                .antMatchers(
                        "/private/profile/**",
                        "/private/customer/orders/**",
                        "/private/reviews/review/new"
                )
                .hasAuthority(CUSTOMER_USER)
                .antMatchers(
                        "/private/articles",
                        "/private/articles/*",
                        "/private/items",
                        "/private/items/*"
                )
                .hasAnyAuthority(CUSTOMER_USER, SALE_USER)
                .antMatchers(
                        "/private/comments/**",
                        "/private/articles/*/*",
                        "/private/items/*/*",
                        "/private/seller/orders/**",
                        "/private/upload"
                )
                .hasAuthority(SALE_USER)
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

