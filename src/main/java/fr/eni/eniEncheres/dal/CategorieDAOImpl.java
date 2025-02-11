package fr.eni.eniEncheres.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import fr.eni.eniEncheres.bo.Categorie;

@Repository
public class CategorieDAOImpl implements CategorieDAO {

    private final JdbcTemplate jdbcTemplate;

    public CategorieDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Categorie> categorieRowMapper = new RowMapper<Categorie>() {
        @Override
        public Categorie mapRow(ResultSet rs, int rowNum) throws SQLException {
            Categorie categorie = new Categorie();
            categorie.setNoCategorie(rs.getInt("no_categorie"));
            categorie.setLibelle(rs.getString("libelle"));
            return categorie;
        }
    };

    @Override
    public Categorie findById(int id) {
        String sql = "SELECT no_categorie, libelle FROM CATEGORIES WHERE no_categorie = ?";
        List<Categorie> categories = jdbcTemplate.query(sql, categorieRowMapper, id);
        return categories.isEmpty() ? null : categories.get(0);
    }

    @Override
    public List<Categorie> findAll() {
        String sql = "SELECT no_categorie, libelle FROM Categorie";
        return jdbcTemplate.query(sql, categorieRowMapper);
    }

    @Override
    public void save(Categorie categorie) {
        String sql = "INSERT INTO Categorie (libelle) VALUES (?)";
        jdbcTemplate.update(sql, categorie.getLibelle());
    }

    @Override
    public void update(Categorie categorie) {
        String sql = "UPDATE Categorie SET libelle = ? WHERE no_categorie = ?";
        jdbcTemplate.update(sql, categorie.getLibelle(), categorie.getNoCategorie());
    }

    @Override
    public void delete(int id) {
        String sqlCheck = "SELECT COUNT(*) FROM ARTICLES_VENDUS WHERE no_categorie = ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, id);
        if (count > 0) {
            throw new IllegalStateException("Impossible de supprimer une catégorie utilisée.");
        }
        
        String sql = "DELETE FROM CATEGORIES WHERE no_categorie = ?";
        jdbcTemplate.update(sql, id);
    }

}
