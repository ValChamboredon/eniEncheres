/**
 * Interface du service métier pour la gestion des catégories.
 * Déclare les méthodes utilisées pour la gestion des catégories dans l'application.
 * 
 * @author Mariami
 * @version 1.0
 */
package fr.eni.eniEncheres.bll;

import fr.eni.eniEncheres.bo.Categorie;
import java.util.List;

public interface CategorieService {
    /**
     * Ajoute une nouvelle catégorie.
     * @param categorie La catégorie à ajouter.
     */
    void ajouterCategorie(Categorie categorie);

    /**
     * Récupère une catégorie par son identifiant.
     * @param noCategorie Identifiant de la catégorie.
     * @return La catégorie trouvée ou null si inexistante.
     */
    Categorie getCategorieById(int noCategorie);

    /**
     * Récupère la liste de toutes les catégories.
     * @return Liste des catégories enregistrées.
     */
    List<Categorie> getAllCategories();

    /**
     * Supprime une catégorie existante.
     * @param noCategorie Identifiant de la catégorie à supprimer.
     */
    void supprimerCategorie(int noCategorie);

    /**
     * Met à jour une catégorie existante.
     * @param categorie La catégorie mise à jour.
     */
    void mettreAJourCategorie(Categorie categorie);
}
