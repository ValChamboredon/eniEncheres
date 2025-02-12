package fr.eni.eniEncheres.bll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eni.eniEncheres.bo.Utilisateur;
import fr.eni.eniEncheres.dal.UtilisateurDAO;
import fr.eni.eniEncheres.exception.BusinessException;

@Service
public class CreditServiceImpl implements CreditService {

	@Autowired
	private UtilisateurDAO utilisateurDAO;
	
	@Override
    public void verifierCredit(int noUtilisateur, int montant) throws BusinessException {
        Utilisateur utilisateur = utilisateurDAO.read(noUtilisateur);
        
        BusinessException be = new BusinessException();
        if (utilisateur == null) {
            be.addCleErreur("ERR_UTILISATEUR_INEXISTANT");
            throw be;
        }
        
        if (utilisateur.getCredit() < montant) {
            be.addCleErreur("ERR_CREDIT_INSUFFISANT");
            throw be;
        }
    }
	
	@Override
    public void transfererPoints(int noAcheteur, int noVendeur, int montant) throws BusinessException {
        verifierCredit(noAcheteur, montant);
        
        Utilisateur acheteur = utilisateurDAO.read(noAcheteur);
        Utilisateur vendeur = utilisateurDAO.read(noVendeur);
        
        BusinessException be = new BusinessException();
        if (vendeur == null) {
            be.addCleErreur("ERR_VENDEUR_INEXISTANT");
            throw be;
        }
        
        acheteur.setCredit(acheteur.getCredit() - montant);
        vendeur.setCredit(vendeur.getCredit() + montant);
        
        utilisateurDAO.update(acheteur);
        utilisateurDAO.update(vendeur);
    }
	
	@Override
    public int getSoldeUtilisateur(int noUtilisateur) throws BusinessException {
        Utilisateur utilisateur = utilisateurDAO.read(noUtilisateur);
        
        BusinessException be = new BusinessException();
        if (utilisateur == null) {
            be.addCleErreur("ERR_UTILISATEUR_INEXISTANT");
            throw be;
        }
        
        return utilisateur.getCredit();
    }
}
