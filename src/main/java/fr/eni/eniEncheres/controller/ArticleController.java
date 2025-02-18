package fr.eni.eniEncheres.controller;

import java.util.Collections;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import fr.eni.eniEncheres.bll.ArticleService;
import fr.eni.eniEncheres.bll.CategorieService;
//import fr.eni.eniEncheres.bll.EnchereService;
import fr.eni.eniEncheres.bll.UtilisateurService;

import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.bo.EtatVente;
import fr.eni.eniEncheres.bo.Retrait;
import fr.eni.eniEncheres.bo.Utilisateur;

import fr.eni.eniEncheres.bo.*;

import fr.eni.eniEncheres.exception.BusinessException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.nio.file.Path;

@Controller
@RequestMapping("/encheres")
@SessionAttributes("CategoriesEnSession")
public class ArticleController {

	private final ArticleService articleService;
	private final CategorieService categorieService;
	private final UtilisateurService utilisateurService;
	
    @Value("${upload.dir}")
    private String uploadDir;

	/**
	 * Constructeur avec injection des dépendances.
	 */
	public ArticleController(ArticleService articleService, CategorieService categorieService,
			UtilisateurService utilisateurService) {
		this.articleService = articleService;
		this.categorieService = categorieService;
		this.utilisateurService = utilisateurService;
	}

	/**
	 * Affiche la liste des articles sur la page principale.
	 */
	@GetMapping
	public String afficherLesArticles(
	        @RequestParam(value = "recherche", required = false) String recherche,
	        @RequestParam(value = "categorie", required = false, defaultValue = "0") Integer noCategorie,
	        @RequestParam(value = "typeRecherche", required = false) String typeRecherche,
	        @RequestParam(value = "ventesEnCours", required = false) Boolean ventesEnCours,
	        @RequestParam(value = "ventesNonDebutees", required = false) Boolean ventesNonDebutees,
	        @RequestParam(value = "ventesTerminees", required = false) Boolean ventesTerminees,
	        Model model, Principal principal) {

	    // Vérification des valeurs nulles
	    if (noCategorie == null) noCategorie = 0;

	    List<ArticleVendu> articlesFiltres;

	    // Cas où l'utilisateur filtre sur "Mes ventes"
	    if (principal != null && "mesVentes".equals(typeRecherche)) {
	        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
	        articlesFiltres = articleService.filtrerVentes(
	                utilisateur.getNoUtilisateur(), ventesEnCours, ventesNonDebutees, ventesTerminees
	        );

	        model.addAttribute("creditsUtilisateur", utilisateur.getCredit());
	    } else {
	        // Recherche standard (par mot-clé et catégorie)
	        articlesFiltres = articleService.rechercherArticles(recherche, noCategorie);
	    }

	    // Ajouter les articles et les filtres au modèle
	    model.addAttribute("articles", articlesFiltres);
	    model.addAttribute("recherche", recherche);
	    model.addAttribute("categorie", noCategorie);
	    model.addAttribute("typeRecherche", typeRecherche);
	    model.addAttribute("ventesEnCours", ventesEnCours);
	    model.addAttribute("ventesNonDebutees", ventesNonDebutees);
	    model.addAttribute("ventesTerminees", ventesTerminees);

	    return "index"; // Retourne la vue index.html
    }
    
	@GetMapping("/vendeur")
	public String afficherProfilVendeur(@RequestParam("pseudo") String pseudo, Model model, HttpSession session) {
		//retrouver le vendeur de l'article
		Utilisateur vendeur = utilisateurService.getUtilisateurByPseudo(pseudo);
		model.addAttribute("vendeur", vendeur);
		
		//récupérer l'utilisateur actif
		Authentication authentification = SecurityContextHolder.getContext().getAuthentication();
		String email = authentification.getName();
		System.out.println(email);
		int userId = utilisateurService.getIdByEmail(email);
		Utilisateur utilisateur = utilisateurService.getUtilisateurById(userId);

	    if (utilisateur != null) {
	        model.addAttribute("isAdmin", utilisateur.isAdministrateur());  // Assurez-vous que isAdmin est ajouté
	        model.addAttribute("vendeur", utilisateur);  // Si tu utilises vendeur pour l'utilisateur
	    } else {
	        model.addAttribute("isAdmin", false);  // Si l'utilisateur n'est pas connecté ou admin
	    }
		
		return "vendeur-profil";
	}


	/**
	 * Charge les catégories en session pour toutes les pages.
	 */
	@ModelAttribute("CategoriesEnSession")
	public List<Categorie> chargerCategories() throws BusinessException {
		return categorieService.getAllCategories();
	}

	/**
	 * Affiche le formulaire de mise en vente d'un article.
	 */
	@GetMapping("/vendre")
	public String afficherFormulaireVendreArticle(Model model, Principal principal) {
		model.addAttribute("article", new ArticleVendu());

		if (principal != null) {
			Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
			model.addAttribute("utilisateurConnecte", utilisateur); // Envoie les infos de l'utilisateur à Thymeleaf
		}

		return "formulaireArticle";
	}

