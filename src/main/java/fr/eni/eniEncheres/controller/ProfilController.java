package fr.eni.eniEncheres.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import fr.eni.eniEncheres.bll.UtilisateurService;
import fr.eni.eniEncheres.bo.Utilisateur;

@Controller
public class ProfilController {
	
	private final UtilisateurService utilisateurService;

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
		
		System.out.println(utilisateur);
		
		return "profil";
	}
	
	@PostMapping("/profil")
	public String modifierProfil (	@RequestParam("pseudo") String pseudo,
									@RequestParam("nom") String nom,
									@RequestParam("prenom") String prenom,
									@RequestParam("email") String email,
									@RequestParam("telephone") String telephone,
									@RequestParam("rue") String rue,
									@RequestParam("code_postal") String codePostal,
									@RequestParam("ville") String ville,
									@RequestParam("mot_de_passe") String motDePasse,
									@RequestParam("credit") int credit,
									@RequestParam("administrateur") boolean administrateur,
									Model model) {
		//récupérer email de l'utilisateur
		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		String currentEmail = authentification.getName();
		Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(currentEmail);
		
		//attribuer à l'utilisateur les modifications du formulaire
		utilisateur.setPseudo(pseudo);
		utilisateur.setNom(nom);
		utilisateur.setPrenom(prenom);
		utilisateur.setEmail(email);
		utilisateur.setTelephone(telephone);
		utilisateur.setRue(rue);
		utilisateur.setCodePostal(codePostal);
		utilisateur.setVille(ville);
		utilisateur.setMotDePasse(motDePasse);
		utilisateur.setCredit(credit);
		utilisateur.setAdministrateur(administrateur);
		
		//mettre à jour la bdd
		utilisateurService.modifier(utilisateur);
		
		//ajouter utilisateur au model
		model.addAttribute("utilisateur", utilisateur);
		
		return "profil";
	}
	
	@PostMapping("/profil/supprimer")
	public String supprimerCompte(@RequestParam("email") String email){
		utilisateurService.supprimerByEmail(email);
		return "redirect:/";
	}

}
