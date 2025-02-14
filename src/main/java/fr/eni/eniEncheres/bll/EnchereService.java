/**
 * Interface du service métier pour la gestion des enchères.
 * Définit les opérations métiers possibles sur les enchères
 */
package fr.eni.eniEncheres.bll;

import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.exception.BusinessException;

import java.util.List;

public interface EnchereService {
    /**
     * Ajoute une nouvelle enchère.
     * @param enchere L'enchère à ajouter.
     */
    void ajouterEnchere(Enchere enchere) throws BusinessException;

    /**
     * Récupère toutes les enchères associées à un article donné.
     * @param noArticle Identifiant de l'article.
     * @return Liste des enchères associées.
     */
    List<Enchere> getEncheresParArticle(int noArticle) throws BusinessException;

    /**
     * Récupère l'enchère avec le montant le plus élevé pour un article donné.
     * @param noArticle Identifiant de l'article.
     * @return L'enchère avec le montant maximal.
     */
    Enchere getEnchereMaxParArticle(int noArticle) throws BusinessException;

    /**
     * Supprime toutes les enchères liées à un article donné.
     * @param noArticle Identifiant de l'article.
     */
    void supprimerEncheresParArticle(int noArticle) throws BusinessException;

    /**
     * Met à jour une enchère existante.
     * @param enchere L'enchère mise à jour.
     */
    void mettreAJourEnchere(Enchere enchere) throws BusinessException;
    
    void placerEnchere(int articleId, String email, int montantEnchere) throws BusinessException;

	void finaliserEncheresTerminees();

	List<Enchere> getEncheresByUtilisateur(int noUtilisateur) throws BusinessException;
}