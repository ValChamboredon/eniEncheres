package fr.eni.eniEncheres.bll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
    public void transfererPoints(int noAcheteur, int noVendeur, int montant) throws BusinessException {
        verifierCredit(noAcheteur, montant);
        
        Utilisateur acheteur = utilisateurDAO.read(noAcheteur);
        Utilisateur vendeur = utilisateurDAO.read(noVendeur);
        
        BusinessException be = new BusinessException();
        if (vendeur == null || acheteur == null) {
            be.addCleErreur("ERR_UTILISATEUR_INEXISTANT");
            throw be;
        }
        
        if (acheteur.getCredit() < montant) {
            be.addCleErreur("ERR_CREDIT_INSUFFISANT");
            throw be;
        }
        
        try {
        acheteur.setCredit(acheteur.getCredit() - montant);
        utilisateurDAO.update(acheteur);
        
        vendeur.setCredit(vendeur.getCredit() + montant);
        utilisateurDAO.update(vendeur);
        
        System.out.println("Transfert effectué : " + montant + " points de " + acheteur.getPseudo() + " vers " + vendeur.getPseudo());
        System.out.println("Nouveau solde " + acheteur.getPseudo() + " : " + vendeur.getCredit());
        System.out.println("Nouveau solde " + acheteur.getPseudo() + " : " + vendeur.getCredit());
        
        } catch (Exception e) {
            be.addCleErreur("ERR_TRANSFERT");
            throw be;
        }
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