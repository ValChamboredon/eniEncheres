package fr.eni.eniEncheres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnchereController {
	
	@GetMapping("/profil")
	public String showProfil() {
		return "profil";
	}

}
