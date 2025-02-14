package fr.eni.eniEncheres.controller;

import java.util.Collections;
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
	private CategorieService categorieService;
	
	@GetMapping
	public String listArticlesEnCours(Model model) {
	    List<ArticleVendu> articlesEnCours = articleService.getArticlesEnVente();
	    model.addAttribute("articles", articlesEnCours);
	    return "listeArticles";
	}
	
	@GetMapping("/{id}")
	public String getArticle(@PathVariable int id, Model model) throws BusinessException {
	    ArticleVendu article = articleService.getArticleById(id);
	    List<Enchere> encheres = enchereService.getEncheresParArticle(id);
	    model.addAttribute("article", article);
	    model.addAttribute("encheres", encheres);
	    return "detailArticle";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/new")
	public String newArticleForm(Model model) {
		model.addAttribute("article", new ArticleVendu());
		return "formulaireArticle";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping
	public String addArticle(@ModelAttribute ArticleVendu article) {
		articleService.addArticle(article);
		return "redirect:/articles";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/edit/{id}")
    public String editArticleForm(@PathVariable int id, Model model) {
        model.addAttribute("article", articleService.getArticleById(id));
        return "formulaireArticle";
    }

	@PreAuthorize("isAuthenticated() and #article.vendeur.noUtilisateur == authentication.principal.id")
    @PostMapping("/edit/{id}")
    public String updateArticle(@PathVariable int id, @ModelAttribute ArticleVendu article) {
        article.setNoArticle(id);
        articleService.updateArticle(article);
        return "redirect:/articles";
    }

    @GetMapping("/delete/{id}")
    public String deleteArticle(@PathVariable int id) {
        articleService.deleteArticle(id);
        return "redirect:/articles";
    }

    @GetMapping("/search")
    public String searchArticles(@RequestParam String keyword, @RequestParam(defaultValue = "0") int categoryId, Model model) {
        model.addAttribute("articles", articleService.searchArticles(keyword, categoryId));
        return "listeArticles";
    }
	

    @GetMapping("/encheres")
    public String listeEncheres(Model model) {
        try {
            List<ArticleVendu> articles = articleService.getArticlesEnVente();
            model.addAttribute("articles", articles);

			model.addAttribute("categories", categorieService.getAllCategories());
            return "listeArticles"; 
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erreurs", Collections.singletonList("Erreur de chargement : " + e.getMessage()));
            return "erreur";
        }
    }
}
