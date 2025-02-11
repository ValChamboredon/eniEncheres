package fr.eni.eniEncheres.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.eni.eniEncheres.bll.EnchereService;
import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.exception.BusinessException;

@Controller
@RequestMapping("/encheres")
public class EnchereController {

    @Autowired
    private EnchereService enchereService;
    @PostMapping("/nouvelle")
    public String placerEnchere(@ModelAttribute Enchere enchere, BindingResult result, Model model) {
        if (!isUserAuthenticated()) {
            model.addAttribute("erreur", "Vous devez être connecté pour enchérir.");
            return "redirect:/connexion";
        }

        if (result.hasErrors()) {
            model.addAttribute("erreur", "Données invalides.");
            return "formulaireEnchere";
        }

        try {
            enchereService.creerEnchere(enchere);
        } catch (BusinessException e) {
            model.addAttribute("erreur", e.getMessage());
            return "formulaireEnchere";
        }

        return "redirect:/articles/" + enchere.getArticle().getNoArticle();
    }

    private boolean isUserAuthenticated() {
        return true; // Ajouter la logique d’authentification
    }

    @GetMapping("/article/{noArticle}")
    public String voirEncheresParArticle(@PathVariable int noArticle, Model model) {
        List<Enchere> encheres = enchereService.obtenirEncheresParArticle(noArticle);
        model.addAttribute("encheres", encheres);
        return "listeEncheres";
    }

    // Autres méthodes pertinentes
}
