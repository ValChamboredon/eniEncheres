//Interface DAO pour la gestion des catégories
package fr.eni.eniEncheres.dal;

import java.util.List;

import fr.eni.eniEncheres.bo.Categorie;

public interface CategorieDAO {
 void ajouterCategorie(Categorie categorie);
 Categorie getCategorieById(int noCategorie);
 List<Categorie> getAllCategories();
 void supprimerCategorie(int noCategorie);
 void mettreAJourCategorie(Categorie categorie);
}
