package com.gsnotes.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsnotes.bo.Enseignant;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.dao.IProfDao;
import com.gsnotes.dao.IUtilisateurDao;
import com.gsnotes.services.IPersonService;
import com.gsnotes.services.IProfService;
import com.gsnotes.utils.export.ExcelExporter;

@Service
@Transactional
public class ProfServiceImpl implements IProfService {

	@Autowired
	private IProfDao personDao;

	@Autowired
	private IPersonService person;
	
	@Override
	public Boolean verifyProf(String nom, String prenom) {
		
		if(personDao.findById(person.getUserByNom(nom, prenom))==null) {
			System.out.println(person.getUserByNom(nom, prenom));
			return false;
		}else {
			System.out.println(person.getUserByNom(nom, prenom));
			return true;
		}
		
	}

	@Override
	public List<Enseignant> getAllEnseignants() {
		return personDao.findAll();
	}
	


}
