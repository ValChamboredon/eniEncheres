package fr.eni.eniEncheres.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .authorizeHttpRequests((authorize) -> 
                authorize
                    .anyRequest().permitAll()
                    //ajouter permit config plus tard 
                    
            )
            .httpBasic(Customizer.withDefaults())
			.formLogin((formLogin) ->
                formLogin
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
            )
            .logout((logout) ->
            logout
                .invalidateHttpSession(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .logoutSuccessUrl("/")
            )
            ;
        return http.build();
    }}
