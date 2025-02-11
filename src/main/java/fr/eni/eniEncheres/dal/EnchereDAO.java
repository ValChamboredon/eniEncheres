package fr.eni.eniEncheres.dal;

import fr.eni.eniEncheres.bo.Enchere;
import java.util.List;

public interface EnchereDAO {
	
    void save(Enchere enchere);
    List<Enchere> findByArticleId(int noArticle);
    Enchere findHighestBidByArticleId(int noArticle);
}
