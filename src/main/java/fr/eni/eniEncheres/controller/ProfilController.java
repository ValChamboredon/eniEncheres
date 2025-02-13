package fr.eni.eniEncheres.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import fr.eni.eniEncheres.bll.UtilisateurService;
import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.exception.BusinessException;
import jakarta.validation.Valid;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
public class ProfilController {
	
	@Autowired
	private final UtilisateurService utilisateurService;
	
	@Autowired
	private MessageSource messageSource;

	public ProfilController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}
		
	@GetMapping("/profil")
	public String afficherProfil(Model model) {
		//récupérer email de l'utilisateur
		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		String email = authentification.getName();
		
		//récupérer l'utilisateur en base de données
		Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(email);
		model.addAttribute("utilisateur", utilisateur);
		
		return "profil";
	}
	
	@PostMapping("/profil/detail")
	public String modifierProfil(
		@RequestParam(value = "motDePasseNouveau", required = false) String motDePasseNouveau,
		@RequestParam(value = "motDePasseConfirme", required = false) String motDePasseConfirme,
		@Valid @ModelAttribute("utilisateur") Utilisateur utilisateur, 
        BindingResult bindingResult,
	    Model model) {

	    // Créer une instance de BusinessException pour collecter les erreurs
	    BusinessException exception = new BusinessException();
	    
	    if(bindingResult.hasErrors()) {
	    	System.out.println("erreur");
	    	
	    	bindingResult.getAllErrors().forEach(error -> {
	            System.out.println("Erreur : " + error.getObjectName() + " - " + error.getDefaultMessage());
	        });
	    	
	    	return "profil-detail";
	    }
	    
	    try {
	    	utilisateurService.modifier(utilisateur);
	    }catch (BusinessException e) {
	    	e.printStackTrace();
	    	e.getClesErreurs().forEach(cle->{
	    		ObjectError erreur = new ObjectError("globalError", cle);
	    		bindingResult.addError(erreur);
	    	});
	    	
	    	
	    	return "profil-detail";
	    	}
	    
	    // Si un nouveau mot de passe est fourni, vérifier s'il correspond à la confirmation
	    if (motDePasseNouveau != null && !motDePasseNouveau.isEmpty()) {
	        if (!motDePasseNouveau.equals(motDePasseConfirme)) {
	            exception.addCleErreur("Les mots de passe ne correspondent pas.");
	        } else {
	            // Encoder et changer le mot de passe
	            utilisateur.setMotDePasse(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(motDePasseNouveau));
	        }
	    }

	    return "profil";  // Retourner à la page profil après modification
	}


	
	@GetMapping("/profil/detail")
	public String afficherProfilDetail(Model model) {
		//récupérer email de l'utilisateur
		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		String email = authentification.getName();
		
		//récupérer l'utilisateur en base de données
		Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(email);
		model.addAttribute("utilisateur", utilisateur);
		
		return "profil-detail";
	}
	
	@PostMapping("/profil/detail/supprimer")
	public String supprimerCompte(@RequestParam("email") String email){
		utilisateurService.supprimerByEmail(email);
		return "redirect:/deconnexion";
	}

}
