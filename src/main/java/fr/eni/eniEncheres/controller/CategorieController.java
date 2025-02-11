package fr.eni.eniEncheres.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.eni.eniEncheres.bll.CategorieService;
import fr.eni.eniEncheres.bo.Categorie;

@Controller
@RequestMapping("/categories")
public class CategorieController {

	
	
	
    @Autowired
    private CategorieService categorieService;

    @GetMapping
    public List<Categorie> getAllCategories() {
        return categorieService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Categorie getCategorieById(@PathVariable int id) {
        return categorieService.getCategorieById(id);
    }

    @PostMapping
    public void createCategorie(@RequestBody Categorie categorie) {
        if (!isAdmin()) {
            throw new SecurityException("Seuls les administrateurs peuvent créer des catégories.");
        }
        categorieService.createCategorie(categorie);
    }
    private boolean isAdmin() {
        return true; // Ajouter la logique ( no idea)
    }

    @PutMapping("/{id}")
    public void updateCategorie(@PathVariable int id, @RequestBody Categorie categorie) {
        categorieService.updateCategorie(categorie);
    }

    @DeleteMapping("/{id}")
    public void deleteCategorie(@PathVariable int id) {
        categorieService.deleteCategorie(id);
    }
}
