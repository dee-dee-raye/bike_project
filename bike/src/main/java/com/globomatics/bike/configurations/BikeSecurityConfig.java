package com.globomatics.bike.configurations;

import com.globomatics.bike.filters.JWTRequestFilter;
import com.globomatics.bike.services.BikeUserDetailsContextMapper;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import sun.security.provider.MD5;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
//This tells Spring to start looking at this class and see that it is
//supplying the specific pieces for web security. This allows us to override Spring's
//defaults and wire in our own.
@EnableWebSecurity
public class BikeSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BikeUserDetailsContextMapper ctxMapper;

    @Autowired
    private JWTRequestFilter jwtRequestFilter;

    @Value("${ldap.urls:ldap://127.0.0.1:389/dc=globomatics,dc=com}")
    private String ldapUrls;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                //Cross Site Request Forgery (csrf) deals with requests when the browser
                //automatically provides authentication (cookies, basic authentication) and since we are
                //using JWT we are not relying on the browser to authenticate us anyway.
                .csrf().disable()

                //We need to enable CORS support in order to use @CrossOrigin annotations
                .cors().and()
//                .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())

//                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/login").permitAll()
                .antMatchers("/api/v1/logout").permitAll()
                .anyRequest().authenticated()

                //We do not want Spring to manage our state, because we our goal is to be stateless
                //and use the JWT each request to authenticate.
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //Since Spring is not managing our state there needs to be something that runs each time
        //to set our security context. This will run our filter each time before the username password
        //authentication filter is run.
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource()
                .url("ldap://localhost:389/dc=pluralsight,dc=com")
                .and()
                .passwordCompare()
                .passwordEncoder(passwordEncoder())
                .passwordAttribute("userPassword")
                .and()
                .userDetailsContextMapper(ctxMapper);
    }

    // We need to make this a Bean so we can access it from our login controller
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

 class plainTextPassword implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        System.out.println(charSequence);
        return (String) charSequence;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        System.out.println(charSequence);
        System.out.println(s);
        return true;
    }
}
