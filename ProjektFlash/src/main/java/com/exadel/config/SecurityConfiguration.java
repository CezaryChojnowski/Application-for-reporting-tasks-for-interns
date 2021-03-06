package com.exadel.config;

import com.exadel.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableConfigurationProperties
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

    @Autowired
    PersistentTokenRepository persistentTokenRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/logout").permitAll()
                .antMatchers("/forgotPassword").permitAll()
                .antMatchers("/reset").permitAll()
                .antMatchers("/accessDenied").permitAll()
                .antMatchers("/**").hasAnyAuthority("admin","intern")
                .anyRequest()
                .authenticated()
                .and().csrf().disable()
                .formLogin().successHandler(customizeAuthenticationSuccessHandler)
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").and().exceptionHandling()
                .and().exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository)
                .and().exceptionHandling().accessDeniedPage("/accessDenied");
    }

        @Override
        public void configure(AuthenticationManagerBuilder builder) throws Exception {
            builder
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(bCryptPasswordEncoder);
}

}
