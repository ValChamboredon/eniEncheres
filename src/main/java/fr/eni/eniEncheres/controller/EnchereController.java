package fr.eni.eniEncheres.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import fr.eni.eniEncheres.bll.EnchereService;
import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.exception.BusinessException;

/**
 * ✅ Contrôleur gérant les enchères sur les articles.
 */
@RestController
@RequestMapping("/encheres")
public class EnchereController {

    private final EnchereService enchereService;

    @Autowired
    public EnchereController(EnchereService enchereService) {
        this.enchereService = enchereService;
    }

    /**
     * ✅ Ajoute une nouvelle enchère.
     *
     * @param enchere L'enchère soumise.
     * @throws BusinessException En cas d'erreur de validation.
     */
    @PostMapping("/nouvelle")
    public void ajouterEnchere(@RequestBody Enchere enchere) throws BusinessException {
        enchereService.ajouterEnchere(enchere);
    }

    /**
     * ✅ Récupère la liste des enchères sur un article donné.
     *
     * @param id L'identifiant de l'article.
     * @return Liste des enchères.
     * @throws BusinessException En cas d'erreur.
     */
    @GetMapping("/article/{id}")
    public List<Enchere> getEncheresParArticle(@PathVariable int id) throws BusinessException {
        return enchereService.getEncheresParArticle(id);
    }

    /**
     * ✅ Récupère la meilleure enchère pour un article donné.
     *
     * @param id L'identifiant de l'article.
     * @return L'enchère la plus haute.
     * @throws BusinessException En cas d'erreur.
     */
    @GetMapping("/max/{id}")
    public Enchere getMeilleureEnchere(@PathVariable int id) throws BusinessException {
        return enchereService.getEnchereMaxParArticle(id);
    }
}
