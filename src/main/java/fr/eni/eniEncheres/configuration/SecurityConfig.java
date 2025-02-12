package fr.eni.eniEncheres.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
 
@Configuration
@EnableWebSecurity
public class SecurityConfig {
 
<<<<<<< HEAD
	
 
	@Bean
	SecurityFilterChain getFilterChain(HttpSecurity security) throws Exception {
	 
	    security.authorizeHttpRequests(auth -> {
	        auth.requestMatchers(HttpMethod.GET, "/css/*", "/images/*").permitAll();
	        auth.requestMatchers("/", "/articles", "/articles/{id}").permitAll();
	        auth.requestMatchers("/inscription", "/connexion").anonymous();
	        auth.requestMatchers("/encheres/**", "/profil/**").authenticated();
	        auth.anyRequest().denyAll(); 
	    });
	 
	    security
	        .formLogin(formLogin -> {
	            formLogin
	                .loginPage("/connexion")
	                .defaultSuccessUrl("/", true);
	        })
	        .logout(logout ->
	            logout
	                .invalidateHttpSession(true)
	                .logoutRequestMatcher(new AntPathRequestMatcher("/deconnexion", "GET"))
	                .logoutSuccessUrl("/")
	        );
	 
	    return security.build();
	}

    
=======
    @Bean
    SecurityFilterChain getFilterChain(HttpSecurity security) throws Exception {
        security.authorizeHttpRequests(auth -> {
            // Configurez d'abord les correspondances spécifiques
            auth.requestMatchers("/css/*", "/images/*").permitAll();
            auth.requestMatchers("/", "/articles", "/inscription", "/connexion").permitAll();
            
            // Protégez les routes qui nécessitent une authentification
            auth.requestMatchers("/profil/**", "/encheres/**", "/articles/new", "/articles/edit/**").authenticated();
            
            // Utilisez anyRequest() en dernier
            auth.anyRequest().authenticated();
        });

        security
            .formLogin(formLogin -> {
                formLogin
                    .loginPage("/connexion")
                    .defaultSuccessUrl("/", true);
            })
            .logout(logout ->
                logout
                    .invalidateHttpSession(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/deconnexion", "GET"))
                    .logoutSuccessUrl("/")
            );
        
        return security.build();
    }

>>>>>>> 452c9cf436e23a67e7e0c007a1ec1bd096db9a7c
    @Bean
    UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager userManager = new JdbcUserDetailsManager(dataSource);

        userManager.setUsersByUsernameQuery(
            "SELECT email AS username, mot_de_passe AS password, 1 AS enabled FROM UTILISATEURS WHERE email = ?"
        );

        userManager.setAuthoritiesByUsernameQuery(
            "SELECT email AS username, 'ROLE_USER' AS authority FROM UTILISATEURS WHERE email = ?"
        );

        return userManager;
    }
}

