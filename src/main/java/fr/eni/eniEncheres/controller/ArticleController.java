package fr.eni.eniEncheres.controller;

import java.util.Collections;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;

import fr.eni.eniEncheres.bll.ArticleService;
import fr.eni.eniEncheres.bll.CategorieService;
import fr.eni.eniEncheres.bll.EnchereService;
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
import jakarta.validation.Valid;

@Controller
@RequestMapping("/encheres")
@SessionAttributes("CategoriesEnSession")
public class ArticleController {

	private final ArticleService articleService;
	private final CategorieService categorieService;
	private final UtilisateurService utilisateurService;
	private EnchereService enchereService;

	/**
	 * Constructeur avec injection des dépendances.
	 */
	public ArticleController(ArticleService articleService, CategorieService categorieService,
			UtilisateurService utilisateurService, EnchereService enchereService) {
		this.articleService = articleService;
		this.categorieService = categorieService;
		this.utilisateurService = utilisateurService;
		this.enchereService = enchereService;
	}

	/**
	 * Affiche la liste des articles sur la page principale.
	 */
	@GetMapping
	public String afficherLesArticles(
	        @RequestParam(value = "recherche", required = false) String recherche,
	        @RequestParam(value = "categorie", required = false, defaultValue = "0") Integer noCategorie,
	        Model model, Principal principal) {

	    // Vérification et correction si `noCategorie` est null
	    if (noCategorie == null) {
	        noCategorie = 0; // Par défaut : "Toutes les catégories"
	    }

	    // Récupération des articles filtrés
	    List<ArticleVendu> articlesFiltres = articleService.rechercherArticles(recherche, noCategorie);

	    // Ajouter les articles et les filtres au modèle
	    model.addAttribute("articles", articlesFiltres);
	    model.addAttribute("recherche", recherche);
	    model.addAttribute("categorie", noCategorie);

	    // Ajouter les crédits si l'utilisateur est connecté
	    if (principal != null) {
	        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
	        model.addAttribute("creditsUtilisateur", utilisateur.getCredit());
	    }

	    return "index"; // Retourne la vue index.html
    }
    
	@GetMapping("/vendeur")
	public String afficherProfilVendeur(@RequestParam("pseudo") String pseudo, Model model) {
		Utilisateur vendeur = utilisateurService.getUtilisateurByPseudo(pseudo);
		model.addAttribute("vendeur", vendeur);
		
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

		Utilisateur utilisateur = null;
		if (principal != null) {
			utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
		}
		if (utilisateur == null) {
	        utilisateur = new Utilisateur(); // Évite null dans le modèle
	        utilisateur.setRue(""); 
	        utilisateur.setCodePostal(""); 
	        utilisateur.setVille("");
	    }

	    model.addAttribute("utilisateurConnecte", utilisateur);

		return "formulaireArticle";
	}

	/**
	 * Enregistre un article mis en vente.
	 */
	@PostMapping("/vendre")
	public String mettreArticleEnVente(@Valid @ModelAttribute("article") ArticleVendu article,
			BindingResult bindingResult, Model model, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "formulaireArticle";
		}

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
			@RequestParam("dateDebutEncheres") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dateDebutEncheres,
			@RequestParam("dateFinEncheres") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime dateFinEncheres,
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
	
	@PostMapping("/encherir")
    public String encherir(@RequestParam("articleId") int articleId,
                          @RequestParam("montantEnchere") int montantEnchere,
                          RedirectAttributes redirectAttributes) {
    	System.out.println("Début méthode encherir - articleId: " + articleId + ", montant: " + montantEnchere);
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Auth: " + (auth != null ? auth.getName() : "null"));
            if (auth == null || !auth.isAuthenticated()) {
            	System.out.println("Utilisateur non authentifié");
                return "redirect:/connexion";
            }

            // Récupération de l'utilisateur connecté
            Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(auth.getName());
            System.out.println("Utilisateur récupéré: " + utilisateur.getPseudo());
            
            // Récupération de l'article
            ArticleVendu article = articleService.getArticleById(articleId);
            System.out.println("Article récupéré: " + article.getNomArticle() + ", état: " + article.getEtatVente());
            
         // Vérification que l'article est bien EN_COURS
            if (article.getEtatVente() != EtatVente.EN_COURS) {
            	System.out.println("Article non en vente: " + article.getEtatVente());
                redirectAttributes.addFlashAttribute("erreur", "ERR_ARTICLE_NON_EN_VENTE");
                return "redirect:/encheres/article/detail/" + articleId;
            }
            
            // Création de l'enchère
            Enchere enchere = new Enchere();
            enchere.setArticle(article);
            enchere.setUtilisateur(utilisateur);
            enchere.setMontantEnchere(montantEnchere);
            enchere.setDateEnchere(LocalDateTime.now());
            System.out.println("Enchère créée: " + enchere);

            // Sauvegarde de l'enchère
            enchereService.ajouterEnchere(enchere);
            System.out.println("Enchère sauvegardée avec succès");
            
            redirectAttributes.addFlashAttribute("success", "Votre enchère a été enregistrée avec succès!");
            return "redirect:/encheres/article/detail/" + articleId;
            
        } catch (BusinessException be) {
            System.out.println("BusinessException: " + be.getClesErreurs());
            be.printStackTrace();
            redirectAttributes.addFlashAttribute("erreurs", be.getClesErreurs());
            return "redirect:/encheres/article/detail/" + articleId;
        } catch (Exception e) {
            System.out.println("Exception générale: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erreur", "Erreur technique");
            return "redirect:/encheres/article/detail/" + articleId;
        }
    }

}
