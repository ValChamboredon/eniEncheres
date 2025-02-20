package fr.eni.eniEncheres.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.eni.eniEncheres.bll.UtilisateurService;
import fr.eni.eniEncheres.bo.MotDePasseDTO;
import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.exception.BusinessException;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;



@Controller
public class LoginController {
	
	@Autowired
	private UtilisateurService utilisateurService;
	
	@Autowired
	private MessageSource messageSource;
	
    @GetMapping("/connexion")
    public String login(Model model, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
            	if("rememberedEmail".equals(cookie.getName())) {
            		model.addAttribute("rememberedEmail", cookie.getValue());
            	}
            }
        }
        return "Connexion";
    }
    
    @GetMapping("/motdepasseoublie")
    public String afficherChangerMotDePasse(){
        return "mot-de-passe-oublie";  
    }
        
    @PostMapping("/motdepasseoublie")
    public String verifierEmail(@RequestParam("email") String email, Model model) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(email);
        
        if (utilisateur != null) {
            model.addAttribute("email", email);
            return "nouveau-mot-de-passe";
        }
        
        return "redirect:/motdepasseoublie?error=email_not_found";
    }
    
    @GetMapping("/motdepasseoublie/reset")
    public String afficherNouveauMotDePasse(){
        return "nouveau-mot-de-passe";  
    }

    @PostMapping("/motdepasseoublie/reset")
    public String resetMotDePasse(@RequestParam("email") String email, @RequestParam("nouveauMotDePasse") String nouveauMotDePasse) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(email);
        
        if (utilisateur != null) {
            utilisateur.setMotDePasse(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(nouveauMotDePasse));
			try {
				utilisateurService.modifier(utilisateur);
				System.out.println("test");
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return "redirect:/connexion";
        }
        
        return "redirect:/motdepasseoublie/reset";
    }
}
