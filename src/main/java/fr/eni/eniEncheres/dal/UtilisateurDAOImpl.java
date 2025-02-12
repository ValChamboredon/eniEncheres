/* =====================
 * IMPLEMENTATION UTILISATEUR DAO
 * ===================== */
package fr.eni.eniEncheres.dal;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.dal.mapper.UtilisateurRowMapper;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO {
 private final JdbcTemplate jdbcTemplate;

 public UtilisateurDAOImpl(JdbcTemplate jdbcTemplate) {
     this.jdbcTemplate = jdbcTemplate;
 }
 
 @Override
 public void ajouterUtilisateur(Utilisateur utilisateur) {
     String sql = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
     jdbcTemplate.update(sql, utilisateur.getPseudo(), utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getEmail(), utilisateur.getTelephone(), utilisateur.getRue(), utilisateur.getCodePostal(), utilisateur.getVille(), utilisateur.getMotDePasse(), utilisateur.getCredit(), utilisateur.isAdministrateur());
 }
 
 @Override
 public Utilisateur getUtilisateurById(int noUtilisateur) {
     String sql = "SELECT * FROM UTILISATEURS WHERE no_utilisateur = ?";
     return jdbcTemplate.queryForObject(sql, new Object[]{noUtilisateur}, new UtilisateurRowMapper());
 }

 @Override
 public Utilisateur getUtilisateurByEmail(String email) {
     String sql = "SELECT * FROM UTILISATEURS WHERE email = ?";
     return jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) ->
         new Utilisateur(rs.getInt("no_utilisateur"), rs.getString("pseudo"), rs.getString("nom"), rs.getString("prenom"), rs.getString("email"), rs.getString("telephone"), rs.getString("rue"), rs.getString("code_postal"), rs.getString("ville"), rs.getString("mot_de_passe"), rs.getInt("credit"), rs.getBoolean("administrateur")));
 }
 
 @Override
 public List<Utilisateur> getAllUtilisateurs() {
     String sql = "SELECT * FROM UTILISATEURS";
     return jdbcTemplate.query(sql, (rs, rowNum) ->
         new Utilisateur(rs.getInt("no_utilisateur"), rs.getString("pseudo"), rs.getString("nom"), rs.getString("prenom"), rs.getString("email"), rs.getString("telephone"), rs.getString("rue"), rs.getString("code_postal"), rs.getString("ville"), rs.getString("mot_de_passe"), rs.getInt("credit"), rs.getBoolean("administrateur")));
 }
 
 @Override
 public void supprimerUtilisateur(int noUtilisateur) {
     String sql = "DELETE FROM UTILISATEURS WHERE no_utilisateur = ?";
     jdbcTemplate.update(sql, noUtilisateur);
 }
 
 @Override
 public void mettreAJourUtilisateur(Utilisateur utilisateur) {
     String sql = "UPDATE UTILISATEURS SET pseudo = ?, nom = ?, prenom = ?, email = ?, telephone = ?, rue = ?, code_postal = ?, ville = ?, mot_de_passe = ?, credit = ?, administrateur = ? WHERE no_utilisateur = ?";
     jdbcTemplate.update(sql, utilisateur.getPseudo(), utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getEmail(), utilisateur.getTelephone(), utilisateur.getRue(), utilisateur.getCodePostal(), utilisateur.getVille(), utilisateur.getMotDePasse(), utilisateur.getCredit(), utilisateur.isAdministrateur(), utilisateur.getNoUtilisateur());
 }

 @Override
 public Utilisateur getUtilisateurByPseudo(String pseudo) { // ✅ Implémentation
     String sql = "SELECT * FROM UTILISATEURS WHERE pseudo = ?";
     return jdbcTemplate.queryForObject(sql, new Object[]{pseudo}, new UtilisateurRowMapper());
 }
 @Override
 public void supprimerByEmail(String email) { // ✅ Implémentation
     String sql = "DELETE FROM UTILISATEURS WHERE email = ?";
     jdbcTemplate.update(sql, email);
 }
}
