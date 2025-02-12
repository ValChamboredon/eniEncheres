package fr.eni.eniEncheres.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.eni.eniEncheres.bll.EnchereService;
import fr.eni.eniEncheres.bo.Enchere;

@RestController
@RequestMapping("/encheres")
public class EnchereController {

    private final EnchereService enchereService;

    @Autowired
    public EnchereController(EnchereService enchereService) {
        this.enchereService = enchereService;
    }

    @PostMapping("/nouvelle") //Ajoute une nouvelle enchère
    public void ajouterEnchere(@RequestBody Enchere enchere) {
        enchereService.ajouterEnchere(enchere);
    }

    @GetMapping("/article/{id}") //Récupère toutes les enchères sur un article
    public List<Enchere> getEncheresParArticle(@PathVariable int id) {
        return enchereService.getEncheresParArticle(id);
    }

    @GetMapping("/max/{id}") //Récupère l'enchère la plus haute
    public Enchere getMeilleureEnchere(@PathVariable int id) {
        return enchereService.getEnchereMaxParArticle(id);
    }
}
