package fr.eni.eniEncheres.controller;

import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.bll.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        categorieService.createCategorie(categorie);
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
