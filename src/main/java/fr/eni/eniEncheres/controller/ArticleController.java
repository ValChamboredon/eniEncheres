package fr.eni.eniEncheres.controller;

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
import fr.eni.eniEncheres.bo.ArticleVendu;

@Controller
@RequestMapping("articles")
public class ArticleController {
	
	@Autowired
	private ArticleService articleService;
	
	@GetMapping
	public String listArticles(Model model) {
		model.addAttribute("articles", articleService.getAllArticles());
		return "listeArticles";
	}
	
	@GetMapping("/{id}")
	public String getArticle(@PathVariable int id, Model model) {
		model.addAttribute("article", articleService.getArticleById(id));
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
	

}
