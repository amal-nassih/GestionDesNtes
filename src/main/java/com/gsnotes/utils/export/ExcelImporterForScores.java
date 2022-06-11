package com.gsnotes.utils.export;

import java.awt.PageAttributes.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.gsnotes.bo.Element;
import com.gsnotes.bo.Enseignant;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.ModuleChoose;
import com.gsnotes.dao.IModuleDao;
import com.gsnotes.dao.INiveauDao;
import com.gsnotes.exceptionhandlers.AppGlobalExceptionHandler;
import com.gsnotes.exceptionhandlers.HeaderNotVerifiedException;
import com.gsnotes.exceptionhandlers.NoteIncorrectException;
import com.gsnotes.services.IProfService;
import com.gsnotes.web.models.UserAndAccountInfos;


public class ExcelImporterForScores {
	
	
    private MultipartFile file;
    private XSSFWorkbook workbook;  
	private XSSFSheet sheet;
	
    private List<String> Header = new ArrayList<>();
    private List<String> Header2 = new ArrayList<>();
    private List<Float> scores=new ArrayList<>();
    private List<String> cneList = new ArrayList<>();
    private List<Enseignant> e = new ArrayList<>();
    private List<com.gsnotes.bo.Module> m = new ArrayList<>(); 
   
    private List<InscriptionAnnuelle> inscrpAn = new ArrayList<>();
    
    private UserAndAccountInfos userInfo;
    
    private List<Double> moyenne = new ArrayList<>();
    
    private List<String> validation = new ArrayList<>();
   
    //elements from excel  :
	private List<String> elems = new ArrayList<>();
	
	
	public ExcelImporterForScores(MultipartFile file,UserAndAccountInfos userInfo,List<Enseignant> profs,List<com.gsnotes.bo.Module> m) throws IllegalStateException, IOException{
		this.file=file;
		this.userInfo = userInfo;
		this.e= profs;
		this.m = m;
	}
	
	
	

	// Question 1 Done !!!! bravo 
	public Boolean checkTheFormat(){
		
		String[] extensions = {"xlsx","xlsm","xlsb","xltx","xltm","xls","xlt","xls","xml","xlam","xla","xlw","xlr"};
		Boolean endsWith = false;
		String fN = file.getOriginalFilename();
	    String extension = fN.substring(fN.lastIndexOf(".") + 1,fN.length());

		for(String e : extensions) {
		    System.out.println(extension.equals(e));
			if(e.equals(extension)) {
				endsWith = true;
				break;
			}
		}
		return endsWith;
	}
	
	
	//Question 2 : 
	public Boolean verifyScores() {
		Boolean v = true;
		for (Float s : scores) {
			if(s<0 || s>20) {
				v = false;
			}
		}
		
		return v;
	}
	
	//Question 3 : verify the header : 
	public Boolean verifyHeader(FileInputStream fileInput) throws IOException {
		Boolean v = true;
		XSSFWorkbook workbook2 = new XSSFWorkbook(fileInput);
		XSSFSheet sheet2=workbook2.getSheetAt(0);
		
		System.out.println(fileInput.toString());
		int firstRow = sheet2.getFirstRowNum();
		int lastRow = sheet2.getLastRowNum();

		for(int index = 0 ;index<=1;index++) {
		
				Row row = sheet2.getRow(index);
				
					for(int cellIndex = row.getFirstCellNum() ; cellIndex< row.getLastCellNum() ; cellIndex++) {
						Cell cell= row.getCell(cellIndex,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);;
						printCellValue(cell,"header2");
					}
				
				}
		
		int k = 0;
		for(String h : Header) {
			 System.out.println(h+"//////"+Header2.get(k));
			if(!h.equals(Header2.get(k))) {
				System.out.println(h+"!="+Header2.get(k));
				v=false;
			   break;
			}
			k++;
		}
      
		
		fileInput.close();
		return v;
	}
	
	
	//Question 3 : based on the database 
	
