package com.gsnotes.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Element;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.InscriptionMatiere;

public interface IInscriptionElemDao extends JpaRepository<InscriptionMatiere, Long> {

	public InscriptionMatiere getByMatiereAndInscriptionAnnuelle(Element m ,InscriptionAnnuelle inscriptionannuelle);
	
	public Double getNoteSRByIdInscriptionMatiere(Long Id);
	
	public Double getNoteSNByIdInscriptionMatiere(Long Id);
	
/*	@Transactional
	@Modifying
	@Query("update inscriptionmatiere c set c.noteSN = ?1 where c.IdInscriptionMatiere = ?2")
	public int updateInscriptionMatiere(Long id,Double note);*/


}
