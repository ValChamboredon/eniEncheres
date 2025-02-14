package fr.eni.eniEncheres.dal;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.eniEncheres.bo.Categorie;
import fr.eni.eniEncheres.dal.mapper.CategorieRowMapper;
import fr.eni.eniEncheres.exception.BusinessException;

/**
 * ✅ Implémentation de l'interface CategorieDAO pour gérer les catégories.
 */
@Repository
public class CategorieDAOImpl implements CategorieDAO {
    
    private final JdbcTemplate jdbcTemplate;

    public CategorieDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * ✅ Ajoute une nouvelle catégorie en base de données.
     * 
     * @param categorie La catégorie à ajouter.
     * @throws BusinessException En cas d'erreur lors de l'ajout.
     */
    @Override
    public void ajouterCategorie(Categorie categorie) throws BusinessException {
        try {
            String sql = "INSERT INTO CATEGORIES (libelle) VALUES (?)";
            jdbcTemplate.update(sql, categorie.getLibelle());
        } catch (Exception e) {
            throw new BusinessException("Erreur lors de l'ajout de la catégorie.");
        }
    }

    /**
     * ✅ Récupère une catégorie par son ID.
     * 
     * @param noCategorie L'ID de la catégorie.
     * @return La catégorie correspondante.
     */
    @Override
    public Categorie getCategorieById(int noCategorie) throws BusinessException {
        try {
            String sql = "SELECT * FROM CATEGORIES WHERE no_categorie = ?";
            return jdbcTemplate.queryForObject(sql, new CategorieRowMapper(), noCategorie);
        } catch (EmptyResultDataAccessException e) {
            throw new BusinessException("Catégorie introuvable.");
        }
    }

    /**
     * ✅ Récupère toutes les catégories disponibles.
     * 
     * @return Liste de toutes les catégories.
     */
    @Override
    public List<Categorie> getAllCategories() {
        String sql = "SELECT * FROM CATEGORIES";
        return jdbcTemplate.query(sql, new CategorieRowMapper());
    }

    /**
     * ✅ Supprime une catégorie par son ID.
     * 
     * @param noCategorie L'ID de la catégorie.
     * @throws BusinessException Si la suppression n'est pas possible (ex: catégorie associée à des articles).
     */
    @Override
    public void supprimerCategorie(int noCategorie) throws BusinessException {
        try {
            String checkSql = "SELECT COUNT(*) FROM ARTICLES_VENDUS WHERE no_categorie = ?";
            int count = jdbcTemplate.queryForObject(checkSql, Integer.class, noCategorie);

            if (count > 0) {
                throw new BusinessException("Impossible de supprimer cette catégorie, elle est utilisée.");
            }

            String sql = "DELETE FROM CATEGORIES WHERE no_categorie = ?";
            jdbcTemplate.update(sql, noCategorie);
        } catch (Exception e) {
            throw new BusinessException("Erreur lors de la suppression de la catégorie.");
        }
    }

    /**
     * ✅ Met à jour une catégorie existante.
     * 
     * @param categorie La catégorie mise à jour.
     * @throws BusinessException En cas d'erreur.
     */
    @Override
    public void mettreAJourCategorie(Categorie categorie) throws BusinessException {
        try {
            String sql = "UPDATE CATEGORIES SET libelle = ? WHERE no_categorie = ?";
            jdbcTemplate.update(sql, categorie.getLibelle(), categorie.getNoCategorie());
        } catch (Exception e) {
            throw new BusinessException("Erreur lors de la mise à jour de la catégorie.");
        }
    }
}
