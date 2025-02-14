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

/**
 * ✅ Implémentation de l'interface `UtilisateurDAO` pour la gestion des utilisateurs.
 * 
 * Cette classe gère l'interaction avec la base de données pour effectuer les opérations CRUD
 * sur les utilisateurs (ajout, modification, suppression et récupération).
 */
@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * ✅ Constructeur de la classe.
     *
     * @param namedParameterJdbcTemplate Template pour l'exécution de requêtes SQL paramétrées.
     */
    public UtilisateurDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    // 🔹 Requête SQL pour insérer un utilisateur dans la base de données.
    private static final String INSERT = 
        "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) " +
        "VALUES (:pseudo, :nom, :prenom, :email, :telephone, :rue, :codePostal, :ville, :motDePasse, :credit, :administrateur)";

    /**
     * ✅ Sauvegarde un nouvel utilisateur en base de données.
     *
     * @param utilisateur L'utilisateur à enregistrer.
     */
    @Override
    public void save(Utilisateur utilisateur) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // 🔹 Paramètres de la requête SQL
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", utilisateur.getPseudo());
        namedParameters.addValue("nom", utilisateur.getNom());
        namedParameters.addValue("prenom", utilisateur.getPrenom());
        namedParameters.addValue("email", utilisateur.getEmail());
        namedParameters.addValue("telephone", utilisateur.getTelephone());
        namedParameters.addValue("rue", utilisateur.getRue());
        namedParameters.addValue("codePostal", utilisateur.getCodePostal());
        namedParameters.addValue("ville", utilisateur.getVille());
        namedParameters.addValue("motDePasse", utilisateur.getMotDePasse());
        namedParameters.addValue("credit", utilisateur.getCredit());
        namedParameters.addValue("administrateur", utilisateur.isAdministrateur());

        // 🔹 Exécution de la requête SQL
        namedParameterJdbcTemplate.update(INSERT, namedParameters, keyHolder);

        // 🔹 Récupération de l'ID généré si l'insertion a réussi
        if (keyHolder.getKey() != null) {
            utilisateur.setNoUtilisateur(keyHolder.getKey().intValue());
        }
    }

    /**
     * ✅ Récupère un utilisateur par son email.
     *
     * @param email L'email de l'utilisateur.
     * @return L'utilisateur correspondant ou `null` s'il n'existe pas.
     */
    @Override
    public Utilisateur getUtilisateur(String email) {
        MapSqlParameterSource namedParameterSource = new MapSqlParameterSource();
        namedParameterSource.addValue("email", email);
        
        return namedParameterJdbcTemplate.queryForObject(
                "SELECT * FROM UTILISATEURS WHERE email = :email",
                namedParameterSource, 
                new UtilisateurRowMapper()
        );
    }

    /**
     * ✅ Met à jour les informations d'un utilisateur existant.
     *
     * @param utilisateur L'utilisateur avec les nouvelles informations.
     */
    @Override
    public void update(Utilisateur utilisateur) {
        String UPDATE_SQL = 
            "UPDATE UTILISATEURS SET pseudo = :pseudo, nom = :nom, prenom = :prenom, email = :email, " +
            "telephone = :telephone, rue = :rue, code_postal = :codePostal, ville = :ville, " +
            "mot_de_passe = :motDePasse, credit = :credit, administrateur = :administrateur " +
            "WHERE no_utilisateur = :noUtilisateur";

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", utilisateur.getPseudo());
        namedParameters.addValue("nom", utilisateur.getNom());
        namedParameters.addValue("prenom", utilisateur.getPrenom());
        namedParameters.addValue("email", utilisateur.getEmail());
        namedParameters.addValue("telephone", utilisateur.getTelephone());
        namedParameters.addValue("rue", utilisateur.getRue());
        namedParameters.addValue("codePostal", utilisateur.getCodePostal());
        namedParameters.addValue("ville", utilisateur.getVille());
        namedParameters.addValue("motDePasse", utilisateur.getMotDePasse());
        namedParameters.addValue("credit", utilisateur.getCredit());
        namedParameters.addValue("administrateur", utilisateur.isAdministrateur());
        namedParameters.addValue("noUtilisateur", utilisateur.getNoUtilisateur());

        namedParameterJdbcTemplate.update(UPDATE_SQL, namedParameters);
    }

    /**
     * ✅ Vérifie si un pseudo est déjà utilisé.
     *
     * @param pseudo Le pseudo à vérifier.
     * @return `true` si le pseudo existe, sinon `false`.
     */
    @Override
    public boolean existsByPseudo(String pseudo) {
        String QUERY = "SELECT COUNT(*) FROM UTILISATEURS WHERE pseudo = :pseudo";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("pseudo", pseudo);
        Integer count = namedParameterJdbcTemplate.queryForObject(QUERY, params, Integer.class);
        return count != null && count > 0;
    }

    /**
     * ✅ Vérifie si un email est déjà utilisé.
     *
     * @param email L'email à vérifier.
     * @return `true` si l'email existe, sinon `false`.
     */
    @Override
    public boolean existsByEmail(String email) {
        String QUERY = "SELECT COUNT(*) FROM UTILISATEURS WHERE email = :email";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", email);
        Integer count = namedParameterJdbcTemplate.queryForObject(QUERY, params, Integer.class);
        return count != null && count > 0;
    }

    /**
     * ✅ Récupère un utilisateur par son ID.
     *
     * @param noUtilisateur L'identifiant de l'utilisateur.
     * @return L'utilisateur correspondant ou `null` s'il n'existe pas.
     */
    @Override
    public Utilisateur read(int noUtilisateur) {
        String QUERY = "SELECT * FROM UTILISATEURS WHERE no_utilisateur = :noUtilisateur";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("noUtilisateur", noUtilisateur);
        return namedParameterJdbcTemplate.queryForObject(QUERY, params, new UtilisateurRowMapper());
    }

    /**
     * ✅ Supprime un utilisateur en utilisant son email.
     *
     * @param email L'email de l'utilisateur à supprimer.
     */
    @Override
    public void supprimerByEmail(String email) {
        String DELETE_SQL = "DELETE FROM UTILISATEURS WHERE email = :email";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", email);
        namedParameterJdbcTemplate.update(DELETE_SQL, params);
    }
}

/**
 * ✅ Mapper pour la transformation des résultats SQL en objets `Utilisateur`.
 */
class UtilisateurRowMapper implements RowMapper<Utilisateur> {
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
