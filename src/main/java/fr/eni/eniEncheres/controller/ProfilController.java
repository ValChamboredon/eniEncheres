package fr.eni.eniEncheres.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.eni.eniEncheres.bll.UtilisateurService;
import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.exception.BusinessException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/encheres")
public class ProfilController {
	
	@Autowired
	private final UtilisateurService utilisateurService;
	
	@Autowired
	private MessageSource messageSource;

	public ProfilController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}
		
	@GetMapping("/profil")
	public String afficherProfil(Model model, HttpSession session) {
		//récupérer email de l'utilisateur
		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		String email = authentification.getName();
		System.out.println(email);
		int userId = utilisateurService.getIdByEmail(email);
		
		//récupérer l'utilisateur en base de données
		Utilisateur utilisateur = utilisateurService.getUtilisateurById(userId);
		model.addAttribute("utilisateur", utilisateur);
		
		return "profil";
	}
	
    @PostMapping("/profil/detail")
    public String modifierProfil(
            @Valid @ModelAttribute("utilisateur") Utilisateur utilisateur,
            BindingResult bindingResult,
            Model model,
            @RequestParam("motDePasseNouveau") String motDePasseNouveau,
            @RequestParam("motDePasseConfirme") String motDePasseConfirme) {
        
        // Vérification des erreurs de validation
        if (bindingResult.hasErrors()) {
        	bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Erreur : " + error.getObjectName() + " - " + error.getDefaultMessage());
            });
            return "profil-detail";
        }else {
            try {
                // Vérification de la correspondance des mots de passe
            	 if (motDePasseNouveau != null && !motDePasseNouveau.isEmpty()) {
        	      if (!motDePasseNouveau.equals(motDePasseConfirme)) {
        	          bindingResult.rejectValue("motDePasse", "error.utilisateur", "Les mots de passe ne correspondent pas.");
        	          return "profil-detail";
        	      } else {
        	          utilisateur.setMotDePasse(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(motDePasseNouveau));
        	      	}
            	 }
            	  
            	//modification en bdd
                utilisateurService.modifier(utilisateur);
                
                //modification du contexte
                // Récupérer l'authentification actuelle
                int id = utilisateur.getNoUtilisateur();

                // Récupérer l'utilisateur avec son email après mise à jour
                Utilisateur utilisateurMisAJour = utilisateurService.getUtilisateurById(id);

                // Créer un nouvel objet Authentication avec les informations mises à jour de l'utilisateur
                Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                    utilisateurMisAJour.getEmail(), 
                    utilisateurMisAJour.getMotDePasse()
                );

                // Mettre à jour le SecurityContext avec la nouvelle authentification
                SecurityContextHolder.getContext().setAuthentication(newAuthentication);
                
            } catch (BusinessException e) {
                e.getClesErreurs().forEach(cle -> {
                    String messageErreur = messageSource.getMessage(cle, null, Locale.getDefault());
                    bindingResult.addError(new ObjectError("globalError", messageErreur));
                });
                return "profil-detail";
            }
        }
        
        return "index";
    }
	
	@GetMapping("/profil/detail")
	public String afficherProfilDetail(Model model) {
		//récupérer email de l'utilisateur
		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		String email = authentification.getName();
		int userId = utilisateurService.getIdByEmail(email);
		
		//récupérer l'utilisateur en base de données
		Utilisateur utilisateur = utilisateurService.getUtilisateurById(userId);
		model.addAttribute("utilisateur", utilisateur);
		
		return "profil-detail";
	}
	
	@PostMapping("/profil/detail/supprimer")
	public String supprimerCompte(@RequestParam("noUtilisateur") int id){
		utilisateurService.supprimerById(id);
		return "redirect:/deconnexion";
	}

}