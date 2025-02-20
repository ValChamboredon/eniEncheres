package fr.eni.eniEncheres.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import fr.eni.eniEncheres.bll.ArticleService;
import fr.eni.eniEncheres.bll.CategorieService;
import fr.eni.eniEncheres.bll.EnchereService;
import fr.eni.eniEncheres.bll.UtilisateurService;
import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.bo.EtatVente;
import fr.eni.eniEncheres.bo.Retrait;
import fr.eni.eniEncheres.bo.Utilisateur;

import fr.eni.eniEncheres.exception.BusinessException;
import jakarta.servlet.http.HttpSession;

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

//    /**
//     * Affiche la liste des articles sur la page principale avec les filtres.
//     */
//    @GetMapping
//    public String afficherLesArticles(
//            @RequestParam(value = "recherche", required = false) String recherche,
//            @RequestParam(value = "categorie", required = false, defaultValue = "0") Integer noCategorie,
//            @RequestParam(value = "typeRecherche", required = false) String typeRecherche,
//            @RequestParam(value = "encheresOuvertes", required = false) Boolean encheresOuvertes,
//            @RequestParam(value = "mesEncheresEnCours", required = false) Boolean mesEncheresEnCours,
//            @RequestParam(value = "mesEncheresRemportees", required = false) Boolean mesEncheresRemportees,
//            @RequestParam(value = "ventesEnCours", required = false) Boolean ventesEnCours,
//            @RequestParam(value = "ventesNonDebutees", required = false) Boolean ventesNonDebutees,
//            @RequestParam(value = "ventesTerminees", required = false) Boolean ventesTerminees,
//            Model model, Principal principal) {
//
//        List<ArticleVendu> articlesFiltres;
//
//        if (principal != null && "mesVentes".equals(typeRecherche)) {
//            // Gestion du filtre "Mes ventes"
//            Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
//            articlesFiltres = articleService.filtrerVentes(
//                    utilisateur.getNoUtilisateur(), ventesEnCours, ventesNonDebutees, ventesTerminees
//            );
//            model.addAttribute("creditsUtilisateur", utilisateur.getCredit());
//
//        } else if (principal != null && "achats".equals(typeRecherche)) {
//            // Gestion du filtre "Achats"
//            Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
//            articlesFiltres = articleService.filtrerAchats(
//                    utilisateur.getNoUtilisateur(), encheresOuvertes, mesEncheresEnCours, mesEncheresRemportees
//            );
//        } else {
//            // Recherche standard (par mot-clé et catégorie)
//            articlesFiltres = articleService.rechercherArticles(recherche, noCategorie);
//        }
//
//        // Ajouter les articles et filtres au modèle
//        model.addAttribute("articles", articlesFiltres);
//        model.addAttribute("recherche", recherche);
//        model.addAttribute("categorie", noCategorie);
//        model.addAttribute("typeRecherche", typeRecherche);
//        model.addAttribute("encheresOuvertes", encheresOuvertes);
//        model.addAttribute("mesEncheresEnCours", mesEncheresEnCours);
//        model.addAttribute("mesEncheresRemportees", mesEncheresRemportees);
//        model.addAttribute("ventesEnCours", ventesEnCours);
//        model.addAttribute("ventesNonDebutees", ventesNonDebutees);
//        model.addAttribute("ventesTerminees", ventesTerminees);
//
//        return "index";
//    }

	@GetMapping(value = "/vendre")
	public String afficherFormulaireVendreArticle(Model model, Principal principal) {
	    // Récupérer l'utilisateur connecté
	    Utilisateur utilisateur = null;
	    if (principal != null) {
	        utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
	    }

	    // Si pas d'utilisateur, créer un utilisateur vide
	    if (utilisateur == null) {
	        utilisateur = new Utilisateur();
	        utilisateur.setRue("");
	        utilisateur.setCodePostal("");
	        utilisateur.setVille("");
	    }

	    // Créer un nouvel article
	    ArticleVendu article = new ArticleVendu();

	    // Ajouter les attributs au modèle
	    model.addAttribute("article", article);
	    model.addAttribute("utilisateurConnecte", utilisateur);

	    // Charger les catégories
	    try {
	        List<Categorie> categories = categorieService.getAllCategories();
	        model.addAttribute("CategoriesEnSession", categories);  // Changez ici
	    } catch (BusinessException e) {
	        // Gérer l'erreur de chargement des catégories
	        model.addAttribute("erreur", "Impossible de charger les catégories");
	    }

	    return "formulaireArticle";
	}

	/**
	 * Enregistre un article mis en vente.
	 */
	@PostMapping("/vendre")
	public String mettreArticleEnVente(
	    @RequestParam("nomArticle") String nomArticle,
	    @RequestParam("description") String description,
	    @RequestParam("categorie") int categorieId,
	    @RequestParam("miseAPrix") int miseAPrix,
	    @RequestParam("dateDebutEncheres") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebutEncheres,
	    @RequestParam("dateFinEncheres") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFinEncheres,
	    @RequestParam("rue") String rue,
	    @RequestParam("codePostal") String codePostal,
	    @RequestParam("ville") String ville,
	    RedirectAttributes redirectAttributes,
	    Principal principal) {

	    try {
	        // Récupérer l'utilisateur connecté
	        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
	        
	        // Récupérer la catégorie
	        Categorie categorie = categorieService.getCategorieById(categorieId);

	        // Créer un nouvel article
	        ArticleVendu article = new ArticleVendu();
	        article.setNomArticle(nomArticle);
	        article.setDescription(description);
	        article.setVendeur(utilisateur);
	        article.setCategorie(categorie);
	        article.setMiseAPrix(miseAPrix);
	        article.setDateDebutEncheres(dateDebutEncheres);
	        article.setDateFinEncheres(dateFinEncheres);
	        
	     // Ajouter des logs de débogage
	        System.out.println("Création d'article:");
	        System.out.println("Nom: " + nomArticle);
	        System.out.println("Description: " + description);
	        System.out.println("Catégorie ID: " + categorieId);
	        System.out.println("Mise à prix: " + miseAPrix);
	        System.out.println("Date début: " + dateDebutEncheres);
	        System.out.println("Date fin: " + dateFinEncheres);

	        // Définir le lieu de retrait
	        Retrait retrait = new Retrait(rue, codePostal, ville);
	        article.setLieuDeRetrait(retrait);
	        
	        System.out.println(retrait);

	        // Prix de vente initial = mise à prix
	        article.setPrixVente(miseAPrix);

	        // Créer l'article
	        articleService.creerArticle(article);

	        redirectAttributes.addFlashAttribute("success", "Article mis en vente avec succès");
	        return "redirect:/encheres";

	    } catch (BusinessException be) {
	        // Log des erreurs métier
	        System.err.println("Erreurs de validation :");
	        be.getMessagesErreur().forEach(System.err::println);

	        redirectAttributes.addFlashAttribute("erreurs", be.getMessagesErreur());
	        return "redirect:/encheres/vendre";
	    } catch (Exception e) {
	        // Log de l'exception technique
	        System.err.println("Erreur technique lors de la création de l'article:");
	        e.printStackTrace();

	        redirectAttributes.addFlashAttribute("erreur", "Erreur technique lors de la création de l'article");
	        return "redirect:/encheres/vendre";
	    }
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

	    // Récupérer la meilleure enchère pour l'article
	    Enchere meilleureEnchere = null;
	    try {
	        meilleureEnchere = enchereService.getMeilleureEnchere(noArticle);
	        model.addAttribute("meilleureEnchere", meilleureEnchere);
	    } catch (BusinessException e) {
	        // Log l'erreur mais continue l'affichage de la page
	        System.err.println("Erreur lors de la récupération de la meilleure enchère: " + e.getMessage());
	        // Optionnel : ajouter un message d'erreur au modèle
	        model.addAttribute("erreur", "Impossible de récupérer l'enchère la plus élevée");
	    }

	    // Si pas de lieu de retrait spécifique, utiliser l'adresse du vendeur
	    if (article.getLieuDeRetrait() == null) {
	        article.setLieuDeRetrait(new Retrait(
	            article.getVendeur().getRue(),
	            article.getVendeur().getCodePostal(),
	            article.getVendeur().getVille()
	        ));
	    }

	    model.addAttribute("article", article);
	    
	    // Si l'utilisateur est connecté, ajouter ses infos au modèle
	    if (principal != null) {
	        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
	        model.addAttribute("utilisateurConnecte", utilisateur);
	    }

	    return "detailArticle";
	}

  
    
    //@ModelAttribute("creditsUtilisateur")


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
                redirectAttributes.addFlashAttribute("erreur", "L'article n'est pas en cours d'enchère");
                return "redirect:/encheres/article/detail/" + articleId;
            }
            
         // Récupérer l'enchère courante la plus haute
            Enchere enchereActuelle = enchereService.getMeilleureEnchere(articleId);
            
         // Vérifier que le montant est supérieur à l'enchère actuelle
            if (enchereActuelle != null && montantEnchere <= enchereActuelle.getMontantEnchere()) {
                redirectAttributes.addFlashAttribute("erreur", "Le montant doit être supérieur à l'enchère actuelle");
                return "redirect:/encheres/article/detail/" + articleId;
            }
            
         // Vérifier que l'utilisateur a assez de crédits
            if (utilisateur.getCredit() < montantEnchere) {
                redirectAttributes.addFlashAttribute("erreur", "Crédit insuffisant");
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


}
