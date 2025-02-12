package fr.eni.eniEncheres.dal;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.dal.mapper.EnchereRowMapper;

@Repository
public class EnchereDAOImpl implements EnchereDAO {
    private final JdbcTemplate jdbcTemplate;

    public EnchereDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void ajouterEnchere(Enchere enchere) {
        String sql = "INSERT INTO ENCHERES (no_utilisateur, no_article, date_enchere, montant_enchere) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, 
            enchere.getUtilisateur().getNoUtilisateur(), 
            enchere.getArticle().getNoArticle(), 
            enchere.getDateEnchere(), 
            enchere.getMontantEnchere()
        );
    }

    @Override
    public List<Enchere> getEncheresParArticle(int noArticle) {
        String sql = "SELECT e.*, u.pseudo, u.nom, u.prenom, a.nom_article " +
                     "FROM ENCHERES e " +
                     "JOIN UTILISATEURS u ON e.no_utilisateur = u.no_utilisateur " +
                     "JOIN ARTICLES_VENDUS a ON e.no_article = a.no_article " +
                     "WHERE e.no_article = ?";
        return jdbcTemplate.query(sql, new Object[]{noArticle}, new EnchereRowMapper());
    }

    @Override
    public Enchere getEnchereMaxParArticle(int noArticle) {
        try {
            String sql = "SELECT e.*, u.pseudo, u.nom, u.prenom, a.nom_article " +
                         "FROM ENCHERES e " +
                         "JOIN UTILISATEURS u ON e.no_utilisateur = u.no_utilisateur " +
                         "JOIN ARTICLES_VENDUS a ON e.no_article = a.no_article " +
                         "WHERE e.no_article = ? ORDER BY e.montant_enchere DESC LIMIT 1";
            return jdbcTemplate.queryForObject(sql, new Object[]{noArticle}, new EnchereRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null; // ✅ Retourne null si aucune enchère trouvée
        }
    }

    @Override
    public void supprimerEncheresParArticle(int noArticle) {
        // ✅ Vérifier s'il y a des enchères avant de les supprimer
        String checkSql = "SELECT COUNT(*) FROM ENCHERES WHERE no_article = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, noArticle);
        
        if (count > 0) {
            String sql = "DELETE FROM ENCHERES WHERE no_article = ?";
            jdbcTemplate.update(sql, noArticle);
        }
    }

    @Override
    public void mettreAJourEnchere(Enchere enchere) {
        String sql = "UPDATE ENCHERES SET montant_enchere = ?, date_enchere = ? WHERE no_utilisateur = ? AND no_article = ?";
        jdbcTemplate.update(sql, 
            enchere.getMontantEnchere(), 
            enchere.getDateEnchere(), 
            enchere.getUtilisateur().getNoUtilisateur(), 
            enchere.getArticle().getNoArticle()
        );
    }
}
