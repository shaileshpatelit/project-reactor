package com.shailesh.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorityReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * Spring security configuration
     * @param http
     * @return
     */
    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        http
        .csrf().disable() //Disable csrf as we will be testing basic auth and just not meant to be called from own webpage
        .authorizeExchange(
                //URL to roles mapping
                (authorize) -> authorize
                .pathMatchers("/resources/**").permitAll()
                .pathMatchers("/admin/**").hasRole("ADMIN")
                .pathMatchers("/dba/**").access((authentication, context) ->
                        AuthorityReactiveAuthorizationManager.hasRole("ADMIN").check(authentication, context)
                                .filter(decision -> !decision.isGranted())
                                .switchIfEmpty(AuthorityReactiveAuthorizationManager.hasRole("DBA").check(authentication, context)))
                .pathMatchers("/user/**").hasRole("USER")
                .anyExchange().denyAll())
                //Allow basic auth
                .httpBasic()
        ;
        return http.build();
    }

    /**
     * Some hardcoded users for testing, we can connect the user details service with DB to autheticate actual users
     * @return
     */
    @Bean
    public MapReactiveUserDetailsService userDetailsRepository() {
        UserDetails rob = User.withUsername("user").password("user123").roles("USER").build();
        UserDetails admin = User.withUsername("admin").password("admin123").roles("USER","ADMIN").build();
        UserDetails dba = User.withUsername("dba").password("dba123").roles("DBA","ADMIN").build();
        return new MapReactiveUserDetailsService(rob, admin, dba);
    }

    /**
     * Bean for no password encoding (encryption from user details service)
     * @return
     */
    @SuppressWarnings("deprecation")
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}