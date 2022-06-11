package com.gsnotes.services;

import java.util.List;

public interface IModuleService {

	
	
	public com.gsnotes.bo.Module getModule(String titre,Long idNiveau);
	
	public com.gsnotes.bo.Module getModuleById(Long id);
	
	public com.gsnotes.bo.Module ContainsModule(String titre,Long idNiveau);
	
	public List<com.gsnotes.bo.Module> getAllModules();
	
	public com.gsnotes.bo.Module getByTitle(String t);
	

}
