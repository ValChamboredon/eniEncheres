package fr.eni.eniEncheres.bll;

import fr.eni.eniEncheres.bo.ArticleVendu;
import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.exception.BusinessException;
import java.util.List;

public interface EnchereService {
    /**
     * Ajoute une nouvelle enchère.
     * Vérifie que le montant est supérieur à l'enchère actuelle,
     * que l'utilisateur a assez de crédit,
     * et gère le remboursement de l'enchérisseur précédent.
     *
     * @param enchere L'enchère à ajouter
     * @throws BusinessException si l'enchère n'est pas valide ou si l'utilisateur n'a pas assez de crédit
     */
    void ajouterEnchere(Enchere enchere) throws BusinessException;

    /**
     * Récupère toutes les enchères pour un article
     */
    List<Enchere> getEncheresParArticle(ArticleVendu noArticle) throws BusinessException;

    /**
     * Récupère l'enchère la plus élevée pour un article
     */
  //  Enchere getEnchereMaxParArticle(int noArticle) throws BusinessException;

    /**
     * Supprime toutes les enchères d'un article
     */
   // void supprimerEncheresParArticle(int noArticle) throws BusinessException;

    /**
     * Met à jour une enchère existante
     */
    void mettreAJourEnchere(Enchere enchere) throws BusinessException;

    /**
     * Récupère les enchères d'un utilisateur
     */
  //  List<Enchere> getEncheresByUtilisateur(int noUtilisateur) throws BusinessException;
}