package fr.eni.eniEncheres.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.eni.eniEncheres.bll.ArticleService;
import fr.eni.eniEncheres.bll.CategorieService;
import fr.eni.eniEncheres.bll.EnchereService;
import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.exception.BusinessException;

@Controller
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private EnchereService enchereService;

    @Autowired
    private CategorieService categorieService; // Utilisation du service corrigé

    /**
     * Affiche la liste des articles en cours de vente.
     */
    @GetMapping
    public String listArticlesEnCours(Model model) {
        List<ArticleVendu> articlesEnCours = articleService.getArticlesEnVente();
        model.addAttribute("articles", articlesEnCours);
        return "listeArticles";
    }

    /**
     * Affiche le détail d'un article spécifique avec ses enchères.
     */
    @GetMapping("/{id}")
    public String getArticle(@PathVariable int id, Model model) throws BusinessException {
        ArticleVendu article = articleService.getArticleById(id);
        List<Enchere> encheres = enchereService.getEncheresParArticle(id);
        model.addAttribute("article", article);
        model.addAttribute("encheres", encheres);
        return "detailArticle";
    }

    /**
     * Affiche le formulaire pour ajouter un nouvel article.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/new")
    public String newArticleForm(Model model) throws BusinessException {
        model.addAttribute("article", new ArticleVendu());
        model.addAttribute("categories", categorieService.getAllCategories());
        return "formulaireArticle";
    }

    /**
     * Enregistre un nouvel article mis en vente.
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public String addArticle(@ModelAttribute ArticleVendu article, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Création d'un nouvel article : " + article);
            articleService.addArticle(article);
            redirectAttributes.addFlashAttribute("successMessage", "Article créé avec succès !");
            return "redirect:/articles";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la création de l'article.");
            return "redirect:/articles/new";
        }
    }


    /**
     * Affiche le formulaire d'édition d'un article existant.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit/{id}")
    public String editArticleForm(@PathVariable int id, Model model) throws BusinessException {
        model.addAttribute("article", articleService.getArticleById(id));
        model.addAttribute("categories", categorieService.getAllCategories()); // Correction ici aussi
        return "formulaireArticle";
    }

    /**
     * Met à jour un article existant (seulement si l'utilisateur est propriétaire de l'article).
     */
    @PreAuthorize("isAuthenticated() and #article.vendeur.noUtilisateur == authentication.principal.id")
    @PostMapping("/edit/{id}")
    public String updateArticle(@PathVariable int id, @ModelAttribute ArticleVendu article) {
        article.setNoArticle(id);
        articleService.updateArticle(article);
        return "redirect:/articles";
    }

    /**
     * Supprime un article (uniquement accessible aux administrateurs ou au propriétaire).
     */
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN') or #article.vendeur.noUtilisateur == authentication.principal.id")
    @GetMapping("/delete/{id}")
    public String deleteArticle(@PathVariable int id) {
        articleService.deleteArticle(id);
        return "redirect:/articles";
    }

    /**
     * Recherche des articles par mot-clé et catégorie.
     */
    @GetMapping("/search")
    public String searchArticles(@RequestParam String keyword, @RequestParam(defaultValue = "0") int categoryId, Model model) {
        model.addAttribute("articles", articleService.searchArticles(keyword, categoryId));
        return "listeArticles";
    }
}

