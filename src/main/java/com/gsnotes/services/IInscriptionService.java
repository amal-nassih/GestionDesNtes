package com.gsnotes.services;

import java.util.List;

import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.Niveau;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.utils.export.ExcelExporter;

public interface IInscriptionService {

	public List<Etudiant> getListEtudiants(Long id);
	
	public InscriptionAnnuelle getInscription(Etudiant etudiant,Long id);
	
	public Etudiant getEtudiant(Long idInscri);

}
