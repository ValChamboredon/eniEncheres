package fr.eni.eniEncheres.controller;


import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.eni.eniEncheres.bll.ArticleService;
import fr.eni.eniEncheres.bll.CategorieService;
import fr.eni.eniEncheres.bll.EnchereService;
import fr.eni.eniEncheres.bll.UtilisateurService;
import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.exception.BusinessException;

@RestController
@RequestMapping("/encheres")
public class EnchereController {

    private final EnchereService enchereService;
    
    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private CategorieService categorieService;
    
    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    public EnchereController(EnchereService enchereService) {
        this.enchereService = enchereService;
    }

    @PostMapping("/nouvelle")
    public String nouvelleEnchere(@RequestParam("articleId") int articleId, 
                                  @RequestParam("montantEnchere") int montantEnchere, 
                                  Principal principal,
                                  Model model) {
        try {
            String email = principal.getName();
            enchereService.placerEnchere(articleId, email, montantEnchere);
            return "redirect:/articles/" + articleId;
        } catch (BusinessException be) {
            model.addAttribute("erreurs", be.getClesErreurs());
            return "erreur";
        }
    }

    @GetMapping("/article/{id}")
    public List<Enchere> getEncheresParArticle(@PathVariable int id) throws BusinessException {
        return enchereService.getEncheresParArticle(id);
    }

    @GetMapping("/max/{id}")
    public Enchere getMeilleureEnchere(@PathVariable int id) throws BusinessException {
        return enchereService.getEnchereMaxParArticle(id);
    }
    
    @GetMapping("/mesencheres")
    public String listeEncheresUtilisateur(Model model, Principal principal) {
        try {
            String email = principal.getName();
            Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(email);
            List<Enchere> encheres = enchereService.getEncheresByUtilisateur(utilisateur.getNoUtilisateur());
            model.addAttribute("encheres", encheres);
            return "listeEncheresUtilisateur"; 
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("erreurs", Collections.singletonList("Erreur de chargement : " + e.getMessage()));
            return "erreur";
        }
    }
}