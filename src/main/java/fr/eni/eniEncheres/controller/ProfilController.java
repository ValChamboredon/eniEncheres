package fr.eni.eniEncheres.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import fr.eni.eniEncheres.bll.UtilisateurService;
import fr.eni.eniEncheres.bo.Utilisateur;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/encheres")
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
		
		return "profil";
	}
	
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

	    // Récupérer l'email de l'utilisateur connecté
	    Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
	    String currentEmail = authentification.getName();
	    Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(currentEmail);

	    // Vérifier si le mot de passe actuel est correct
	    if (!PasswordEncoderFactories.createDelegatingPasswordEncoder().matches(motDePasse, utilisateur.getMotDePasse())) {
	        return "redirect:/profil?errorMotDePasse";  // Le mot de passe actuel est incorrect
	    }

	    // Si un nouveau mot de passe est fourni, vérifier s'il correspond à la confirmation
	    if (motDePasseNouveau != null && !motDePasseNouveau.isEmpty()) {
	        if (!motDePasseNouveau.equals(motDePasseConfirme)) {
	            return "redirect:/profil?errorMotDePasseMismatch";  // Les mots de passe ne correspondent pas
	        }
	        // Encoder et changer le mot de passe
	        utilisateur.setMotDePasse(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(motDePasseNouveau));
	    }

	    // Mettre à jour les autres informations de l'utilisateur
	    utilisateur.setPseudo(pseudo);
	    utilisateur.setNom(nom);
	    utilisateur.setPrenom(prenom);
	    utilisateur.setEmail(email);
	    utilisateur.setTelephone(telephone);
	    utilisateur.setRue(rue);
	    utilisateur.setCodePostal(codePostal);
	    utilisateur.setVille(ville);
	    utilisateur.setCredit(credit);

	    // Mettre à jour l'utilisateur dans la base de données
	    utilisateurService.modifier(utilisateur);

	    // Ajouter l'utilisateur au modèle pour l'afficher dans la vue
	    model.addAttribute("utilisateur", utilisateur);

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
