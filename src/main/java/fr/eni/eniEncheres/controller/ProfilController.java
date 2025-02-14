package fr.eni.eniEncheres.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import fr.eni.eniEncheres.bll.UtilisateurService;
import fr.eni.eniEncheres.bo.Utilisateur;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

/**
 * ✅ Contrôleur responsable de la gestion du profil utilisateur.
 */
@Controller
@RequestMapping("/encheres")
public class ProfilController {
	
	private final UtilisateurService utilisateurService;

	public ProfilController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}
		
	/**
	 * ✅ Affiche le profil de l'utilisateur connecté.
	 *
	 * @param model Modèle pour afficher les informations utilisateur.
	 * @return La page de profil utilisateur.
	 */
	@GetMapping("/profil")
	public String afficherProfil(Model model) {
		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		String email = authentification.getName();
		
		Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(email);
		model.addAttribute("utilisateur", utilisateur);
		
		return "profil";
	}

	/**
	 * ✅ Met à jour les informations du profil utilisateur.
	 */
	@PostMapping("/profil/detail")
	public String modifierProfil(
	    @RequestParam("pseudo") String pseudo,
	    @RequestParam("nom") String nom,
	    @RequestParam("prenom") String prenom,
	    @RequestParam("email") String email,
	    @RequestParam("telephone") String telephone,
	    @RequestParam("rue") String rue,
	    @RequestParam("code_postal") String codePostal,
	    @RequestParam("ville") String ville,
	    @RequestParam("mot_de_passe") String motDePasse,
	    @RequestParam("nouveau_mot_de_passe") String motDePasseNouveau,
	    @RequestParam("confirmer_mot_de_passe") String motDePasseConfirme,
	    @RequestParam("credit") int credit,
	    Model model) {

	    Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
	    String currentEmail = authentification.getName();
	    Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(currentEmail);

	    // ✅ Vérification du mot de passe actuel
	    if (!PasswordEncoderFactories.createDelegatingPasswordEncoder().matches(motDePasse, utilisateur.getMotDePasse())) {
	        return "redirect:/profil?errorMotDePasse";
	    }

	    // ✅ Mise à jour du profil
	    utilisateur.setPseudo(pseudo);
	    utilisateur.setNom(nom);
	    utilisateur.setPrenom(prenom);
	    utilisateur.setEmail(email);
	    utilisateur.setTelephone(telephone);
	    utilisateur.setRue(rue);
	    utilisateur.setCodePostal(codePostal);
	    utilisateur.setVille(ville);
	    utilisateur.setCredit(credit);

	    utilisateurService.modifier(utilisateur);
	    model.addAttribute("utilisateur", utilisateur);

	    return "profil";
	}
}
