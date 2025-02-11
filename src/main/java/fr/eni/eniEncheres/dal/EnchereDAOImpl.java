package fr.eni.eniEncheres.dal;

import fr.eni.eniEncheres.bo.Enchere;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EnchereDAOImpl implements EnchereDAO {

    private final JdbcTemplate jdbcTemplate;

    public EnchereDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Enchere> enchereRowMapper = new RowMapper<Enchere>() {
        @Override
        public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
            Enchere enchere = new Enchere();
            enchere.setNoEnchere(rs.getInt("no_enchere"));
            enchere.setDateEnchere(rs.getTimestamp("date_enchere").toLocalDateTime());
            enchere.setMontantEnchere(rs.getInt("montant_enchere"));
            // Assurez-vous de mapper les relations avec Utilisateur et ArticleVendu si nécessaire
            return enchere;
        }
    };

    @Override
    public void save(Enchere enchere) {
        String sql = "INSERT INTO Encheres (date_enchere, montant_enchere, no_utilisateur, no_article) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            enchere.getDateEnchere(),
            enchere.getMontantEnchere(),
            enchere.getUtilisateur().getNoUtilisateur(),
            enchere.getArticle().getNoArticle());
    }

    @Override
    public List<Enchere> findByArticleId(int noArticle) {
        String sql = "SELECT * FROM Encheres WHERE no_article = ?";
        return jdbcTemplate.query(sql, enchereRowMapper, noArticle);
    }

    @Override
    public Enchere findHighestBidByArticleId(int noArticle) {
        String sql = "SELECT * FROM Encheres WHERE no_article = ? ORDER BY montant_enchere DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sql, enchereRowMapper, noArticle);
    }
}
