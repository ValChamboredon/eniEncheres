package fr.eni.eniEncheres.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.eni.eniEncheres.bll.UtilisateurService;
import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.exception.BusinessException;
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
	public String inscrireUtilisateur(@Valid @ModelAttribute("utilisateur") Utilisateur utilisateur, 
	                                  BindingResult result, 
	                                  Model model) {

	    if (result.hasErrors()) {
	        System.out.println("Formulaire invalide.");
	        result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
	        return "inscription";
	    }

	    try {
	        if (utilisateurService.pseudoExistant(utilisateur.getPseudo())) {
	            model.addAttribute("erreurPseudo", "Ce pseudo est déjà pris.");
	            return "inscription";
	        }

	        if (utilisateurService.emailExistant(utilisateur.getEmail())) {
	            model.addAttribute("erreurEmail", "Cet email est déjà utilisé.");
	            return "inscription";
	        }

	        utilisateurService.enregistrer(utilisateur);
	    } catch (BusinessException e) {
	        model.addAttribute("erreurs", e.getClesErreurs());
	        model.addAttribute("utilisateur", utilisateur);
	        return "inscription";
	    }

	    return "redirect:/connexion"; // Redirect user to login after successful signup
	}








	
	

	

}

