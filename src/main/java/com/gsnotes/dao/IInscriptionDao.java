package com.gsnotes.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.Niveau;

public interface IInscriptionDao extends JpaRepository<InscriptionAnnuelle, Long> {
	
	public List<InscriptionAnnuelle> getByNiveau(Niveau niveau);
	public InscriptionAnnuelle getByEtudiantAndNiveau(Etudiant etudiant,Niveau niveau);
	
	public Etudiant getEtudiantByIdInscription(Long Id);

}
