package com.gsnotes.web.controllers;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.jni.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.gsnotes.bo.Element;
import com.gsnotes.bo.Enseignant;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.InscriptionMatiere;
import com.gsnotes.bo.InscriptionModule;
import com.gsnotes.bo.Module;
import com.gsnotes.bo.ModuleChoose;
import com.gsnotes.services.IEtudiantService;
import com.gsnotes.services.IInscriptionMatiereService;
import com.gsnotes.services.IInscriptionModuleService;
import com.gsnotes.services.IInscriptionService;
import com.gsnotes.services.IModuleService;
import com.gsnotes.services.IProfService;
import com.gsnotes.utils.export.ExcelExporterForScores;
import com.gsnotes.utils.export.ExcelImporterForScores;
import com.gsnotes.web.models.UserAndAccountInfos;

@Controller
@RequestMapping("/prof")
public class UtilisateurProfController {
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private IModuleService imod;
	
	@Autowired
	private IInscriptionService insc;
	
	@Autowired 
    private IProfService prof;
	
	@Autowired
	private IInscriptionMatiereService inscrMat;
	
	@Autowired
	private IInscriptionModuleService inscrMod;
	
	@Autowired
	private IEtudiantService eServ ;
	
	@RequestMapping("/showNotes/{module}")
	public String showNotes(@PathVariable(value = "module") String module,Model model) {
		model.addAttribute("module", module);
		
		return "/prof/showNotes";
	}
	
	
	@RequestMapping("/NotesImport/")
	public String importNotes(Model model) {
		
		if(model.containsAttribute("module")) {
			System.out.println("/prof/ScoresImport");
			return "/prof/ScoresImport";
		}
		
		
		model.addAttribute("module",new ModuleChoose());  //khas darori ytinitialisa hna bax ymkn nst3mlo moduleattribute
		
		return "/prof/NotesImport";
	}
	
	
	@RequestMapping("/ScoresImport/")
	public String importScores(Model model) {
			return "/prof/ScoresImport";
		
	}
	
	
	@RequestMapping(value="/importExcel")  
	public String upload(@RequestPart("file") MultipartFile file,HttpServletRequest request) throws IllegalStateException, IOException{  
	        
		    
	        System.out.println(file.getOriginalFilename());
	       
	        UserAndAccountInfos userInfo = (UserAndAccountInfos) httpSession.getAttribute("userInfo");

	        //save the file in upload directory
	        if(Files.notExists(Paths.get("C:\\Users\\amalo\\Downloads\\upload\\"))) {
		        Files.createDirectory(Paths.get("C:\\Users\\amalo\\Downloads\\upload\\"));
	        }
            if(Files.notExists(Paths.get("C:\\Users\\amalo\\Downloads\\upload\\").resolve(file.getOriginalFilename()))){
    	        Files.copy(file.getInputStream(), Paths.get("C:\\Users\\amalo\\Downloads\\upload\\").resolve(file.getOriginalFilename()));
            }
            
            
            List<Enseignant> profs = prof.getAllEnseignants();
            List<com.gsnotes.bo.Module> m = imod.getAllModules();
	        ExcelImporterForScores imp = new ExcelImporterForScores(file,userInfo,profs,m);    
	       
	        List<List<String>> output = imp.getFile();
	        
	        List<Etudiant> listE = new ArrayList<>();
	        for(String out :  output.get(1)) {
	        	listE.add(eServ.getEtudiantbyCNE(out));
	        }
	        
	        System.out.println(listE);
	        
	        if(httpSession.getAttribute("session")!=null) {
	        	httpSession.removeAttribute("session");
	        }
    	    

	        
	        Module module = imod.getByTitle(output.get(0).get(1));
	        
	        if("SN".equals(output.get(0).get(0))) {
	        	
	        	Boolean v = inscrMat.checkNoteNorm(listE, module.getNiveau().getIdNiveau(), module.getIdModule());
	        
	        	System.out.println(v);
	        	
	        	List<InscriptionMatiere> insM = new ArrayList<>(); 
        		List<InscriptionModule> insMd = new ArrayList<>(); 

        	    List<Float> scores = imp.getScores();
        	    
        	    Map<Element, Float> NOTES = new HashMap<>();
        	    
        	    System.out.println(scores);
        	    
        	    int j = 0;
        	    for(Etudiant et:listE) {
        	    	
        	    	 InscriptionAnnuelle instAnn = insc.getInscription(et, module.getNiveau().getIdNiveau());
        	    		        	    	 
        	    	 for(Element e : module.getElements()) {
 	        	    	NOTES.put(e, scores.get(j));
 	        	    	InscriptionMatiere mat = null;
 	        	    	InscriptionModule modi = null;
 	        	    	
 	        	    	if(inscrMat.contains(instAnn, e)) {
 	        	    		mat = inscrMat.getInscriptionMa(instAnn, e);
 	        	    		
 	        	    		modi = inscrMod.getInscription(module, instAnn);
 	        	    		if(modi!=null) {
 	        	    			modi.setNoteSN(scores.get(j));
 	        	    		}else {
 	        	    			modi = new InscriptionModule();
 	        	    			modi.setNoteSN(scores.get(j));
 	        	    			modi.setNoteSR(-1);
 	        	    			modi.setModule(module);
 	        	    			modi.setInscriptionAnnuelle(instAnn);
 	        	    			}
 	        	    		
 	        	    		
 	        	    		mat.setNoteSN(scores.get(j));
 	        	    	}else {
	 	        	        mat = new InscriptionMatiere();
	 	        	    	mat.setCoefficient(e.getCurrentCoefficient());
	 	        	    	mat.setMatiere(e);
	 	        	    	mat.setInscriptionAnnuelle(instAnn);
	 	        	    	mat.setNoteSN(scores.get(j));
	 	        	    	mat.setNoteSR(-1);
	 	        	    	
	 	        	    	modi = inscrMod.getInscription(module, instAnn);
 	        	    		if(modi!=null) {
 	        	    			modi.setNoteSN(scores.get(j));
 	        	    		}else {
 	        	    			modi = new InscriptionModule();
 	        	    			modi.setNoteSN(scores.get(j));
 	        	    			modi.setNoteSR(-1);
 	        	    			modi.setModule(module);
 	        	    			modi.setInscriptionAnnuelle(instAnn);
 	        	    			}
 	        	    		
	 	        	    	
	 	        	    	
 	        	    	}
 	        	    
 	        	    	j++;
 	        	    	
 	        	    	insM.add(mat);
 	        	    	insMd.add(modi);
 	        	    }
        	    }
	        	
	        	if(v) {
	        	 
	        	   System.out.println(insMd);
	        	   System.out.println("NOTES "  + NOTES );
	        	    
	        		httpSession.setAttribute("NOTES", insM);
	        	    httpSession.setAttribute("session", "SN");
	        		httpSession.setAttribute("NOTESModule", insMd);
		        	return "/prof/updateOrNot";
	        	}
	        	
	        	////////// ajouter la notes sans prendre le point de vue du prof en consideration
	        
        	    
        	    inscrMat.InsertNoteNorm(insM);
	        	
	        	return "/prof/userHomePage";
	        	
	        } else if("SR".equals(output.get(0).get(0))) {
	        	
	        	
	        	Boolean v = inscrMat.checkNoteRatt(listE, module.getNiveau().getIdNiveau(), module.getIdModule());
		        
	        	System.out.println(v);
	        	
	        	List<InscriptionMatiere> insM = new ArrayList<>(); 
        		List<InscriptionModule> insMd = new ArrayList<>(); 

        	    List<Float> scores = imp.getScores();
        	    
        	    Map<Element, Float> NOTES = new HashMap<>();
        	    
        	    System.out.println(scores);
        	    
        	    int j = 0;
        	    for(Etudiant et:listE) {
        	    
        	    	 InscriptionAnnuelle instAnn = insc.getInscription(et, module.getNiveau().getIdNiveau());
        	       	    	 
        	    	 for(Element e : module.getElements()) {
 	        	    	NOTES.put(e, scores.get(j));
 	        	    	InscriptionMatiere mat = null;
 	        	    	InscriptionModule modi = null;
 	        	    	
 	        	    	if(inscrMat.contains(instAnn, e)) {
 	        	    		mat = inscrMat.getInscriptionMa(instAnn, e);
 	         
 	        	    		modi = inscrMod.getInscription(module, instAnn);
 	        	    	  
 	        	    		if(modi!=null) {
 	        	    			modi.setNoteSN(scores.get(j));
 	        	    		}else {
 	        	    			modi = new InscriptionModule();
 	        	    			modi.setNoteSR(scores.get(j));
 	        	    			modi.setNoteSN(-1);
 	        	    			modi.setModule(module);
 	        	    			modi.setInscriptionAnnuelle(instAnn);
 	        	    			}
 	        	    		
 	        	    		
 	        	    		mat.setNoteSR(scores.get(j));
 	        	    	}else {
	 	        	        mat = new InscriptionMatiere();
	 	        	    	mat.setCoefficient(e.getCurrentCoefficient());
	 	        	    	mat.setMatiere(e);
	 	        	    	mat.setInscriptionAnnuelle(instAnn);
	 	        	    	mat.setNoteSR(scores.get(j));
	 	        	    	mat.setNoteSN(-1);
	 	        	    	
	 	        	    	modi = inscrMod.getInscription(module, instAnn);
 	        	    		if(modi!=null) {
 	        	    			modi.setNoteSR(scores.get(j));
 	        	    		}else {
 	        	    			modi = new InscriptionModule();
 	        	    			modi.setNoteSR(scores.get(j));
 	        	    			modi.setNoteSN(-1);
 	        	    			modi.setModule(module);
 	        	    			modi.setInscriptionAnnuelle(instAnn);
 	        	    			}
 	        	    		
	 	        	    	
	 	        	    	
 	        	    	}
 	        	    
 	        	    	j++;
 	        	    	
 	        	    	insM.add(mat);
 	        	    	insMd.add(modi);
 	        	    }
        	    }
	        	
	        	
	        	if(v) {
	        	   
	        	    
	        	   
	        	   inscrMat.InsertNoteRat(insM);
	        	   
	        	   System.out.println(insM);
	        	   System.out.println("NOTES "  + NOTES );
	        	    httpSession.setAttribute("session", "SR");
	        		httpSession.setAttribute("NOTES", insM);
	        		httpSession.setAttribute("NOTESModule", insMd);
	        		
		        	return "/prof/updateOrNot";
	        	}
	        	
	        	
	        	
        	   
        	    inscrMat.InsertNoteRat(insM);
	        	inscrMod.InsertOrUpdateInscrip(insMd);
	        	
	        	return "/prof/userHomePage";
	        }
	     
	        return "/prof/userHomePage";
	    }  
	
	
	@RequestMapping("/updateYes")
	public String updateChoiceYes(Model model) {
		List<InscriptionMatiere> malist = (List<InscriptionMatiere>)httpSession.getAttribute("NOTES");
		List<InscriptionModule> insMd = (List<InscriptionModule>)httpSession.getAttribute("NOTESModule");
		 if("SN".equals(httpSession.getAttribute("session"))) {
			 inscrMat.InsertNoteNorm(malist);
			 inscrMod.InsertOrUpdateInscrip(insMd);
			 
		 }else {
			 inscrMat.InsertNoteRat(malist);
			 inscrMod.InsertOrUpdateInscrip(insMd);
			 
		 }
		 
		 System.out.println("this is my list " + malist);
		
		return "/prof/userHomePage";
	}
	
