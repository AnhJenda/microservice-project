package com.example.authservice.security.config;

import com.example.authservice.security.filter.AuTokenFilter;
import com.example.authservice.security.filter.AuthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
    @author: Dinh Quang Anh
    Date   : 8/4/2023
    Project: spring_school_api
*/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPoint unauthorizedHandler;

    @Bean
    public AuTokenFilter authenticationJwtTokenFilter() {
        return new AuTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/v1/login**", "/api/v1/signup**", "/api/v1/verify-email**").permitAll()
                .antMatchers(HttpMethod.GET
                        , "/api/v1/products/product"
                        , "/api/v1/accessories/accessory"
                        , "/api/v1/achivements/achivement"
                        , "/api/v1/coaches/coach"
                        , "/api/v1/football-teams/football-team"
                        , "/api/v1/product-images/product-image"
                        , "/api/v1/product-sizes/product-size"
                        , "/api/v1/schedules/schedule"
                        , "/api/v1/sizes/size"
                        , "/api/v1/tournaments/tournament"
                ).permitAll()
                .antMatchers(HttpMethod.POST
                        , "/api/v1/accessories/"
                        , "/api/v1/achivements/"
                        , "/api/v1/coaches/"
                        , "/api/v1/football-teams/"
                        , "/api/v1/newses/"
                        , "/api/v1/products/"
                        , "/api/v1/product-images/"
                        , "/api/v1/product-sizes/"
                        , "/api/v1/schedules/"
                        , "/api/v1/sizes/"
                        ,"/api/v1/playeres/"
                ).permitAll()
                .antMatchers(HttpMethod.POST
                        , "/api/v1/accessories/**"
                        , "/api/v1/achivements/**"
                        , "/api/v1/coaches/**"
                        , "/api/v1/football-teams/"
                        , "/api/v1/newses/**"
                        , "/api/v1/products/**"
                        , "/api/v1/product-images/"
                        , "/api/v1/product-sizes/"
                        , "/api/v1/schedules/**"
                        , "/api/v1/sizes/"
                        , "/api/v1/tournaments/"
                        ,"/api/v1/playeres/**"
                ).hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST
                        , "/api/v1/cart"
                ).hasAuthority("USER")
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
///login**", "/api/v1/signup**"