	public Boolean verifyHeaderFromDB() throws IOException {
		Boolean v = true;
	
		List<String> head = new ArrayList<>();
	    String[] HEADERs = { "MODULE", "SEMESTRE", "Année" ,
				    "Enseignant", "Session", "Classe"  };
	    
	    // make sure the header structure hasn't changed
	    int j = 0;
	    for(int k = 0;k<12 ; k+=2) {
	    	System.out.println(HEADERs[j] + " ?? " +Header.get(k));
	    	if(!(Header.get(k)).equals(HEADERs[j])) {
	    		v = false;
	    		return v;
	    	}
	    	j++;
	    }
	    
	  //check the prof : 
		
	  		String profName = Header.get(7);
	  		v = false;
	  		for(Enseignant i : e) {
	  			System.out.println(i.getNom()+" "+i.getPrenom());
	  			if(profName.equals(i.getNom()+" "+i.getPrenom())) {
	  				System.out.println("Prof verified  !!!");
	  				v=true;
	  			}
	  		}
	  		
	  		if(v==false) {
	  	    	return v;
	  	    }
		
	    // check the MODULE 
	    v=false;
	    for(com.gsnotes.bo.Module mo : m ) {
	    	if(mo.getTitre().equals(Header.get(1))) {
	    		System.out.println(Header.get(11)+" ??? " + mo.getNiveau().getFiliere().getCodeFiliere()+mo.getNiveau().getAlias() );
	    		if(Header.get(11).equals(mo.getNiveau().getFiliere().getCodeFiliere()+mo.getNiveau().getAlias())) {
	    			v=true;
	    			System.out.println("module and class verified !!");
	    			break;
	    		}
	    	}
	    }
	    
	    if(v==false) {
	    	System.out.println("not module");
	    	return v;
	    }
	    
	    //check the date : 
	    
	    DateFormat dateFormatter = new SimpleDateFormat("yyyy");
		String currentDateTime = dateFormatter.format(new Date());
		
		v = false;
		if(currentDateTime.equals(Header.get(5)) ) {
			v=true;
			System.out.println("date verified");
		}
		
		if(v==false) {
	    	return v;
	    }

		
		
		

		
		// check the elements :
		v=false;
		List<String> elMod = new ArrayList<>();
		
		for(com.gsnotes.bo.Module modu : m ) {
			
			if(Header.get(1).equals(modu.getTitre())){
				for(Element el : modu.getElements() ) {
					elMod.add(el.getNom());
				}
			}
		}
		
		System.out.println(elMod+"|||" + elems);
		
		v=false;
		
		if(elMod.size() == elems.size()) {
			if(elMod.containsAll(elems)) {
				v = true;
			}
		}
		
		if(v==false) {
	    	return v;
	    }
		
		//verify that there is a insriptions annuelle of the student in the niveau
		//verify the date == date d'inscription annuelle
		//verify the formula to calculate the moyenne and the validation
		
	
	
		return v;
	}
	
	
	// verifying the formula : 
	
	public Boolean verifyFormula() {
		Boolean b = true;
		
		for(Double moy : moyenne) {
		    //check the moyenne
		}
		
		//3la hsab l moyenne
		int j = 0;
		for(String vali : validation) {
		  
			if(!((moyenne.get(j)<=12 && "v".equals(vali))||(moyenne.get(j)>12 && "nv".equals(vali)))) {
				b=false;
				break;
			}
			j++;
		}
		
		return b;
	}
	
	
	public void printCellValue(Cell cell,String storeIn) {
		CellType cellType = cell.getCellType().equals(CellType.FORMULA)?cell.getCachedFormulaResultType():cell.getCellType();
		
		if(cellType.equals(CellType.STRING)) {
			System.out.println(cell.getStringCellValue()+"|");
			
			//store the cell value
			if("header".equals(storeIn)) {
				Header.add(cell.getStringCellValue());
			}else if("cne".equals(storeIn)) {
				cneList.add(cell.getStringCellValue());
			}else if("header2".equals(storeIn)) {
				Header2.add(cell.getStringCellValue());
			}else if("elements".equals(storeIn)) {
				elems.add(cell.getStringCellValue());
			}else if("validation".equals(storeIn)) {
				validation.add(cell.getStringCellValue());
			}
		}
		if(cellType.equals(CellType.NUMERIC)) {
			
			if(DateUtil.isCellDateFormatted(cell)) {
				System.out.println(cell.getDateCellValue()+"|");
				
				
			}else {
				System.out.println(cell.getNumericCellValue()+"|");
				
				if("scores".equals(storeIn)) {
					scores.add((float) cell.getNumericCellValue());
				}else if("moyenne".equals(storeIn)) {
					moyenne.add((double) cell.getNumericCellValue());
				}
			}
			
			
		}
		if(cellType.equals(CellType.BOOLEAN)) {
			System.out.println(cell.getBooleanCellValue()+"|");
		}
				
	}
	

	

