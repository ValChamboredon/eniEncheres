package fr.eni.eniEncheres.dal;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import fr.eni.eniEncheres.bo.Utilisateur;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public UtilisateurDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}


	// Request SQL
	private static final String INSERT = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) "
			+ "VALUES (:pseudo, :nom, :prenom, :email, :telephone, :rue, :codePostal, :ville, :motDePasse, :credit, :administrateur)";

	
	private static final String UPDATE = "UPDATE UTILISATEURS SET pseudo = :pseudo, nom = :nom, prenom = :prenom, email = :email, telephone = :telephone, rue = :rue, code_postal = :codePostal, ville = :ville, mot_de_passe = :motDePasse, credit = :credit, administrateur = :administrateur WHERE email = :email";
	private final String FIND_BY_ID = "SELECT * FROM UTILISATEURS WHERE no_utilisateur =:id";
	private final String FIND_BY_EMAIL = "SELECT * FROM UTILISATEURS WHERE email = :email";
	private static final String DELETE = "DELETE FROM UTILISATEURS WHERE email = :email";
	private final String COUNT_IDENTICAL_EMAILS = "SELECT COUNT (EMAIL) FROM UTILISATEURS WHERE  EMAIL = :email";
	private final String COUNT_IDENTICAL_PSEUDOS = "SELECT COUNT (PSEUDO) FROM UTILISATEURS WHERE PSEUDO = :pseudo";
	
	
	@Override
	public void update(Utilisateur utilisateur) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", utilisateur.getPseudo());
		namedParameters.addValue("nom", utilisateur.getNom());
		namedParameters.addValue("prenom", utilisateur.getPrenom());
		namedParameters.addValue("email", utilisateur.getEmail());
		namedParameters.addValue("telephone", utilisateur.getTelephone());
		namedParameters.addValue("rue", utilisateur.getRue());
		namedParameters.addValue("codePostal", utilisateur.getCodePostal());
		namedParameters.addValue("ville", utilisateur.getVille());
		namedParameters.addValue("motDePasse", utilisateur.getMotDePasse()); // à haché
		namedParameters.addValue("credit", utilisateur.getCredit());
		namedParameters.addValue("administrateur", utilisateur.isAdministrateur());
 
		namedParameterJdbcTemplate.update(UPDATE, namedParameters);
 
	}

	@Override
	public void save(Utilisateur utilisateur) {
		//récupère la clé auto incrémentée
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("pseudo", utilisateur.getPseudo());
		namedParameters.addValue("nom", utilisateur.getNom());
		namedParameters.addValue("prenom", utilisateur.getPrenom());
		namedParameters.addValue("email", utilisateur.getEmail());
		namedParameters.addValue("telephone", utilisateur.getTelephone());
		namedParameters.addValue("rue", utilisateur.getRue());
		namedParameters.addValue("codePostal", utilisateur.getCodePostal());
		namedParameters.addValue("ville", utilisateur.getVille());
		namedParameters.addValue("motDePasse", utilisateur.getMotDePasse()); // à haché
		namedParameters.addValue("credit", utilisateur.getCredit());
		namedParameters.addValue("administrateur", utilisateur.isAdministrateur());

		namedParameterJdbcTemplate.update(INSERT, namedParameters,keyHolder);
		
		if (keyHolder.getKey() != null) {
	        utilisateur.setNoUtilisateur(keyHolder.getKey().intValue());
	    }

	}

	@Override
	public boolean existsByEmail(String email) {
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("email", email);
	    int nbOccurrencesEmail = namedParameterJdbcTemplate.queryForObject(
	        COUNT_IDENTICAL_EMAILS, namedParameters, Integer.class
	    );

	    System.out.println("Vérification email: " + email + " → Nombre d'occurrences: " + nbOccurrencesEmail);
	    return nbOccurrencesEmail > 0;
	}

	@Override
	public boolean existsByPseudo(String pseudo) {
	    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue("pseudo", pseudo);
	    int nbOccurrencesPseudo = namedParameterJdbcTemplate.queryForObject(
	        COUNT_IDENTICAL_PSEUDOS, namedParameters, Integer.class
	    );

	    System.out.println("Vérification pseudo: " + pseudo + " → Nombre d'occurrences: " + nbOccurrencesPseudo);
	    return nbOccurrencesPseudo > 0;
	}

	


	@Override
	public Utilisateur read(int noUtilisateur) {
		MapSqlParameterSource namedParameterSource = new MapSqlParameterSource();
		namedParameterSource.addValue("id", noUtilisateur);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, namedParameterSource, new UtilisateurRowMapper());
	}

	@Override
	public Utilisateur getUtilisateur(String email) {
		MapSqlParameterSource namedParameterSource = new MapSqlParameterSource();
		namedParameterSource.addValue("email", email);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_EMAIL, namedParameterSource, new UtilisateurRowMapper());
	}

	@Override
	public void supprimerByEmail(String email) {
		MapSqlParameterSource namedParameterSource = new MapSqlParameterSource();
		namedParameterSource.addValue("email", email);
		namedParameterJdbcTemplate.update(DELETE, namedParameterSource);
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
