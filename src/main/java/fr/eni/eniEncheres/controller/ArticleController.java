package fr.eni.eniEncheres.controller;

import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.eniEncheres.bll.ArticleService;
import fr.eni.eniEncheres.bll.CategorieService;
import fr.eni.eniEncheres.bll.UtilisateurService;
import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.bo.Retrait;
import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.exception.BusinessException;
import jakarta.validation.Valid;

/**
 * ✅ Contrôleur gérant les actions liées aux enchères (affichage, vente, gestion des catégories).
 */
@Controller
@RequestMapping("/encheres")
@SessionAttributes("CategoriesEnSession")
public class ArticleController {

    private final ArticleService articleService;
    private final CategorieService categorieService;
    private final UtilisateurService utilisateurService;

    public ArticleController(ArticleService articleService, CategorieService categorieService, UtilisateurService utilisateurService) {
        this.articleService = articleService;
        this.categorieService = categorieService;
        this.utilisateurService = utilisateurService;
    }

    /**
     * ✅ Affiche la liste des enchères en cours.
     * 
     * @param model Modèle contenant les enchères à afficher.
     * @return La vue affichant la liste des enchères.
     */
    @GetMapping
    public String afficherLesArticles(Model model) {
        List<ArticleVendu> articles = articleService.getArticlesEnVente();
        model.addAttribute("articles", articles);
        return "index";
    }

    /**
     * ✅ Charge les catégories en session.
     * Cela garantit que les catégories sont toujours disponibles dans les vues.
     *
     * @return Liste de toutes les catégories disponibles.
     * @throws BusinessException Si une erreur survient lors de la récupération des catégories.
     */
    @ModelAttribute("CategoriesEnSession")
    public List<Categorie> chargerCategories() throws BusinessException {
        System.out.println("Chargement des CATEGORIES en session...");
        return categorieService.getAllCategories();
    }

    /**
     * ✅ Affiche le formulaire de mise en vente d'un article.
     * Ajoute un article vide et un retrait pour la liaison des données avec Thymeleaf.
     *
     * @param model Modèle utilisé pour la liaison des données.
     * @return La vue du formulaire de vente d'un article.
     */
    @GetMapping("/vendre")
    public String afficherFormulaireVendreArticle(Model model) {
        model.addAttribute("article", new ArticleVendu());
        model.addAttribute("retrait", new Retrait());
        return "formulaireArticle";
    }

    /**
     * ✅ Enregistre un article mis en vente.
     * Vérifie que l'utilisateur est connecté et que les validations sont respectées.
     *
     * @param retrait Informations sur le retrait de l'article.
     * @param article Détails de l'article mis en vente.
     * @param bindingResult Résultats des validations.
     * @param model Modèle pour transmettre les données à la vue.
     * @return Redirection vers la liste des enchères si la vente réussit, sinon retour au formulaire avec erreurs.
     */
    @PostMapping("/vendre")
    public String mettreArticleEnVente(@Valid @ModelAttribute("retrait") Retrait retrait,
                                       @Valid @ModelAttribute("article") ArticleVendu article, 
                                       BindingResult bindingResult, Model model) {
        
        if (bindingResult.hasErrors()) {
            System.out.println("Erreurs de validation !");
            return "formulaireArticle"; // ✅ Garde l'utilisateur sur le formulaire si erreurs de validation
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // ✅ Vérifie que l'utilisateur est connecté avant de soumettre une enchère
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentEmail = authentication.getName();
            Utilisateur utilisateurCourant = utilisateurService.getUtilisateurByEmail(currentEmail);

            // ✅ Associe le vendeur et le lieu de retrait
            article.setVendeur(utilisateurCourant);
            article.setLieuDeRetrait(retrait);

            try {
                articleService.creerArticle(article);
                return "redirect:/encheres"; // ✅ Redirige vers la liste des enchères
            } catch (BusinessException be) {
                for (String messageErreur : be.getMessagesErreur()) {
                    bindingResult.addError(new ObjectError("globalError", messageErreur));
                }
                model.addAttribute("errors", be.getMessagesErreur());
                return "formulaireArticle";
            }
        }

        return "redirect:/encheres/connexion"; // 🔹 Redirige si l'utilisateur n'est pas connecté
    }
}
