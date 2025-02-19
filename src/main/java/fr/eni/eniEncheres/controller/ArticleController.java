package fr.eni.eniEncheres.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import fr.eni.eniEncheres.bll.ArticleService;
import fr.eni.eniEncheres.bll.CategorieService;
import fr.eni.eniEncheres.bll.UtilisateurService;
import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.bo.Retrait;
import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.exception.BusinessException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/encheres")
@SessionAttributes("CategoriesEnSession")
public class ArticleController {

    private final ArticleService articleService;
    private final CategorieService categorieService;
    private final UtilisateurService utilisateurService;

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
     * Charge les catégories en session pour toutes les pages.
     */
    @ModelAttribute("CategoriesEnSession")
    public List<Categorie> chargerCategories() throws BusinessException {
        return categorieService.getAllCategories();
    }

    /**
     * Affiche la liste des articles sur la page principale avec les filtres.
     */
    @GetMapping
    public String afficherLesArticles(
            @RequestParam(value = "recherche", required = false) String recherche,
            @RequestParam(value = "categorie", required = false, defaultValue = "0") Integer noCategorie,
            @RequestParam(value = "typeRecherche", required = false) String typeRecherche,
            @RequestParam(value = "encheresOuvertes", required = false) Boolean encheresOuvertes,
            @RequestParam(value = "mesEncheresEnCours", required = false) Boolean mesEncheresEnCours,
            @RequestParam(value = "mesEncheresRemportees", required = false) Boolean mesEncheresRemportees,
            @RequestParam(value = "ventesEnCours", required = false) Boolean ventesEnCours,
            @RequestParam(value = "ventesNonDebutees", required = false) Boolean ventesNonDebutees,
            @RequestParam(value = "ventesTerminees", required = false) Boolean ventesTerminees,
            Model model, Principal principal) {

        List<ArticleVendu> articlesFiltres;

        if (principal != null && "mesVentes".equals(typeRecherche)) {
            // Gestion du filtre "Mes ventes"
            Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
            articlesFiltres = articleService.filtrerVentes(
                    utilisateur.getNoUtilisateur(), ventesEnCours, ventesNonDebutees, ventesTerminees
            );
            model.addAttribute("creditsUtilisateur", utilisateur.getCredit());

        } else if (principal != null && "achats".equals(typeRecherche)) {
            // Gestion du filtre "Achats"
            Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
            articlesFiltres = articleService.filtrerAchats(
                    utilisateur.getNoUtilisateur(), encheresOuvertes, mesEncheresEnCours, mesEncheresRemportees
            );
        } else {
            // Recherche standard (par mot-clé et catégorie)
            articlesFiltres = articleService.rechercherArticles(recherche, noCategorie);
        }

        // Ajouter les articles et filtres au modèle
        model.addAttribute("articles", articlesFiltres);
        model.addAttribute("recherche", recherche);
        model.addAttribute("categorie", noCategorie);
        model.addAttribute("typeRecherche", typeRecherche);
        model.addAttribute("encheresOuvertes", encheresOuvertes);
        model.addAttribute("mesEncheresEnCours", mesEncheresEnCours);
        model.addAttribute("mesEncheresRemportees", mesEncheresRemportees);
        model.addAttribute("ventesEnCours", ventesEnCours);
        model.addAttribute("ventesNonDebutees", ventesNonDebutees);
        model.addAttribute("ventesTerminees", ventesTerminees);

        return "index";
    }

    /**
     * Affiche le formulaire de mise en vente d'un article.
     */
    @GetMapping("/vendre")
    public String afficherFormulaireVendreArticle(Model model, Principal principal) {
        model.addAttribute("article", new ArticleVendu());

        if (principal != null) {
            Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
            model.addAttribute("utilisateurConnecte", utilisateur);
        }

        return "formulaireArticle";
    }

    
    /**
     * Enregistre un article mis en vente.
     */
    @PostMapping("/vendre")
    public String mettreArticleEnVente(@Valid @ModelAttribute("article") ArticleVendu article,
                                       BindingResult bindingResult, Model model, Principal principal) throws BusinessException {
        if (principal == null) {
            return "redirect:/encheres/connexion";
        }

        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
        model.addAttribute("utilisateurConnecte", utilisateur);

        if (bindingResult.hasErrors()) {
            return "formulaireArticle";
        }

        article.setVendeur(utilisateur);
        articleService.creerArticle(article);

        return "redirect:/encheres";
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
     * Supprime un article.
     */
    @PostMapping("/supprimer")
    public String supprimerArticle(@RequestParam("articleId") int articleId, Principal principal) {
        ArticleVendu article = articleService.getArticleById(articleId);
        if (article != null && article.getVendeur().getEmail().equals(principal.getName())) {
            articleService.supprimerArticle(articleId);
        }
        return "redirect:/encheres";
    }

    /**
     * Modifie un article.
     */
    @PostMapping("/modifier")
    public String modifierArticle(@RequestParam("articleId") int articleId,
                                  @RequestParam("nomArticle") String nomArticle,
                                  @RequestParam("description") String description,
                                  @RequestParam("miseAPrix") int miseAPrix,
                                  @RequestParam("dateDebutEncheres") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebutEncheres,
                                  @RequestParam("dateFinEncheres") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFinEncheres,
                                  Principal principal) {
        ArticleVendu article = articleService.getArticleById(articleId);
        if (article != null && article.getEtatVente().equals("CREEE") &&
                article.getVendeur().getEmail().equals(principal.getName())) {
            article.setNomArticle(nomArticle);
            article.setDescription(description);
            article.setMiseAPrix(miseAPrix);
            article.setDateDebutEncheres(dateDebutEncheres);
            article.setDateFinEncheres(dateFinEncheres);
            articleService.modifierArticle(article);
        }
        return "redirect:/encheres/article/detail/" + articleId;
    }
    
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
    
    @GetMapping("/modifier")
    public String afficherFormulaireModification(@RequestParam("articleId") int articleId, Model model, Principal principal) {
        ArticleVendu article = articleService.getArticleById(articleId);

        if (article != null && article.getVendeur().getEmail().equals(principal.getName())) {
            model.addAttribute("article", article);
            return "modifierArticle"; // Assurez-vous que cette vue existe !
        }

        return "redirect:/encheres"; // Redirection si l'article n'existe pas ou que l'utilisateur n'a pas les droits
    }

}
