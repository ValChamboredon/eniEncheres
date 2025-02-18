package fr.eni.eniEncheres.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain getFilterChain(HttpSecurity security) throws Exception {
        security.authorizeHttpRequests(auth -> {
            // Ressources statiques accessibles à tous
            auth.requestMatchers("/css/*", "/images/*").permitAll();
            
            // Pages accessibles sans authentification
            auth.requestMatchers("/", "/encheres", "/articles", "/inscription", "/connexion").permitAll();
            // Pages nécessitant une authentification
            auth.requestMatchers("/profil/**", "/articles/new", "/articles/edit/**").authenticated();
            auth.requestMatchers("/encheres/encherir", "/encheres/article/*/encherir").authenticated();
            // Utilisation de anyRequest() en dernier
            auth.anyRequest().authenticated();
        });

        security.formLogin(formLogin -> {
            formLogin.loginPage("/connexion").defaultSuccessUrl("/encheres", true).permitAll();
        });

        security.logout(logout -> logout.invalidateHttpSession(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/deconnexion", "GET"))
                .logoutSuccessUrl("/encheres")
                .permitAll());

        // Désactivation du CSRF
        security.csrf(csrf -> csrf.disable());

        return security.build();
    }

    @Bean
    UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager userManager = new JdbcUserDetailsManager(dataSource);
        userManager.setUsersByUsernameQuery(
                "SELECT email AS username, mot_de_passe AS password, 1 AS enabled FROM UTILISATEURS WHERE email = ?");
        userManager.setAuthoritiesByUsernameQuery(
                "SELECT email AS username, 'ROLE_USER' AS authority FROM UTILISATEURS WHERE email = ?");
        return userManager;
    }
}
