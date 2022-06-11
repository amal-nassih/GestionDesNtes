package com.gsnotes.services.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.passay.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Module;
import com.gsnotes.bo.Role;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.dao.ICompteDao;
import com.gsnotes.dao.IModuleDao;
import com.gsnotes.dao.INiveauDao;
import com.gsnotes.dao.IRoleDao;
import com.gsnotes.dao.IUtilisateurDao;
import com.gsnotes.services.ICompteService;
import com.gsnotes.services.IModuleService;
import com.gsnotes.utils.export.ExcelExporter;

@Service
@Transactional
public class IModuleServiceImpl implements IModuleService {
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private IModuleDao mod;


	@Autowired
	private INiveauDao niv;


	@Override
	public Module getModule(String titre,Long idNiveau) {
		
		return mod.getByTitreAndNiveau(titre, niv.getById(idNiveau));
	}


	@Override
	public Module getModuleById(Long id) {
		return mod.getById(id);
	}


	@Override
	public Module ContainsModule(String titre,Long idNiveau) {
		if(getModule(titre, idNiveau)==null) {
			LOGGER.error("the module that entered doesn't exist in database");
			return null;
		}
			return getModule(titre, idNiveau);
		
	}


	@Override
	public List<Module> getAllModules() {
		return mod.findAll();
	}


	@Override
	public  Module getByTitle(String t) {
		return mod.getByTitre(t);
	}
	
	
}
