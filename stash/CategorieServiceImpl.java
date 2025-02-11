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
        return categorieDAO.findById(id);
    }

    @Override
    public List<Categorie> getAllCategories() {
        return categorieDAO.findAll();
    }

    @Override
    public void createCategorie(Categorie categorie) {
        categorieDAO.save(categorie);
    }

    @Override
    public void updateCategorie(Categorie categorie) {
        categorieDAO.update(categorie);
    }

    @Override
    public void deleteCategorie(int id) {
        categorieDAO.delete(id);
    }
}
