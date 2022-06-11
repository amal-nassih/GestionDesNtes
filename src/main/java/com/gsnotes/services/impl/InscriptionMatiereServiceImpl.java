package com.gsnotes.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Element;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.InscriptionMatiere;
import com.gsnotes.bo.Module;
import com.gsnotes.bo.Niveau;
import com.gsnotes.bo.Role;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.dao.ICompteDao;
import com.gsnotes.dao.IInscriptionDao;
import com.gsnotes.dao.IInscriptionElemDao;
import com.gsnotes.dao.IModuleDao;
import com.gsnotes.dao.INiveauDao;
import com.gsnotes.dao.IRoleDao;
import com.gsnotes.dao.IUtilisateurDao;
import com.gsnotes.services.ICompteService;
import com.gsnotes.services.IInscriptionMatiereService;
import com.gsnotes.services.IInscriptionService;
import com.gsnotes.utils.export.ExcelExporter;

@Service
@Transactional
public class InscriptionMatiereServiceImpl implements IInscriptionMatiereService {
	
	@Autowired
	IInscriptionDao inscr;
	
	@Autowired
	INiveauDao niv;
	
	@Autowired
	IInscriptionElemDao inscrMat;
	
	@Autowired
	IModuleDao m;

	
	
	@Override
	public Map<Etudiant,Map<com.gsnotes.bo.Module,Map<Element,Double>>> getEtudiantScoreDict(List<Etudiant> etud, Long idNiv) {
		
		Map<Etudiant,Map<com.gsnotes.bo.Module,Map<Element,Double>>> e = new HashMap(); 
		
		for (Etudiant etudiant : etud) {
			
			InscriptionAnnuelle i = null;
		    i = inscr.getByEtudiantAndNiveau(etudiant, niv.getById(idNiv));
		    
		    if(i != null) {
		    	
		    	List<com.gsnotes.bo.Module> l = m.getByNiveau(niv.getById(idNiv));
		        Map<com.gsnotes.bo.Module,Map<Element,Double>> listElem = new HashMap();
		        
		        for (Module mod : l) {
		        	List<Element> elements = mod.getElements();
		        	
		        	for(Element eleme : elements){
		        		
		        		InscriptionMatiere inscrpElem = null;
		        		inscrpElem = inscrMat.getByMatiereAndInscriptionAnnuelle(eleme, i);
		        		
		        		if(inscrpElem != null) {
		        			
		        			Double note = inscrpElem.getNoteSN(); // TODO : get score of ratt
		        			
		        			Map<Element,Double> d  = new HashMap<>();
		        			
		        			d.put(eleme, note);
		        			
				        	listElem.put(mod, d); 
				        	
				        	e.put(etudiant, listElem);

		        		}
		        		
		        	}
				}
		    	
		    }
			
		}
		return e;
	}
	
	// TODO : check if the values dyal la note de rat or normale khawyin f db 3ndna comme entrer List etudiant and id matiere
	
	public Boolean checkNoteRatt(List<Etudiant> etud, Long idNiv,Long IdModule) {
		Boolean v = false;
		Map<Etudiant,Map<Element,Boolean>> mp = new HashMap<>();
		
		for(Etudiant et : etud) {
			InscriptionMatiere iMa = null;
			
			InscriptionAnnuelle a = null;
			a = inscr.getByEtudiantAndNiveau(et, niv.getById(idNiv));
			
			if(a != null) {
				Map<Element,Boolean> mpElem = new HashMap<>();

				for(Element elem : m.getById(IdModule).getElements()) {
					
					
					iMa = inscrMat.getByMatiereAndInscriptionAnnuelle(elem, a);
					
					if(iMa != null) {
						if(iMa.getNoteSR() != -1) {
							System.out.println("note ratt : " + iMa.getNoteSR());
							v=true;
							mpElem.put(elem,v);

							
						}else {
							System.out.println("note ratt : lool " + iMa.getNoteSR());
							v = false;
							mpElem.put(elem,v);
							break;
						}
				}
				
					
				}
				
				mp.put(et, mpElem);
				
				System.out.println(mp.get(et) +" => " + et );
				
			}
		}
		
		
		return v;
	}
	
