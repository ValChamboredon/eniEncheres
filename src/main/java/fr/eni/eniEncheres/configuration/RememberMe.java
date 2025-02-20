package fr.eni.eniEncheres.configuration;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RememberMe implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Récupérer les valeurs du formulaire
        String username = request.getParameter("username");
        String password = request.getParameter("password"); 

        if (username != null) {
            Cookie emailCookie = new Cookie("rememberedEmail", username);
            emailCookie.setMaxAge(7 * 24 * 60 * 60);  // Expiration après 7 jours
            emailCookie.setPath("/");  // Le cookie est valable sur toute l'application
            response.addCookie(emailCookie);
        }
    	
    	response.sendRedirect("/encheres");
    }
}