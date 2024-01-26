package cz.jobs.ppro.security;

import cz.jobs.ppro.service.UserService;
import cz.jobs.ppro.service.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                //.requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")
                                .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                                .requestMatchers("/user/**", "/dashboard/**", "/jobs/**", "/profile/**").hasAnyRole("SEEKER", "MANAGER", "ADMIN")
                                .requestMatchers("/add_job/**").hasAnyRole("MANAGER", "ADMIN")
                                .requestMatchers("/my_jobs/**", "/download-cv/**").hasAnyRole("MANAGER", "ADMIN")
                                .requestMatchers("/cv/**").hasAnyRole("SEEKER")
                                .requestMatchers("/login/**", "/register/**", "/all_jobs/**").permitAll()
                                .anyRequest().authenticated())
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .permitAll()
                                .defaultSuccessUrl("/dashboard")
                                .successHandler(new AuthenticationSuccessHandler() {
                                    @Override
                                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                        response.sendRedirect("/dashboard");
                                    }
                                })
                )
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authenticationManager(authenticationManager);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

}
