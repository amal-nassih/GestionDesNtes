package com.gsnotes.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Element;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.InscriptionMatiere;
import com.gsnotes.bo.InscriptionModule;

public interface IInscriptionModuleDao extends JpaRepository<InscriptionModule, Long> {
	
	public InscriptionModule getByModuleAndInscriptionAnnuelle(Module m ,InscriptionAnnuelle inscriptionannuelle);

	public InscriptionModule getByModuleAndInscriptionAnnuelle(com.gsnotes.bo.Module m, InscriptionAnnuelle i);

}
