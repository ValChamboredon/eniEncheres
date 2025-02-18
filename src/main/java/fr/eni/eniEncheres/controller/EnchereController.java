package fr.eni.eniEncheres.controller;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.eni.eniEncheres.bll.ArticleService;
import fr.eni.eniEncheres.bll.CategorieService;
import fr.eni.eniEncheres.bll.EnchereService;
import fr.eni.eniEncheres.bll.UtilisateurService;
import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.bo.EtatVente;
import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.exception.BusinessException;

@Controller
@RequestMapping("/encheres")
public class EnchereController {

    private final EnchereService enchereService;
    private final ArticleService articleService;
    private final UtilisateurService utilisateurService;

    @Autowired
    public EnchereController(EnchereService enchereService, ArticleService articleService, UtilisateurService utilisateurService) {
        this.enchereService = enchereService;
        this.articleService = articleService;
        this.utilisateurService = utilisateurService;
    }

//    @PostMapping("/article/{id}")
//    public void ajouterEnchere(@RequestBody Enchere enchere) throws BusinessException {
//        enchereService.ajouterEnchere(enchere);
//    }
    
//    @PostMapping("/encherir")
//    public String encherir(@RequestParam("articleId") int articleId,
//                          @RequestParam("montantEnchere") int montantEnchere,
//                          RedirectAttributes redirectAttributes) {
//    	System.out.println("Début méthode encherir - articleId: " + articleId + ", montant: " + montantEnchere);
//        try {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            System.out.println("Auth: " + (auth != null ? auth.getName() : "null"));
//            if (auth == null || !auth.isAuthenticated()) {
//            	System.out.println("Utilisateur non authentifié");
//                return "redirect:/connexion";
//            }
//
//            // Récupération de l'utilisateur connecté
//            Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(auth.getName());
//            System.out.println("Utilisateur récupéré: " + utilisateur.getPseudo());
//            
//            // Récupération de l'article
//            ArticleVendu article = articleService.getArticleById(articleId);
//            System.out.println("Article récupéré: " + article.getNomArticle() + ", état: " + article.getEtatVente());
//            
//         // Vérification que l'article est bien EN_COURS
//            if (article.getEtatVente() != EtatVente.EN_COURS) {
//            	System.out.println("Article non en vente: " + article.getEtatVente());
//                redirectAttributes.addFlashAttribute("erreur", "ERR_ARTICLE_NON_EN_VENTE");
//                return "redirect:/encheres/article/detail/" + articleId;
//            }
//            
//            // Création de l'enchère
//            Enchere enchere = new Enchere();
//            enchere.setArticle(article);
//            enchere.setUtilisateur(utilisateur);
//            enchere.setMontantEnchere(montantEnchere);
//            enchere.setDateEnchere(LocalDateTime.now());
//            System.out.println("Enchère créée: " + enchere);
//
//            // Sauvegarde de l'enchère
//            enchereService.ajouterEnchere(enchere);
//            System.out.println("Enchère sauvegardée avec succès");
//            
//            redirectAttributes.addFlashAttribute("success", "Votre enchère a été enregistrée avec succès!");
//            return "redirect:/encheres/article/detail/" + articleId;
//            
//        } catch (BusinessException be) {
//            System.out.println("BusinessException: " + be.getClesErreurs());
//            be.printStackTrace();
//            redirectAttributes.addFlashAttribute("erreurs", be.getClesErreurs());
//            return "redirect:/encheres/article/detail/" + articleId;
//        } catch (Exception e) {
//            System.out.println("Exception générale: " + e.getMessage());
//            e.printStackTrace();
//            redirectAttributes.addFlashAttribute("erreur", "Erreur technique");
//            return "redirect:/encheres/article/detail/" + articleId;
//        }
//    }


//    @GetMapping("/article/{id}")
//    public List<Enchere> getEncheresParArticle(@PathVariable int id) throws BusinessException {
//        return enchereService.getEncheresParArticle(id);
//    }
//
//    @GetMapping("/max/{id}")
//    public Enchere getMeilleureEnchere(@PathVariable int id) throws BusinessException {
//        return enchereService.getEnchereMaxParArticle(id);
//    }
    
    
}

