package fr.eni.eniEncheres.dal;

import fr.eni.eniEncheres.bo.Categorie;
import java.util.List;

public interface CategorieDAO {
	
	
    Categorie findById(int id);
    List<Categorie> findAll();
    
    void save(Categorie categorie);
    void update(Categorie categorie);
    void delete(int id);
}
