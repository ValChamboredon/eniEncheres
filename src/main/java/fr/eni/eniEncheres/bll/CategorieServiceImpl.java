package fr.eni.eniEncheres.bll;

import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.dal.CategorieDAO;
import fr.eni.eniEncheres.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

	@Service
	public class CategorieServiceImpl implements CategorieService {
	
		@Autowired
	    private CategorieDAO categorieDAO;
	
	    public CategorieServiceImpl(CategorieDAO categorieDAO) {
	        this.categorieDAO = categorieDAO;
	    }
	
	    @Override
	    public void ajouterCategorie(Categorie categorie) throws BusinessException {
	        BusinessException be = new BusinessException();
	        
	        // Validation du libellé
	        if (categorie.getLibelle() == null || categorie.getLibelle().trim().isEmpty()) {
	            be.addCleErreur("ERR_CATEGORIE_LIBELLE_OBLIGATOIRE");
	        }
	        
	        if (be.getClesErreurs() != null && !be.getClesErreurs().isEmpty()) {
	            throw be;
	        }
	        
	        categorieDAO.ajouterCategorie(categorie);
	    }
	
	    @Override
	    public Categorie getCategorieById(int id) throws BusinessException {
	        return categorieDAO.getCategorieById(id);
	    }
	
	    @Override
	    public List<Categorie> getAllCategories() throws BusinessException {
	        return categorieDAO.getAllCategories();
	    }
	
	    @Override
	    public void supprimerCategorie(int noCategorie) throws BusinessException {
	        BusinessException be = new BusinessException();
	        
	        if (noCategorie <= 0) {
	            be.addCleErreur("ERR_CATEGORIE_ID_INVALIDE");
	        }
	        
	        // On vérifie que la catégorie existe
	        Categorie categorie = categorieDAO.getCategorieById(noCategorie);
	        if (categorie == null) {
	            be.addCleErreur("ERR_CATEGORIE_INEXISTANTE");
	        }
	        
	        if (be.getClesErreurs() != null && !be.getClesErreurs().isEmpty()) {
	            throw be;
	        }
	        
	        try {
	            categorieDAO.supprimerCategorie(noCategorie);
	        } catch (IllegalStateException e) {
	            be.addCleErreur("ERR_CATEGORIE_UTILISEE");
	            throw be;
	        }
	    }
	
	    @Override
	    public void mettreAJourCategorie(Categorie categorie) throws BusinessException {
	        BusinessException be = new BusinessException();
	        
	        if (categorie.getNoCategorie() <= 0) {
	            be.addCleErreur("ERR_CATEGORIE_ID_INVALIDE");
	        }
	        
	        if (categorie.getLibelle() == null || categorie.getLibelle().trim().isEmpty()) {
	            be.addCleErreur("ERR_CATEGORIE_LIBELLE_OBLIGATOIRE");
	        }
	        
	        if (be.getClesErreurs() != null && !be.getClesErreurs().isEmpty()) {
	            throw be;
	        }
	        
	        categorieDAO.mettreAJourCategorie(categorie);
	    }
	}