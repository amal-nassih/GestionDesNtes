package com.gsnotes.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.dao.IEtudiantDao;
import com.gsnotes.dao.IUtilisateurDao;
import com.gsnotes.services.IEtudiantService;
import com.gsnotes.services.IPersonService;
import com.gsnotes.utils.export.ExcelExporter;

@Service
@Transactional
public class EtudiantServiceImp implements IEtudiantService {

	@Autowired
	private IEtudiantDao et;

	@Override
	public Etudiant getEtudiantbyCNE(String CNE) {
		return et.getByCne(CNE);
	}


}
