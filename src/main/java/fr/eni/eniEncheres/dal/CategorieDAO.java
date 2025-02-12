//Interface DAO pour la gestion des catégories
package fr.eni.eniEncheres.dal;

import java.util.List;

import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.exception.BusinessException;

public interface CategorieDAO {
 void ajouterCategorie(Categorie categorie) throws BusinessException;
 Categorie getCategorieById(int noCategorie) throws BusinessException;
 List<Categorie> getAllCategories() throws BusinessException;
 void supprimerCategorie(int noCategorie) throws BusinessException;
 void mettreAJourCategorie(Categorie categorie) throws BusinessException;
}
