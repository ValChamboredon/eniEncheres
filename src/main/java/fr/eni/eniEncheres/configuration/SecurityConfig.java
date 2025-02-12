package fr.eni.eniEncheres.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
    //Bean Crée un objet réutilisable pour encoder/décoder les mots de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
    @Bean 
    /**
     * UserDetailsManager : Gère l'authentification via la base de données
     * DataSource : Connexion à la base de données SQL 
     */
    UserDetailsManager users(DataSource dataSource) {
    	
        JdbcUserDetailsManager userManager = new JdbcUserDetailsManager(dataSource);

        userManager.setUsersByUsernameQuery(
            "SELECT email AS username, mot_de_passe AS password, 1 AS enabled FROM UTILISATEURS WHERE email = ?"
           /**
            *  cette requête  récupère l'utilisateur en fonction de son email, en renvoyant :
			* email → username
			* mot_de_passe → password
			* 1 → enabled (compte toujours actif)
			*/
        );
        
        userManager.setAuthoritiesByUsernameQuery(
            "SELECT email AS username, 'ROLE_USER' AS authority FROM UTILISATEURS WHERE email = ?"
        	/**
        	 * Chaque utilisateur connecté a le rôle ROLE_USER, 
        	 * nécessaire pour accéder aux pages sécurisées.
        	 */
        		
        );

        return userManager;
    }
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/connexion", "/inscription", "/css/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
            		.loginPage("/connexion") // URL de la page de connexion personnalisée
                    .defaultSuccessUrl("/", true) // Redirection vers la page d'accueil après connexion
                    .failureUrl("/connexion?error=true") // En cas d'échec, renvoie sur la page de connexion avec une erreur
                    .permitAll()
            )
            .logout(logout -> logout
            		.logoutUrl("/deconnexion") // URL pour se déconnecter
                    .logoutSuccessUrl("/") // Après la déconnexion, retour à la page d'accueil
                    .permitAll()
            );

        return http.build();
    }
    
}
