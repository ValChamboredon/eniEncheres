package fr.eni.eniEncheres.dal.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import fr.eni.eniEncheres.bo.Categorie;

public class CategorieRowMapper implements RowMapper<Categorie> {
	@Override
	public Categorie mapRow(ResultSet rs, int rowNum) throws SQLException {

		Categorie categorie = new Categorie();

		categorie.setNoCategorie(rs.getInt("no_categorie"));
		categorie.setLibelle(rs.getString("libelle"));

		return categorie;
	}

}