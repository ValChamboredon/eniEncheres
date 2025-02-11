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
        String sql = "SELECT no_categorie, libelle FROM Categorie WHERE no_categorie = ?";
        return jdbcTemplate.queryForObject(sql, categorieRowMapper, id);
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
        String sql = "DELETE FROM Categorie WHERE no_categorie = ?";
        jdbcTemplate.update(sql, id);
    }
}
