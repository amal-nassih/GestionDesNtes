package com.gsnotes.utils.export;

import java.awt.PageAttributes.MediaType;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.gsnotes.bo.Element;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.ModuleChoose;
import com.gsnotes.dao.IModuleDao;
import com.gsnotes.dao.INiveauDao;
import com.gsnotes.web.models.UserAndAccountInfos;


public class ExcelExporterForScores {
	
	private XSSFWorkbook workbook;  //this is our excel file
	
	private XSSFSheet sheet;
	private String session;
	private String prof; 
	List<Etudiant> listE;
	Map<Etudiant,List<Double>> scores;
	private int nbrElements;

	
	private com.gsnotes.bo.Module m;
	
	public ExcelExporterForScores() {
		
	}

	private static String[][] HEADERs = {{ "MODULE", "SEMESTRE", "Année" },
			   { "Enseignant", "Session", "Classe" } };

	
	// entrer ici hadik l map li konti khdama 3liha
	public ExcelExporterForScores(String session,com.gsnotes.bo.Module m,List<Etudiant> listE,UserAndAccountInfos userInfo){
		workbook = new XSSFWorkbook(); // create new excel file
		this.session = session;
		this.prof = userInfo.getNom()+" "+userInfo.getPrenom();
		this.m=m;
		this.listE = listE;
	}
	
	private void writeHeader() {

		
		sheet = workbook.createSheet("Exam scores"); // our first sheet inside the workbook
		
		
	    CellStyle style = createStyle("NotnormalCell");
		CellStyle style2 = createStyle("normalCell");
		
		//making the main header
		 DateFormat dateFormatter = new SimpleDateFormat("yyyy");
		 String currentDateTime = dateFormatter.format(new Date());
		 
		 DateFormat dateMon = new SimpleDateFormat("mm");
		 String dateMonth = dateMon.format(new Date());		 
	
		 String[][] values = {{m.getTitre(),"PRINTEMPS",currentDateTime}
		                     ,{prof,session,m.getNiveau().getFiliere().getCodeFiliere()+m.getNiveau().getAlias()}};
		 
		int j=0;
		for(int i=0;i<2;i++) {
			Row row = sheet.createRow(i);
			j=0;
			for(int h=0;h<3;h++) {
				createCell(row,j,HEADERs[i][h],style);
				j+=2;
			}
			int d=1;
			for(int k=0;k<3;k++) {
				createCell(row,d,values[i][k],style2);
				d+=2;
			}
			
		}
			

	   

	
	    List<String> header2 = new ArrayList<>();
	    
	    header2.add("ID");
	    header2.add("CNE");
	    header2.add("NOM");
	    header2.add("PRENOM");
	    
	    List<Element> elms = m.getElements();
	    
	    nbrElements = elms.size();
	    
	    for(int i = 0; i< nbrElements;i++) {
	    	 header2.add(elms.get(i).getNom());
	    }
	    
	    header2.add("MOYENNE");
	    header2.add("VALIDATION");

	
	  
	    Row row31 = sheet.createRow(2);

	    
    	Row row3 = sheet.createRow(3);
	    
		for(int i=0;i<header2.size();i++) {
				createCell(row3,i,header2.get(i),style);
			}
			
			
	
		//set the sheet cells to autosize
	    for(int colNum =0; colNum<row3.getLastCellNum();colNum++) {
	    	sheet.autoSizeColumn(colNum);
	    }
	}

	private CellStyle createStyle(String s) {
		
		if("normalCell".equals(s)) {
			
			CellStyle style = workbook.createCellStyle();
			
			XSSFFont font = workbook.createFont();
			font.setFontName("Courier New");
			font.setItalic(true);
			font.setColor(HSSFColorPredefined.DARK_BLUE.getIndex());
			font.setBold(false);
			font.setFontHeight(20);
			style.setFont(font);		

			return style;
		}else {
			
		   CellStyle style = workbook.createCellStyle();
		    XSSFFont font = workbook.createFont();
			font.setFontName("Courier New");
			font.setItalic(true);
			font.setColor(HSSFColorPredefined.DARK_BLUE.getIndex());
			font.setBold(true);
			font.setFontHeight(20);
			style.setFont(font);
			
		   style.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.index);
		   style.setFillPattern(FillPatternType.BIG_SPOTS);
		   style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		   
		   style.setBorderBottom(BorderStyle.MEDIUM_DASH_DOT);
		   style.setBorderLeft(BorderStyle.MEDIUM_DASH_DOT);
			return style;
		}
		
	}
	private void createCell(Row row, int i, Object s, CellStyle style) {
		
		sheet.autoSizeColumn(i);
		Cell cell = row.createCell(i);
		if (s instanceof Integer) {
			cell.setCellValue((Integer) s);
			cell.setCellType(CellType.NUMERIC);
		} 
        else if (s instanceof Long) {
			cell.setCellValue((Long) s);
			cell.setCellType(CellType.NUMERIC);
		} else if (s instanceof Boolean) {
			cell.setCellValue((Boolean) s);
			cell.setCellType(CellType.BOOLEAN);
		} else {
			cell.setCellValue((String) s);
			cell.setCellType(CellType.STRING);
		}
		cell.setCellStyle(style);
		
	}
	
	private void write() {
		int rowCount = 4;

		CellStyle style = createStyle("normalCell");

	    for (Etudiant et : listE) {
	    	Row row = sheet.createRow(rowCount++);
				createCell(row,0,et.getIdUtilisateur(),style);
				createCell(row,1,et.getCne(),style);
				createCell(row,2,et.getNom(),style);
				createCell(row,3,et.getPrenom(),style);
				
				//TODO : add elements notes 
				//TODO : add the formula for both the last columns 
				//moyenne = sum(elementsNotes)/nbrOfElements
				//validation < 12 NV 

		}
	    
	    


		
	}
	
	
	public void generate(HttpServletResponse response) throws IOException {
		
		writeHeader();
		write();
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
        
		outputStream.close();
		
      
	}
	

 }