	public Boolean checkNoteNorm(List<Etudiant> etud, Long idNiv,Long IdModule) { 
		
		Boolean v = false;
		
		Map<Etudiant,Map<Element,Boolean>> mp = new HashMap<>();
				
				for(Etudiant et : etud) {
					InscriptionMatiere iMa = null;
					
					InscriptionAnnuelle a = null;
					a = inscr.getByEtudiantAndNiveau(et, niv.getById(idNiv));
					
					if(a != null) {
						Map<Element,Boolean> mpElem = new HashMap<>();
		
						for(Element elem : m.getById(IdModule).getElements()) {
							
							
							iMa = inscrMat.getByMatiereAndInscriptionAnnuelle(elem, a);
							
							if(iMa != null) {
								if(iMa.getNoteSN() != -1) {
									System.out.println("note Normale : " + iMa.getNoteSN());
									v=true;
									mpElem.put(elem,v);
		
									
								}else {
									System.out.println("note Normal : vide " + iMa.getNoteSN());
									v = false;
									mpElem.put(elem,v);
									break;
								}
						}
						
							
						}
						
						mp.put(et, mpElem);
						
						System.out.println(mp.get(et) +" => " + et );
						
					}
				}
		
		return v;
	}

	


	//TODO : insert notes if it is a rattrapage note then make sure we have value
	// inserer f 2 table inscription matiere then module
	
	

	public Boolean InsertNoteNorm(List<InscriptionMatiere> inscMatiere) {
		for(InscriptionMatiere ima : inscMatiere) {
			if(ima.getNoteSR() == -1 || ima.getNoteSR() == 0) {
				ima.setNoteFinale(ima.getNoteSN());  // 100% normale
			}
			else {
				ima.setNoteFinale(ima.getNoteSN()*0.4 + ima.getNoteSR() * 0.6); //40% session normal 60% ratt
			}
			
			if(ima.getNoteFinale()>=12) {
				ima.setValidation("v");
			}else {
				ima.setValidation("NV");
			}
	
			ima.setPlusInfos("updated by excel file");
			inscrMat.save(ima);
		}
		return true;
	}

	/*@Override
	public Boolean UpdateNoteNorm(List<InscriptionMatiere> inscMatiere) {
		for(InscriptionMatiere ima : inscMatiere) {
			if(ima.getNoteSR() == -1) {
				ima.setNoteFinale(ima.getNoteSN());  // 100% normale
			}
			else {
				ima.setNoteFinale(ima.getNoteSN()*0.4 + ima.getNoteSR() * 0.6); //40% session normal 60% ratt
			}
			
			if(ima.getNoteFinale()>=12) {
				ima.setValidation("v");
			}else {
				ima.setValidation("NV");
			}
	
			ima.setPlusInfos("updated by excel file");
			inscrMat.save(ima);
		}
		return true;
	}*/

	@Override
	public Boolean contains(InscriptionAnnuelle ins, Element element) {
		if(inscrMat.getByMatiereAndInscriptionAnnuelle(element, ins) == null) {
			return false;
		}
		return true;
	}

	@Override
	public InscriptionMatiere getInscriptionMa(InscriptionAnnuelle ins, Element element) {
		return inscrMat.getByMatiereAndInscriptionAnnuelle(element, ins);
	}

	@Override
	public Boolean InsertNoteRat(List<InscriptionMatiere> inscMatiere) {
		Boolean b = false;
		
		for(InscriptionMatiere ima : inscMatiere) {
			if(ima.getNoteSN() == -1) {
				ima.setNoteFinale(ima.getNoteSR());  // 100% rattrapage
			}
			else {
				ima.setNoteFinale(ima.getNoteSN()*0.4 + ima.getNoteSR() * 0.6); //40% session normale 60% ratt
			}
			
			if(ima.getNoteFinale()>=12) {
				ima.setValidation("v");
			}else {
				ima.setValidation("NV");
			}
	
			ima.setPlusInfos("updated by excel file");
			
			inscrMat.save(ima);
			

		}
		

		System.out.println("Insertion done");
		
		b = true;
		return b;
	}

	@Override
	public Boolean UpdateNoteNorm(List<InscriptionMatiere> inscMatiere) {
		// TODO Auto-generated method stub
		return null;
	}

}
