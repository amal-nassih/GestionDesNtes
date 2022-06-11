package com.gsnotes.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



import com.gsnotes.bo.Module;
import com.gsnotes.bo.Niveau;


public interface IModuleDao extends JpaRepository<Module, Long> {
	
	public Module getByTitreAndNiveau(String titre,Niveau Niveau);
	public List<Module> findByTitre(String titre);
	
	public List<Module> getByNiveau(Niveau Niveau);
	
	public Module getByTitre(String Titre);

}
