package fr.eni.eniEncheres.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import fr.eni.eniEncheres.bll.contexte.ContexteService;
import fr.eni.eniEncheres.bo.Utilisateur;
import jakarta.validation.Valid;


@Controller
public class InscriptionController {
	
	@Autowired
	private ContexteService contexteService;

	public InscriptionController(ContexteService contexteService) {
		this.contexteService = contexteService;
	}
	
	@GetMapping ("/inscription")
	public String afficherFormulaireInscription(Model model) {
		model.addAttribute("utilisateur", new Utilisateur());
		return "inscription";
	}
	
	@PostMapping("/inscription")
	public String inscrireUtilisateur(@Valid Utlisateur utilisateur, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return
		}
	}
	

}

//En tant qu’utilisateur, je peux m’inscrire sur la plateforme Enchères.org.
//Le pseudo doit être unique sur toute la plateforme, ainsi que l’email. 
//Le pseudo n’accepte que des caractères alphanumériques. 
//Si la création du profil est validée, l’utilisateur est dirigé vers la page d’accueil (liste des enchères).
//Le crédit initial est de 0
