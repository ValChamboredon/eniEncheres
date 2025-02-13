package fr.eni.eniEncheres.controller;

import java.util.Collections;

import java.security.Principal;
import java.time.LocalDate;
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
     * Affiche la liste des articles sur la page principale.
     */
    @GetMapping
    public String afficherLesArticles(Model model, Principal principal) {
        List<ArticleVendu> articles = articleService.consulterTout();
        model.addAttribute("articles", articles);

        if (principal != null) {
            Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(principal.getName());
            model.addAttribute("creditsUtilisateur", utilisateur.getCredit());
        }

        return "index";
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
    public String afficherFormulaireVendreArticle(Model model) {
        model.addAttribute("article", new ArticleVendu());
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
            article.setLieuDeRetrait(new Retrait(
                    article.getVendeur().getRue(),
                    article.getVendeur().getCodePostal(),
                    article.getVendeur().getVille()
            ));
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
            if (article.getEtatVente() == EtatVente.ENCHERES_TERMINEES || article.getEtatVente() == EtatVente.RETRAIT_EFFECTUE) {
                return "redirect:/encheres?error=suppressionNonAutorisee";
            }
            articleService.supprimerArticle(articleId);
        }


        return "redirect:/encheres";
    }

    /**
     * Affiche le formulaire de modification d’un article si l’enchère n’a pas commencé.
     */
    @GetMapping("/modifier")
    public String afficherFormulaireModification(@RequestParam("articleId") int articleId, Principal principal, Model model) {
        ArticleVendu article = articleService.getArticleById(articleId);


        if (article != null && article.getEtatVente() == EtatVente.CREEE
                && article.getVendeur().getEmail().equals(principal.getName())) {
            model.addAttribute("article", article);
            return "modifierArticle";
        }

        return "redirect:/encheres";
    }


    /**
     * Modifie un article si l’utilisateur est le vendeur et que l’état est encore `CREEE`.
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
}

