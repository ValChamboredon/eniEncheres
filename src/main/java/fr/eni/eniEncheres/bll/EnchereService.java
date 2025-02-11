package fr.eni.eniEncheres.bll;

import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.exception.BusinessException;
import java.util.List;

public interface EnchereService {
    void creerEnchere(Enchere enchere) throws BusinessException;
    List<Enchere> obtenirEncheresParArticle(int noArticle);
    Enchere obtenirEnchereLaPlusHaute(int noArticle);
}