	@RequestMapping("/dontupdate")
	public String updateChoiceNo(Model model) {
		
		return "/prof/userHomePage";
	}
	
	@RequestMapping("/records/export/excel")
	public void exportIntoExcel(HttpServletResponse response,@ModelAttribute("module") ModuleChoose module,Model model) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=records_" + currentDateTime + ".xlsx"; // to make sure mn extension cati akhir 5 letters mn l 3nwan if it is != .xlsx donc error
		response.setHeader(headerKey, headerValue);
		
        List<Etudiant> listE = insc.getListEtudiants(module.getNiveau());
        
	    Module m = imod.ContainsModule(module.getName(),module.getNiveau());
	    
	    UserAndAccountInfos userInfo = (UserAndAccountInfos) httpSession.getAttribute("userInfo");
        
        String profName = userInfo.getNom() + " "+ userInfo.getPrenom();
		System.out.println(profName);
		
	    if(m==null) {
	    	System.out.println("problem has occured!!!! "+module.getName()+" "+module.getNiveau());
	    	System.out.println(inscrMat.getEtudiantScoreDict(listE, module.getNiveau()));
	    }else {
	    	
	    	
	        UserAndAccountInfos userInfo2 = (UserAndAccountInfos) httpSession.getAttribute("userInfo");
 
	    	System.out.println(inscrMat.getEtudiantScoreDict(listE, module.getNiveau()));

	    	Map<Etudiant,Map<com.gsnotes.bo.Module,Map<Element,Double>>> et  = inscrMat.getEtudiantScoreDict(listE, module.getNiveau());
	    	
	    	for(Etudiant e : et.keySet()) {
	    		System.out.println("Etudiant : " + e.getNom());
	    		
	    		for(Module mo : et.get(e).keySet()) {
	    			System.out.println("Module : " + mo.getTitre());
	    			for(Element el : mo.getElements()) {
	    				System.out.println("Element : " + el.getNom() + " => Note normale = " + et.get(e).get(mo).get(el));
	    			}
	    		}
	    	}
	    	
	    	
	    	
	    	
	    	ExcelExporterForScores generator = new ExcelExporterForScores(module.getSession(),m,listE,userInfo2);
			generator.generate(response);
	    	
	    }
	
 	
		
		//return "/prof/ScoresImport"; //TODO : khlih yrederigik ma khdamax
	}

}
