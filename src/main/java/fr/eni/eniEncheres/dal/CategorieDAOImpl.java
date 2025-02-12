package fr.eni.eniEncheres.dal;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.dal.mapper.CategorieRowMapper;
import fr.eni.eniEncheres.exception.BusinessException;

@Repository
public class CategorieDAOImpl implements CategorieDAO {
    private final JdbcTemplate jdbcTemplate;

    public CategorieDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public void ajouterCategorie(Categorie categorie) throws BusinessException {
        try {
            String sql = "INSERT INTO CATEGORIES (libelle) VALUES (?)";
            jdbcTemplate.update(sql, categorie.getLibelle());
        } catch (Exception e) {
            BusinessException be = new BusinessException();
            be.addCleErreur("ERR_INSERTION_CATEGORIE");
            throw be;
        }
    }
    
    @Override
    public Categorie getCategorieById(int noCategorie) throws BusinessException {
        try {
            String sql = "SELECT * FROM CATEGORIES WHERE no_categorie = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{noCategorie}, new CategorieRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            BusinessException be = new BusinessException();
            be.addCleErreur("ERR_RECUPERATION_CATEGORIE");
            throw be;
        }
    }
    
    @Override
    public List<Categorie> getAllCategories() throws BusinessException {
        try {
            String sql = "SELECT * FROM CATEGORIES";
            return jdbcTemplate.query(sql, new CategorieRowMapper());
        } catch (Exception e) {
            BusinessException be = new BusinessException();
            be.addCleErreur("ERR_RECUPERATION_CATEGORIES");
            throw be;
        }
    }
    
    @Override
    public void supprimerCategorie(int noCategorie) throws BusinessException {
        try {
            String checkSql = "SELECT COUNT(*) FROM ARTICLES_VENDUS WHERE no_categorie = ?";
            int count = jdbcTemplate.queryForObject(checkSql, Integer.class, noCategorie);
            
            if (count > 0) {
                BusinessException be = new BusinessException();
                be.addCleErreur("ERR_SUPPRESSION_CATEGORIE_UTILISEE");
                throw be;
            }

            String sql = "DELETE FROM CATEGORIES WHERE no_categorie = ?";
            jdbcTemplate.update(sql, noCategorie);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            BusinessException be = new BusinessException();
            be.addCleErreur("ERR_SUPPRESSION_CATEGORIE");
            throw be;
        }
    }
    
    @Override
    public void mettreAJourCategorie(Categorie categorie) throws BusinessException {
        try {
            String sql = "UPDATE CATEGORIES SET libelle = ? WHERE no_categorie = ?";
            jdbcTemplate.update(sql, categorie.getLibelle(), categorie.getNoCategorie());
        } catch (Exception e) {
            BusinessException be = new BusinessException();
            be.addCleErreur("ERR_MISE_A_JOUR_CATEGORIE");
            throw be;
        }
    }
}