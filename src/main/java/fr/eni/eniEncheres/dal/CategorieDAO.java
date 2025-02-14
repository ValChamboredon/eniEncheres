package fr.eni.eniEncheres.dal;

import java.util.List;
import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.exception.BusinessException;

/**
 * ✅ Interface DAO pour la gestion des catégories.
 * 
 * Cette interface définit les opérations CRUD pour les catégories d'articles.
 */
public interface CategorieDAO {

    /**
     * ✅ Ajoute une nouvelle catégorie.
     * 
     * @param categorie La catégorie à ajouter.
     * @throws BusinessException En cas d'erreur lors de l'ajout.
     */
    void ajouterCategorie(Categorie categorie) throws BusinessException;

    /**
     * ✅ Récupère une catégorie par son ID.
     * 
     * @param noCategorie L'ID de la catégorie.
     * @return La catégorie correspondante.
     * @throws BusinessException En cas d'erreur.
     */
    Categorie getCategorieById(int noCategorie) throws BusinessException;

    /**
     * ✅ Récupère toutes les catégories disponibles.
     * 
     * @return Liste de toutes les catégories.
     */
    List<Categorie> getAllCategories();

    /**
     * ✅ Supprime une catégorie par son ID.
     * 
     * @param noCategorie L'ID de la catégorie.
     * @throws BusinessException Si la suppression n'est pas possible (ex: catégorie associée à des articles).
     */
    void supprimerCategorie(int noCategorie) throws BusinessException;

    /**
     * ✅ Met à jour une catégorie existante.
     * 
     * @param categorie La catégorie mise à jour.
     * @throws BusinessException En cas d'erreur.
     */
    void mettreAJourCategorie(Categorie categorie) throws BusinessException;
}
