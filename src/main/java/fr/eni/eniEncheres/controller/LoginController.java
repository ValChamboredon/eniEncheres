package fr.eni.eniEncheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
	
//	@GetMapping("/encheres")
//	public String connexion(	@RequestParam String username,
//								@RequestParam String password,
//								@RequestParam(required = false) boolean rememberMe,
//								HttpServletResponse response) {
//		Cookie emailCookie = new Cookie("rememberedEmail", username);
//		emailCookie.setMaxAge(7*24*60*60);
//		emailCookie.setPath("/");
//		response.addCookie(emailCookie);
//		System.out.println("Cookie ajouté: " + emailCookie.getName() + " = " + emailCookie.getValue());
//		
//		System.out.println("test");
//
//		
//		return "index";
//	}
//	
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
}
