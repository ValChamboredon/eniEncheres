package fr.eni.eniEncheres.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.spi.DirectoryManager;

import org.springframework.stereotype.Repository;

import com.zaxxer.hikari.hibernate.HikariConnectionProvider;

import fr.eni.eniEncheres.bo.Utilisateur;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO {

	private static final String INSERT = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	private static final String CHECK_PSEUDO = "SELECT COUNT(*) FROM UTILISATEURS WHERE pseudo = ?";
	private static final String CHECK_EMAIL = "SELECT COUNT(*) FROM UTILISATEURS WHERE email = ?";

	
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
}
