package com.test.microservices;

import java.io.FileOutputStream;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.test.microservices.pojos.*;
import com.test.microservices.repositories.*;
@Component
public class Verification_Liens {
	public Verification_Liens(ChampionRepository s,PaysRepository d) {
		this.destRepo=d;
		this.srcRepo=s;
		try {
			document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream("./Rapports/Champion_vers_NvPays.pdf"));
			document.open();
			document.addTitle("Rapport");
		    document.addSubject("verifier la validite des references");
		    document.addKeywords("Java, PDF, iText, Spring");
		    document.addAuthor("GONSALLO Ayrton");
		    document.addCreator("GONSALLO Ayrton");
		    table = new PdfPTable(4);
		    table.setWidths(new int[]{1,2, 4, 2});
		    PdfPCell c1 = new PdfPCell(new Phrase("Erreur n°"));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
		    c1 = new PdfPCell(new Phrase("Id Champion"));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        c1 = new PdfPCell(new Phrase("Id Mongo Champion"));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        c1 = new PdfPCell(new Phrase("Id Nv Pays"));
	        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(c1);
	        table.setHeaderRows(1);
			font = FontFactory.getFont(FontFactory.COURIER, 24, Font.BOLD);
			font3 = FontFactory.getFont(FontFactory.COURIER, 20, BaseColor.GREEN);
			
			font1 = FontFactory.getFont(FontFactory.COURIER, 18, BaseColor.RED);
			font2 = FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLUE);
			document.add(new Chunk("Champion vers Nv Pays:", font).setUnderline(0.1f, -2f));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	Font font, font1,font2,font3;
	Document document;
	PdfPTable table;
	ChampionRepository srcRepo;
	PaysRepository destRepo;
	int trouves;
	int nontrouves;
 public void check() throws Exception{
	 List<Champion> tous=srcRepo.findAll();
	 for(Champion element:tous) {
		 if(element.getNvPaysID()==null || destRepo.existsByAbreviation(element.getNvPaysID())) {
			 trouves++;
		 }
		 else {
			 nontrouves++;
			 table.addCell(String.valueOf(nontrouves));
			 table.addCell(String.valueOf(element.getId()));
			 table.addCell(element.getIdMongo());
			 table.addCell(String.valueOf(element.getNvPaysID()));
			 
		 }
	 }
	 document.add(new Paragraph("Elements non trouvés:  ", font3));
	 document.add(new Paragraph("\n", font3));
	 document.add(table);
	document.add(new Paragraph("total trouvés:  "+trouves+" / "+tous.size(), font1));
	document.add(new Paragraph(StringUtils.repeat(" ", 5)+StringUtils.repeat("-", 30)+StringUtils.repeat(" ", 5), font));
	document.close();
	 System.out.println("traitement fini et document exporté!");
 }
}
