package fr.eni.eniEncheres.dal;

import java.util.List;
import fr.eni.eniEncheres.bo.Enchere;
import fr.eni.eniEncheres.exception.BusinessException;

public interface EnchereDAO {
    void ajouterEnchere(Enchere enchere) throws BusinessException;
    List<Enchere> getEncheresParArticle(int noArticle) throws BusinessException;
    Enchere getEnchereMaxParArticle(int noArticle) throws BusinessException;
    void supprimerEncheresParArticle(int noArticle) throws BusinessException;
    void mettreAJourEnchere(Enchere enchere) throws BusinessException;
	List<Enchere> getEncheresByUtilisateur(int noUtilisateur);
}