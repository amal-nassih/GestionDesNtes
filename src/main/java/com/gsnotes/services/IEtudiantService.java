package com.gsnotes.services;

import java.util.List;

import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.utils.export.ExcelExporter;

public interface IEtudiantService {

	public Etudiant getEtudiantbyCNE(String CNE); 
	
	

}
