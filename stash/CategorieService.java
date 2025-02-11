package fr.eni.eniEncheres.bll;

import fr.eni.eniEncheres.bo.Categorie;
import java.util.List;

public interface CategorieService {
    
	Categorie getCategorieById(int id);
    List<Categorie> getAllCategories();
    
    //Seul le membre admin pourra gerer les catégories
    void createCategorie(Categorie categorie);
    void updateCategorie(Categorie categorie);
    void deleteCategorie(int id);
}
