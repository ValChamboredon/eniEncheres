package fr.eni.eniEncheres.dal;

import java.util.List;

import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.exception.BusinessException;

public interface CategorieDAO {
 void ajouterCategorie(Categorie categorie) throws BusinessException;
 Categorie getCategorieById(int noCategorie) throws BusinessException; // itération 1
 List<Categorie> getAllCategories(); // itération 1
 void supprimerCategorie(int noCategorie) throws BusinessException;
 void mettreAJourCategorie(Categorie categorie) throws BusinessException;
}