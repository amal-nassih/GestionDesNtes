package com.gsnotes.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Element;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.InscriptionMatiere;
import com.gsnotes.bo.InscriptionModule;
import com.gsnotes.bo.Module;
import com.gsnotes.bo.Niveau;
import com.gsnotes.bo.Role;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.dao.ICompteDao;
import com.gsnotes.dao.IInscriptionDao;
import com.gsnotes.dao.IInscriptionElemDao;
import com.gsnotes.dao.IInscriptionModuleDao;
import com.gsnotes.dao.IModuleDao;
import com.gsnotes.dao.INiveauDao;
import com.gsnotes.dao.IRoleDao;
import com.gsnotes.dao.IUtilisateurDao;
import com.gsnotes.services.ICompteService;
import com.gsnotes.services.IInscriptionMatiereService;
import com.gsnotes.services.IInscriptionModuleService;
import com.gsnotes.services.IInscriptionService;
import com.gsnotes.utils.export.ExcelExporter;

@Service
@Transactional
public class InscriptionModuleServiceImpl implements IInscriptionModuleService {
	
	@Autowired
	IInscriptionDao inscr;
	
	@Autowired
	INiveauDao niv;
	
	@Autowired
	IInscriptionElemDao inscrMat;
	
	@Autowired
	IInscriptionModuleDao inscrMod;
	
	@Autowired
	IModuleDao m;

	@Override
	public Boolean InsertOrUpdateInscrip(List<InscriptionModule> inscModule) {
		
		for(InscriptionModule ima : inscModule) {   //make inscriptions module dans une table
			if(ima.getNoteSR() == -1) {
				ima.setNoteFinale(ima.getNoteSN());  // 100% normale
			}else if(ima.getNoteSN() == -1) {
				ima.setNoteFinale(ima.getNoteSR());
			}
			else {
				ima.setNoteFinale(ima.getNoteSN()*0.4 + ima.getNoteSR() * 0.6); //40% session normal 60% ratt
			}
			
			if(ima.getNoteFinale()>=12) {
				ima.setValidation("v");
			}else {
				ima.setValidation("NV");
			}
	
			ima.setPlusInfos("updated by excel file");
			
			if(inscrMod.getByModuleAndInscriptionAnnuelle(ima.getModule(), ima.getInscriptionAnnuelle()) == null) {
				inscrMod.save(ima);
				System.out.println("no");
			}else {
				/*InscriptionModule mo = inscrMod.getByModuleAndInscriptionAnnuelle(ima.getModule(), ima.getInscriptionAnnuelle());
				
				mo.setNoteFinale(ima.getNoteFinale());
				mo.setNoteSN(ima.getNoteSN());
				mo.setNoteSR(ima.getNoteSR());
			
			    inscrMod.save(ima);*/
			}
			
		}
		
		return true;
	}

	@Override
	public InscriptionModule getInscription(Module m, InscriptionAnnuelle i) {
		return inscrMod.getByModuleAndInscriptionAnnuelle(m, i);
	}

	
	
	
}
