package com.gsnotes.services;

import java.util.List;
import java.util.Map;

import com.gsnotes.bo.Element;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.InscriptionMatiere;
import com.gsnotes.bo.InscriptionModule;
import com.gsnotes.bo.Niveau;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.utils.export.ExcelExporter;

public interface IInscriptionModuleService {

	public Boolean InsertOrUpdateInscrip(List<InscriptionModule> inscModule);
	InscriptionModule getInscription(com.gsnotes.bo.Module m, InscriptionAnnuelle i);
	
}
