package fr.eni.eniEncheres.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.spi.DirectoryManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.zaxxer.hikari.hibernate.HikariConnectionProvider;

import fr.eni.eniEncheres.bo.Utilisateur;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO {

			private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

			public UtilisateurDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
				this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
			}
			
		

		
		//Request SQL
		private static final String INSERT = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		private static final String CHECK_PSEUDO = "SELECT COUNT(*) FROM UTILISATEURS WHERE pseudo = ?";
		private static final String CHECK_EMAIL = "SELECT COUNT(*) FROM UTILISATEURS WHERE email = ?";
		
		
		private final String GET = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe FROM UTILISATEURS WHERE email = ?";
		private final String FIND_BY_ID="SELECT * FROM UTILISATEURS WHERE no_utilisateur =:id";
		
		
		
		@Override
		 public void save(Utilisateur utilisateur) {
	        try (Connection conn = ConnectionProvider.getConnection()) {
	            PreparedStatement stmt = conn.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
	            
	            stmt.setString(1, utilisateur.getPseudo());
	            stmt.setString(2, utilisateur.getNom());
	            stmt.setString(3, utilisateur.getPrenom());
	            stmt.setString(4, utilisateur.getEmail());
	            stmt.setString(5, utilisateur.getTelephone());
	            stmt.setString(6, utilisateur.getRue());
	            stmt.setString(7, utilisateur.getCodePostal());
	            stmt.setString(8, utilisateur.getVille());
	            stmt.setString(9, utilisateur.getMotDePasse());
	            stmt.setInt(10, utilisateur.getCredit());
	            stmt.setBoolean(11, utilisateur.isAdministrateur());
	            
	            stmt.executeUpdate();
	            
	            // Récupération de l'ID généré
	            ResultSet rs = stmt.getGeneratedKeys();
	            if(rs.next()) {
	                utilisateur.setNoUtilisateur(rs.getInt(1));
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de l'enregistrement de l'utilisateur", e);
	        }
	    }
		
		@Override
		public boolean existsByEmail(String email) {
			try (Connection conn = ConnectionProvider.getConnection()) {
	            PreparedStatement stmt = conn.prepareStatement(CHECK_EMAIL);
	            stmt.setString(1, email);
	            
	            ResultSet rs = stmt.executeQuery();
	            if(rs.next()) {
	                return rs.getInt(1) > 0;
	            }
	            return false;
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la vérification de l'email", e);
	        }
		}
		
		@Override
		public boolean existsByPseudo(String pseudo) {
			try (Connection conn = ConnectionProvider.getConnection()) {
	            PreparedStatement stmt = conn.prepareStatement(CHECK_PSEUDO);
	            stmt.setString(1, pseudo);
	            
	            ResultSet rs = stmt.executeQuery();
	            if(rs.next()) {
	                return rs.getInt(1) > 0;
	            }
	            return false;
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la vérification du pseudo", e);
	        }
		}

		@Override
		public Utilisateur read(int noUtilisateur) {
			MapSqlParameterSource namedParameterSource = new MapSqlParameterSource();
			namedParameterSource.addValue("id", noUtilisateur);
			return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, namedParameterSource,new UtilisateurRowMapper());
		}


		@Override
		public Utilisateur getUtilisateur(String email) {
			//TODO
			return null;
		}



		@Override
		public void supprimerByEmail(String email) {
			// TODO Auto-generated method stub
			
		}
	
		class UtilisateurRowMapper implements RowMapper<Utilisateur> {
			@Override
			public Utilisateur mapRow(ResultSet rs, int rowNum) throws SQLException {
				var utilisateur = new Utilisateur();
				utilisateur.setNoUtilisateur(rs.getInt("no_utilisateur"));
				utilisateur.setPseudo(rs.getString("pseudo"));
				utilisateur.setNom(rs.getString("nom"));
				utilisateur.setPrenom(rs.getString("prenom"));
				utilisateur.setEmail(rs.getString("email"));
				utilisateur.setTelephone(rs.getString("telephone"));
				utilisateur.setRue(rs.getString("rue"));
				utilisateur.setCodePostal(rs.getString("code_postal"));
				utilisateur.setVille(rs.getString("ville"));
				utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
				utilisateur.setCredit(rs.getInt("credit"));
				utilisateur.setAdministrateur(rs.getBoolean("administrateur"));
				return utilisateur;
			}
		}


}
