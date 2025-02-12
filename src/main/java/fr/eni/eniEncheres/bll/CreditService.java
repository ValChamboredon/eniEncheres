package fr.eni.eniEncheres.bll;

import fr.eni.eniEncheres.exception.BusinessException;

public interface CreditService {
	
	void verifierCredit(int noUtilisateur, int montant) throws BusinessException;
	
	void transfererPoints(int noAcheteur, int noVendeur, int montant) throws BusinessException;
	 
	int getSoldeUtilisateur(int noUtilisateur) throws BusinessException; 

}
