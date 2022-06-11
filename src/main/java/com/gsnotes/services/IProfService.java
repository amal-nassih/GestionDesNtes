package com.gsnotes.services;

import java.util.List;

import com.gsnotes.bo.Enseignant;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.utils.export.ExcelExporter;

public interface IProfService {

	public Boolean verifyProf(String nom,String prenom);

	public List<Enseignant> getAllEnseignants();
	
	

}
