package fr.eni.eniEncheres.bll;

import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.exception.BusinessException;
import java.util.List;

public interface CategorieService {
    /**
     * Ajoute une nouvelle catégorie.
     * @param categorie La catégorie à ajouter.
     * @throws BusinessException en cas d'erreur lors de l'ajout
     */
    void ajouterCategorie(Categorie categorie) throws BusinessException;

    /**
     * Récupère une catégorie par son identifiant.
     * @param noCategorie Identifiant de la catégorie.
     * @return La catégorie trouvée ou null si inexistante.
     * @throws BusinessException en cas d'erreur lors de la récupération
     */
    Categorie getCategorieById(int id) throws BusinessException;

    /**
     * Récupère la liste de toutes les catégories.
     * @return Liste des catégories enregistrées.
     * @throws BusinessException en cas d'erreur lors de la récupération
     */
    List<Categorie> getAllCategories() throws BusinessException;

    /**
     * Supprime une catégorie existante.
     * @param noCategorie Identifiant de la catégorie à supprimer.
     * @throws BusinessException en cas d'erreur lors de la suppression
     */
    void supprimerCategorie(int noCategorie) throws BusinessException;

    /**
     * Met à jour une catégorie existante.
     * @param categorie La catégorie mise à jour.
     * @throws BusinessException en cas d'erreur lors de la mise à jour
     */
    void mettreAJourCategorie(Categorie categorie) throws BusinessException;
}