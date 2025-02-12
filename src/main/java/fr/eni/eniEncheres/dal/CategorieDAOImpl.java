// Implémentation DAO pour Categorie
package fr.eni.eniEncheres.dal;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.dal.mapper.CategorieRowMapper;

@Repository
public class CategorieDAOImpl implements CategorieDAO {
    private final JdbcTemplate jdbcTemplate;

    public CategorieDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public void ajouterCategorie(Categorie categorie) {
        String sql = "INSERT INTO CATEGORIES (libelle) VALUES (?)";
        jdbcTemplate.update(sql, categorie.getLibelle());
    }
    
    @Override
    public Categorie getCategorieById(int noCategorie) {
        try {
            String sql = "SELECT * FROM CATEGORIES WHERE no_categorie = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{noCategorie}, new CategorieRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null; // Renvoie null si aucune catégorie n'est trouvée
        }
    }
    
    @Override
    public List<Categorie> getAllCategories() {
        String sql = "SELECT * FROM CATEGORIES";
        return jdbcTemplate.query(sql, new CategorieRowMapper());
    }
    
    @Override
    public void supprimerCategorie(int noCategorie) {
        //  Vérifier si la catégorie est utilisée avant suppression
        String checkSql = "SELECT COUNT(*) FROM ARTICLES_VENDUS WHERE no_categorie = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, noCategorie);
        
        if (count > 0) {
            throw new IllegalStateException("Impossible de supprimer une catégorie utilisée par des articles.");
        }

        String sql = "DELETE FROM CATEGORIES WHERE no_categorie = ?";
        jdbcTemplate.update(sql, noCategorie);
    }
    
    @Override
    public void mettreAJourCategorie(Categorie categorie) {
        String sql = "UPDATE CATEGORIES SET libelle = ? WHERE no_categorie = ?";
        jdbcTemplate.update(sql, categorie.getLibelle(), categorie.getNoCategorie());
    }
}
