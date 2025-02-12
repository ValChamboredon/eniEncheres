package fr.eni.eniEncheres.dal.mapper;

import fr.eni.eniEncheres.bo.Utilisateur;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurRowMapper implements RowMapper<Utilisateur> {
    @Override
    public Utilisateur mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Utilisateur(
            rs.getInt("no_utilisateur"),
            rs.getString("pseudo"),
            rs.getString("nom"),
            rs.getString("prenom"),
            rs.getString("email"),
            rs.getString("telephone"),
            rs.getString("rue"),
            rs.getString("code_postal"),
            rs.getString("ville"),
            rs.getString("mot_de_passe"),
            rs.getInt("credit"),
            rs.getBoolean("administrateur")
        );
    }
}
