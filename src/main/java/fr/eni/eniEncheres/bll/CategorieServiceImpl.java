package fr.eni.eniEncheres.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.dal.CategorieDAO;

@Service
public class CategorieServiceImpl implements CategorieService {

    private final CategorieDAO categorieDAO;

    public CategorieServiceImpl(CategorieDAO categorieDAO) {
        this.categorieDAO = categorieDAO;
    }

    @Override
    public Categorie getCategorieById(int id) {
        Categorie categorie = categorieDAO.findById(id);
        if (categorie == null) {
            throw new IllegalArgumentException("Catégorie introuvable pour l'ID " + id);
        }
        return categorie;
    }


    @Override
    public List<Categorie> getAllCategories() {
        return categorieDAO.findAll();
    }

    //Accessible aux admins 
       
    
    
    private boolean isAdmin() {
        // Logique pour vérifier si l'utilisateur actuel est admin
        return true; // Modifier pour une vraie vérification
    }
    

    @Override
    public void createCategorie(Categorie categorie) {
        if (!isAdmin()) {
            throw new SecurityException("Seuls les administrateurs peuvent créer des catégories.");
        }
        categorieDAO.save(categorie);
    }

    @Override
    public void updateCategorie(Categorie categorie) {
    	if (!isAdmin()) {
    		throw new SecurityException("Seuls les administrateurs peuvent mettre à jour des catégories.");
    }
    categorieDAO.save(categorie);
        categorieDAO.update(categorie);
    }

    @Override
    public void deleteCategorie(int id) {
    	if (!isAdmin()) {
           throw new SecurityException("Seuls les administrateurs peuvent supprimer à jour des catégories.");
    	}        
    	categorieDAO.delete(id);
    }
}