	/**
	 * Enregistre un article mis en vente.
	 */
	@PostMapping("/vendre")
	public String mettreArticleEnVente(@Valid @ModelAttribute("article") ArticleVendu article,
			BindingResult bindingResult, Model model, Principal principal, @RequestParam("image") MultipartFile image) {
		if (bindingResult.hasErrors()) {
			return "formulaireArticle";
		}
        
        // Définir le dossier de destination
//        File destinationDir = new File("");
//        if (!destinationDir.exists()) {
//        	destinationDir.mkdirs(); // Crée le dossier s'il n'existe pas
//        }
//
//        // Construire le chemin du fichier
//        File destinationFile = new File(uploadDir + image.getOriginalFilename());
//
//        // Sauvegarde du fichier
//        try {
//			image.transferTo(destinationFile);
//		} catch (IllegalStateException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        

		if (principal != null) {
			Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
			article.setVendeur(utilisateur);

			try {
				articleService.creerArticle(article);
				return "redirect:/encheres";
			} catch (BusinessException be) {
				bindingResult.addError(new ObjectError("globalError", be.getMessage()));
				return "formulaireArticle";
			}
		}

		return "redirect:/encheres/connexion";
	}

	/**
	 * Affiche le détail d'un article.
	 */
	@GetMapping("/article/detail/{noArticle}")
	public String afficherDetailArticle(@PathVariable("noArticle") int noArticle, Model model, Principal principal) {
		ArticleVendu article = articleService.getArticleById(noArticle);

		if (article == null) {
			return "redirect:/encheres";
		}

		if (article.getLieuDeRetrait() == null) {
			article.setLieuDeRetrait(new Retrait(article.getVendeur().getRue(), article.getVendeur().getCodePostal(),
					article.getVendeur().getVille()));
		}

		model.addAttribute("article", article);
		return "detailArticle";
	}

	/**
	 * Supprime un article si l'utilisateur connecté est bien le vendeur.
	 */
	@PostMapping("/supprimer")
	public String supprimerArticle(@RequestParam("articleId") int articleId, Principal principal) {
		ArticleVendu article = articleService.getArticleById(articleId);

		if (article != null && article.getVendeur().getEmail().equals(principal.getName())) {
			if (article.getEtatVente() == EtatVente.ENCHERES_TERMINEES
					|| article.getEtatVente() == EtatVente.RETRAIT_EFFECTUE) {
				return "redirect:/encheres?error=suppressionNonAutorisee";
			}
			articleService.supprimerArticle(articleId);
		}

		return "redirect:/encheres";
	}

	/**
	 * Affiche le formulaire de modification d’un article si l’enchère n’a pas
	 * commencé.
	 */
	@GetMapping("/modifier")
	public String afficherFormulaireModification(@RequestParam("articleId") int articleId, Principal principal,
			Model model) {
		ArticleVendu article = articleService.getArticleById(articleId);

		if (article != null && article.getEtatVente() == EtatVente.CREEE
				&& article.getVendeur().getEmail().equals(principal.getName())) {
			model.addAttribute("article", article);
			return "modifierArticle";
		}

		return "redirect:/encheres";
	}

	/**
	 * Modifie un article si l’utilisateur est le vendeur et que l’état est encore
	 * `CREEE`.
	 */
	@PostMapping("/modifier")
	public String modifierArticle(@RequestParam("articleId") int articleId,
			@RequestParam("nomArticle") String nomArticle, @RequestParam("description") String description,
			@RequestParam("miseAPrix") int miseAPrix,
			@RequestParam("dateDebutEncheres") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebutEncheres,
			@RequestParam("dateFinEncheres") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFinEncheres,
			Principal principal) {

		ArticleVendu article = articleService.getArticleById(articleId);

		if (article != null && article.getEtatVente() == EtatVente.CREEE
				&& article.getVendeur().getEmail().equals(principal.getName())) {
			article.setNomArticle(nomArticle);
			article.setDescription(description);
			article.setMiseAPrix(miseAPrix);
			article.setDateDebutEncheres(dateDebutEncheres);
			article.setDateFinEncheres(dateFinEncheres);

			articleService.modifierArticle(article);
		}

		return "redirect:/encheres/article/detail/" + articleId;
	}

	/**
	 * Charge les crédits de l'utilisateur connecté.
	 */
	@ModelAttribute("creditsUtilisateur")
	public Integer chargerCreditsUtilisateur(Principal principal) {
		if (principal != null) {
			Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
			return utilisateur.getCredit();
		}
		return 0;
	}

	@GetMapping("/recherche")
	public String rechercherArticles(@RequestParam(value = "recherche", required = false) String recherche,
			@RequestParam(value = "categorie", required = false, defaultValue = "0") Integer noCategorie, Model model) {

		// Récupération des articles filtrés
		List<ArticleVendu> articlesFiltres = articleService.rechercherArticles(recherche, noCategorie);

		// Ajouter les articles filtrés au modèle pour l'affichage dans la vue
		model.addAttribute("articles", articlesFiltres);

		// Redirige vers la page des enchères avec la liste filtrée
		return "index";
	}

}