	public void getAllTheCells() {
		
		int firstRow = sheet.getFirstRowNum();
		int lastRow = sheet.getLastRowNum();

		for(int index = firstRow ;index<=lastRow;index++) {
		
			if(!(index==2)) {
				Row row = sheet.getRow(index);

				if(index==0 || index==1) {
					
					for(int cellIndex = row.getFirstCellNum() ; cellIndex< row.getLastCellNum() ; cellIndex++) {
						Cell cell= row.getCell(cellIndex,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);;
						printCellValue(cell,"header");
					}
				}
				
				if(index==3) {
					for(int cellIndex = row.getFirstCellNum() + 4 ; cellIndex < row.getLastCellNum() - 2 ; cellIndex++ ) {
						Cell cell= row.getCell(cellIndex,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);;
						printCellValue(cell,"elements");
					}
				}
				if(index>3) {
					
					for(int cellIndex = row.getFirstCellNum() ; cellIndex< row.getLastCellNum() ; cellIndex++) {
						
						if(cellIndex==1) {
							Cell cell= row.getCell(cellIndex,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);;
							printCellValue(cell,"cne");
						}
						
						if(cellIndex>3 && cellIndex< row.getLastCellNum() - 2) { // TODO : TO BE CHANGED FACH DIRI MOYENNE AND VALIDATION
							Cell cell= row.getCell(cellIndex,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);;
							printCellValue(cell,"scores");
						}
						
						if(cellIndex == row.getLastCellNum() - 2) {
							Cell cell= row.getCell(cellIndex,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);;
							printCellValue(cell,"moyenne");
						}
						
						if(cellIndex == row.getLastCellNum() - 1) { 
							Cell cell= row.getCell(cellIndex,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);;
							printCellValue(cell,"validation");
						}
						
					
					}
					
				}
			}
			
		}
      
		
	}
	
	public List<Float> getScores(){
		System.out.println(scores);
		return scores;
	}
	public List<List<String>> getFile() throws IOException{
		

		
		if(checkTheFormat()==false) {
			System.out.println(checkTheFormat()+" doesn't have the desired extension");
			throw new NoteIncorrectException("please check the file format it's not an excel file !!!");
		}else {
			
			FileInputStream f = new FileInputStream(new File("C:\\Users\\amalo\\Downloads\\upload\\"+file.getOriginalFilename()));
			
	        //Create Workbook instance holding reference the desired file
			workbook = new XSSFWorkbook(f);
			sheet = workbook.getSheetAt(0);
			

			getAllTheCells();
			
            
			if(verifyScores()) {
				System.out.println("verified");
				
				// check the Header 
			//	FileInputStream f2 = new FileInputStream(new File("C:\\"+"records_2022-05-22_17_22_31.xlsx"));
			//	Boolean vh = verifyHeader(f2);
				
				Boolean vh = verifyHeaderFromDB();
				if(vh) {
					System.out.println("verified header");
				}else {
					System.out.println("please try not to change the header !!!");
					throw new HeaderNotVerifiedException("please try not to change the header");

				}
				//f2.close();

			}else {
				System.out.println("not verified"); // hna khas throw exception
				throw new NoteIncorrectException("you have entered wrong scores it must be between 0 and 20");
			}
			

			
            f.close();
		}
		
		List<String> myOutput1 = new ArrayList<>();
		
		myOutput1.add(Header.get(9)); // session
		myOutput1.add(Header.get(1));  // nom module
		
		List<List<String>> myOutput2 = new ArrayList<>();
		
		myOutput2.add(myOutput1);
		myOutput2.add(cneList);
		

		
		
	
			return myOutput2;
		
		

	}
	
 }
