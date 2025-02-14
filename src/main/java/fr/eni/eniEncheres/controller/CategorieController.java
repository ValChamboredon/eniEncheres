package fr.eni.eniEncheres.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import fr.eni.eniEncheres.bll.CategorieService;
import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.exception.BusinessException;

/**
 * ✅ Contrôleur gérant les catégories pour les enchères.
 */
@Controller
@RequestMapping("/categories")
public class CategorieController {
    private final CategorieService categorieService;

    @Autowired
    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    /**
     * ✅ Affiche la liste des catégories.
     *
     * @param model Modèle pour afficher les catégories.
     * @return La vue contenant la liste des catégories.
     * @throws BusinessException En cas d'erreur de récupération.
     */
    @GetMapping
    public String getAllCategories(Model model) throws BusinessException {
        model.addAttribute("categories", categorieService.getAllCategories());
        return "categories/liste";
    }

    /**
     * ✅ Affiche le formulaire d'ajout d'une nouvelle catégorie.
     *
     * @param model Modèle contenant l'objet catégorie.
     * @return La page du formulaire d'ajout.
     */
    @GetMapping("/ajouter")
    public String afficherFormulaireAjout(Model model) {
        model.addAttribute("categorie", new Categorie());
        return "categories/formulaire";
    }

    /**
     * ✅ Ajoute une nouvelle catégorie en base de données.
     *
     * @param categorie La catégorie à ajouter.
     * @return Redirection vers la liste des catégories.
     * @throws BusinessException En cas d'erreur métier.
     */
    @PostMapping("/ajouter")
    public String ajouterCategorie(@ModelAttribute Categorie categorie) throws BusinessException {
        categorieService.ajouterCategorie(categorie);
        return "redirect:/categories";
    }
}
