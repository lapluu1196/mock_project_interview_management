package com.team3.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        userDetailsManager.setUsersByUsernameQuery("select username, password, " +
                "CASE WHEN status = 'Active' THEN 1 ELSE 0 END as enabled from Users where username=?");
        userDetailsManager.setAuthoritiesByUsernameQuery("select username, role as authority from Users where username=?");

        return userDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http. csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(configurer ->
                        configurer.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                                .requestMatchers("/auth/password/**").permitAll()
                                .requestMatchers("/**").permitAll()
                                .anyRequest().authenticated())
                .formLogin(login ->
                        login.loginPage("/auth/login")
                                .loginProcessingUrl("/authenticateUser")
                                .permitAll())
                .rememberMe(rememberMe -> rememberMe
                        .tokenValiditySeconds(86400)
                        .key("uniqueAndSecret")
                        .rememberMeParameter("rememberMe"))
                .logout(logout ->
                        logout.logoutUrl("/logout")
                                .logoutSuccessUrl("/auth/login?logout")
                                .permitAll())
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied")
                );

        return http.build();
    }
}
