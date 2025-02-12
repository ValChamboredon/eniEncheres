package fr.eni.eniEncheres.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import fr.eni.eniEncheres.bll.CategorieService;
import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.exception.BusinessException;

@Controller
@RequestMapping("/categories")
public class CategorieController {
    private final CategorieService categorieService;

    @Autowired
    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @GetMapping
    public String getAllCategories(Model model) throws BusinessException {
        model.addAttribute("categories", categorieService.getAllCategories());
        return "categories/liste";
    }

    @GetMapping("/ajouter")
    public String afficherFormulaireAjout(Model model) {
        model.addAttribute("categorie", new Categorie());
        return "categories/formulaire";
    }

    @PostMapping("/ajouter")
    public String ajouterCategorie(@ModelAttribute Categorie categorie) throws BusinessException {
        categorieService.ajouterCategorie(categorie);
        return "redirect:/categories";
    }

    @GetMapping("/modifier/{id}")
    public String afficherFormulaireModification(@PathVariable int id, Model model) throws BusinessException {
        model.addAttribute("categorie", categorieService.getCategorieById(id));
        return "categories/formulaire";
    }

    @PostMapping("/modifier/{id}")
    public String modifierCategorie(@PathVariable int id, @ModelAttribute Categorie categorie) throws BusinessException {
        categorie.setNoCategorie(id);
        categorieService.mettreAJourCategorie(categorie);
        return "redirect:/categories";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimerCategorie(@PathVariable int id) throws BusinessException {
        categorieService.supprimerCategorie(id);
        return "redirect:/categories";
    }
    
    @ModelAttribute("categories")
    public List<Categorie> getAllCategories() throws BusinessException {
        return categorieService.getAllCategories();
    }
}