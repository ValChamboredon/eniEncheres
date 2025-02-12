package fr.eni.eniEncheres.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.eni.eniEncheres.bll.CategorieService;
import fr.eni.eniEncheres.bo.Categorie;

@RestController
@RequestMapping("/categories")
public class CategorieController {

    private final CategorieService categorieService;

    @Autowired
    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    @GetMapping
    public List<Categorie> getAllCategories() {
        return categorieService.getAllCategories();
    }

    @PostMapping
    public void ajouterCategorie(@RequestBody Categorie categorie) {
        categorieService.ajouterCategorie(categorie);
    }

    @PutMapping("/{id}")
    public void mettreAJourCategorie(@PathVariable int id, @RequestBody Categorie categorie) {
        categorie.setNoCategorie(id);
        categorieService.mettreAJourCategorie(categorie);
    }

    @DeleteMapping("/{id}")
    public void supprimerCategorie(@PathVariable int id) {
        categorieService.supprimerCategorie(id);
    }
}
