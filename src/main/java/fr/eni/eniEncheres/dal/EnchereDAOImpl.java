//package fr.eni.eniEncheres.dal;
//
//import java.util.List;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import fr.eni.eniEncheres.bo.Enchere;
//import fr.eni.eniEncheres.dal.mapper.EnchereRowMapper;
//import fr.eni.eniEncheres.exception.BusinessException;
//
//@Repository
//public class EnchereDAOImpl implements EnchereDAO {
//    private final JdbcTemplate jdbcTemplate;
//
//    public EnchereDAOImpl(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public void ajouterEnchere(Enchere enchere) throws BusinessException {
//        try {
//            String sql = "INSERT INTO ENCHERES (no_utilisateur, no_article, date_enchere, montant_enchere) VALUES (?, ?, ?, ?)";
//            jdbcTemplate.update(sql, 
//                enchere.getUtilisateur().getNoUtilisateur(),
//                enchere.getArticle().getNoArticle(),
//                enchere.getDateEnchere(),
//                enchere.getMontantEnchere()
//            );
//        } catch (Exception e) {
//            BusinessException be = new BusinessException();
//            be.addCleErreur("ERR_INSERTION_ENCHERE");
//            throw be;
//        }
//    }
//
//    @Override
//    public List<Enchere> getEncheresParArticle(int noArticle) throws BusinessException {
//        try {
//        	String sql = "SELECT e.*, u.pseudo, u.nom, u.prenom, " +
//                    "a.nom_article, a.description, a.date_debut_encheres, a.date_fin_encheres, " +
//                    "c.no_categorie, c.libelle " +
//                    "FROM ENCHERES e " +
//                    "JOIN UTILISATEURS u ON e.no_utilisateur = u.no_utilisateur " +
//                    "JOIN ARTICLES_VENDUS a ON e.no_article = a.no_article " +
//                    "JOIN CATEGORIES c ON a.no_categorie = c.no_categorie " +
//                    "WHERE e.no_article = ?";
//            return jdbcTemplate.query(sql, new Object[]{noArticle}, new EnchereRowMapper());
//        } catch (Exception e) {
//            BusinessException be = new BusinessException();
//            be.addCleErreur("ERR_RECUPERATION_ENCHERES");
//            throw be;
//        }
//    }
//
//    @Override
//    public Enchere getEnchereMaxParArticle(int noArticle) throws BusinessException {
//        try {
//            String sql = "SELECT e.*, u.pseudo, u.nom, u.prenom, a.nom_article " +
//                        "FROM ENCHERES e " +
//                        "JOIN UTILISATEURS u ON e.no_utilisateur = u.no_utilisateur " +
//                        "JOIN ARTICLES_VENDUS a ON e.no_article = a.no_article " +
//                        "WHERE e.no_article = ? " +
//                        "ORDER BY e.montant_enchere DESC LIMIT 1";
//            return jdbcTemplate.queryForObject(sql, new Object[]{noArticle}, new EnchereRowMapper());
//        } catch (EmptyResultDataAccessException e) {
//            return null;
//        } catch (Exception e) {
//            BusinessException be = new BusinessException();
//            be.addCleErreur("ERR_RECUPERATION_MEILLEURE_ENCHERE");
//            throw be;
//        }
//    }
//
//    @Override
//    public void supprimerEncheresParArticle(int noArticle) throws BusinessException {
//        try {
//            String sql = "DELETE FROM ENCHERES WHERE no_article = ?";
//            jdbcTemplate.update(sql, noArticle);
//        } catch (Exception e) {
//            BusinessException be = new BusinessException();
//            be.addCleErreur("ERR_SUPPRESSION_ENCHERES");
//            throw be;
//        }
//    }
//
//    @Override
//    public void mettreAJourEnchere(Enchere enchere) throws BusinessException {
//        try {
//            String sql = "UPDATE ENCHERES SET montant_enchere = ?, date_enchere = ? " +
//                        "WHERE no_utilisateur = ? AND no_article = ?";
//            jdbcTemplate.update(sql,
//                enchere.getMontantEnchere(),
//                enchere.getDateEnchere(),
//                enchere.getUtilisateur().getNoUtilisateur(),
//                enchere.getArticle().getNoArticle()
//            );
//        } catch (Exception e) {
//            BusinessException be = new BusinessException();
//            be.addCleErreur("ERR_MISE_A_JOUR_ENCHERE");
//            throw be;
//        }
//    }
//}