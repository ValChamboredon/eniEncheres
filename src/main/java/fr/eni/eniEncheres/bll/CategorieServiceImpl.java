/**
 * Implémentation du service de gestion des catégories.
 * Cette classe fournit les opérations métier sur les catégories (CRUD).
 * 
 * @author Mariami
 * @version 1.0
 */
package fr.eni.eniEncheres.bll;

import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.dal.CategorieDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Annotation @Service permet à Spring de gérer cette classe en tant que service.
 */
@Service
public class CategorieServiceImpl implements CategorieService {

    // Dépendance vers la couche DAO pour interagir avec la base de données.
    private final CategorieDAO categorieDAO;

    /**
     * Injection de dépendance de `CategorieDAO` via le constructeur.
     * @param categorieDAO DAO des catégories.
     */
    @Autowired
    public CategorieServiceImpl(CategorieDAO categorieDAO) {
        this.categorieDAO = categorieDAO;
    }

    /**
     * Ajoute une nouvelle catégorie à la base.
     * @param categorie La catégorie à ajouter.
     */
    @Override
    public void ajouterCategorie(Categorie categorie) {
        categorieDAO.ajouterCategorie(categorie);
    }

    /**
     * Récupère une catégorie spécifique par son identifiant.
     * @param noCategorie Identifiant de la catégorie.
     * @return La catégorie correspondante.
     */
    @Override
    public Categorie getCategorieById(int noCategorie) {
        return categorieDAO.getCategorieById(noCategorie);
    }

    /**
     * Récupère toutes les catégories disponibles.
     * @return Liste de toutes les catégories.
     */
    @Override
    public List<Categorie> getAllCategories() {
        return categorieDAO.getAllCategories();
    }

    /**
     * Supprime une catégorie de la base.
     * @param noCategorie Identifiant de la catégorie à supprimer.
     */
    @Override
    public void supprimerCategorie(int noCategorie) {
        categorieDAO.supprimerCategorie(noCategorie);
    }

    /**
     * Met à jour une catégorie existante.
     * @param categorie La catégorie mise à jour.
     */
    @Override
    public void mettreAJourCategorie(Categorie categorie) {
        categorieDAO.mettreAJourCategorie(categorie);
    }
}
