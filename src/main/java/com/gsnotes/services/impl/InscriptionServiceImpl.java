package com.gsnotes.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.Niveau;
import com.gsnotes.bo.Role;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.dao.ICompteDao;
import com.gsnotes.dao.IInscriptionDao;
import com.gsnotes.dao.INiveauDao;
import com.gsnotes.dao.IRoleDao;
import com.gsnotes.dao.IUtilisateurDao;
import com.gsnotes.services.ICompteService;
import com.gsnotes.services.IInscriptionService;
import com.gsnotes.utils.export.ExcelExporter;

@Service
@Transactional
public class InscriptionServiceImpl implements IInscriptionService {
	
	@Autowired
	IInscriptionDao inscr;
	
	@Autowired
	INiveauDao niv;

	@Override
	public List<Etudiant> getListEtudiants(Long id) {
		List<Etudiant> list = new ArrayList<>();
		for (InscriptionAnnuelle i : inscr.getByNiveau(niv.getById(id))) {
			list.add(i.getEtudiant());
		}
		return list;
	}

	@Override
	public InscriptionAnnuelle getInscription(Etudiant etudiant, Long id) {
		return inscr.getByEtudiantAndNiveau(etudiant, niv.getById(id));
	}

	@Override
	public Etudiant getEtudiant(Long idInscri) {
		return inscr.getEtudiantByIdInscription(idInscri);
	}

	

}
