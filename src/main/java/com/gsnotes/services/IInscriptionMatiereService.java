package com.gsnotes.services;

import java.util.List;
import java.util.Map;

import com.gsnotes.bo.Element;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.InscriptionMatiere;
import com.gsnotes.bo.Niveau;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.utils.export.ExcelExporter;

public interface IInscriptionMatiereService {

	public Map<Etudiant,Map<com.gsnotes.bo.Module,Map<Element,Double>>> getEtudiantScoreDict(List<Etudiant> etud,Long idNiv);
	
	public Boolean checkNoteRatt(List<Etudiant> etud, Long niveau,Long IdModule);
	public Boolean checkNoteNorm(List<Etudiant> etud, Long idNiv,Long IdModule);
	
	public Boolean InsertNoteNorm(List<InscriptionMatiere> inscMatiere);

	public Boolean InsertNoteRat(List<InscriptionMatiere> inscMatiere);

	public Boolean UpdateNoteNorm(List<InscriptionMatiere> inscMatiere);
	
	public Boolean contains(InscriptionAnnuelle ins , Element element);
	
	public InscriptionMatiere getInscriptionMa(InscriptionAnnuelle ins , Element element);
}
