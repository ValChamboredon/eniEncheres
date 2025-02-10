package fr.eni.eniEncheres.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import fr.eni.eniEncheres.bll.UtilisateurService;
import fr.eni.eniEncheres.bo.Utilisateur;
import jakarta.validation.Valid;


@Controller
public class InscriptionController {
	
	@Autowired
	private UtilisateurService utilisateurService;

	public InscriptionController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}
	
	@GetMapping ("/inscription")
	public String afficherFormulaireInscription(Model model) {
		model.addAttribute("utilisateur", new Utilisateur());
		return "inscription";
	}
	
	@PostMapping("/inscription")
	public String inscrireUtilisateur(@Valid Utilisateur utilisateur, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "inscription";
		}
		if(utilisateurService.pseudoExistant(utilisateur.getPseudo())) {
			model.addAttribute("erreur", "Le pseudo est déjà utilisé.");
			return "inscription";
		}
		if(utilisateurService.emailExistant(utilisateur.getEmail())) {
			model.addAttribute("erreur", "L'e-mail est déjà utilisé.");
			return "inscription";
		}
		utilisateurService.enregistrer(utilisateur);
		return "redirect:/connexion";
	}
	

}


