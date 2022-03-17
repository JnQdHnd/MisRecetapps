package miRecetApp.app.controller;

import java.awt.Color;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.LocaleResolver;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import miRecetApp.app.model.entity.Artefacto;
import miRecetApp.app.model.entity.ArtefactoEnUso;
import miRecetApp.app.model.entity.Ingrediente;
import miRecetApp.app.model.entity.Instruccion;
import miRecetApp.app.model.entity.Producto;
import miRecetApp.app.model.entity.Receta;
import miRecetApp.app.service.IArtefactoService;
import miRecetApp.app.service.IProductoService;
import miRecetApp.app.service.IRecetaService;

/**
 * Clase que controla la gestión la ventana de inicio. 
 * 
 * @author Julian Quenard
 */
@Controller
public class PDFController {
	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private IArtefactoService artefactoService;
	
	@Autowired
	private IRecetaService recetaService;
	
	@Autowired
	LocaleResolver localeResolver;
	
	@GetMapping(value = {"/receta/verReceta/exportaPDF/{recetaId}"})
	public void exportaAPdf(@PathVariable(name = "recetaId") long recetaId, HttpServletResponse response) throws DocumentException, IOException {
		
		Receta receta = recetaService.findOne(recetaId);
		System.out.println("****EXPORTANDO A PDF RECETA: " + receta.getNombre());
		
		response.setContentType("application/pdf");
		
		String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=MisRecetApps - " + receta.getNombre() + ".pdf";
        response.setHeader(headerKey, headerValue);
        
        export(response, receta);
	}
	
	public void export(HttpServletResponse response, Receta receta) throws DocumentException, IOException {
		
		System.out.println("TAMAÑO DE LISTADO DE PRODUCTOS" + productoService.findAll().size());
		
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
         
        document.open();
        
        FontFactory.register("Quicksand-VariableFont_wght.ttf");
		FontFactory.register("Quicksand-Bold.ttf");
		FontFactory.register("Quicksand-Medium.ttf");
		FontFactory.register("Quicksand-SemiBold.ttf");
		FontFactory.register("Quicksand-Regular.ttf");
		
		System.out.println("ACTIVADO EL PDF DE RECETA: " + receta.getNombre());
		Font fontTitulo = FontFactory.getFont("Quicksand", 16, Font.BOLD, new Color(86, 61, 124));
		PdfPCell cell = null;
			
		PdfPTable tabla1 = new PdfPTable(2);
		tabla1.setWidthPercentage(75);
		tabla1.setWidths(new int [] {20,330});
		cell = new PdfPCell();
		cell.setBackgroundColor(new Color(247, 247, 247));
		cell.setPadding(8f);
		cell.setPaddingTop(4);
		cell.setPaddingBottom(9);
		cell.setFixedHeight(40f);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		
		cell.setBorder(0);
		cell.setBorderWidthTop(0.5f);
		cell.setBorderWidthBottom(0.5f);
		cell.setBorderWidthLeft(0.5f);
		cell.setBorderColorLeft(new Color(86, 61, 124));
		cell.setBorderColorTop(new Color(86, 61, 124));
		cell.setBorderColorBottom(Color.LIGHT_GRAY);
		
		tabla1.addCell(cell);
		cell.setPhrase(new Phrase(receta.getNombre(), fontTitulo));
		
		cell.setBorderWidthLeft(0);
		cell.setBorderWidthRight(0.5f);
		cell.setBorderColorRight(new Color(86, 61, 124));
		
		tabla1.addCell(cell);
		
		Font fuenteCuerpoTitulo = FontFactory.getFont("Quicksand", 14, Font.BOLD, new Color(86, 61, 124));
		Font fuenteCuerpoSubtitulo = FontFactory.getFont("Quicksand", 12, Font.BOLD, new Color(86, 61, 124));
		Font fuenteCuerpo = FontFactory.getFont("Quicksand", 12, Font.NORMAL, Color.DARK_GRAY);
		Font fuentePunto = FontFactory.getFont("Quicksand", 12, Font.NORMAL, new Color(127, 124, 130));
		
		PdfPCell cellCuerpo = null;
		cellCuerpo = new PdfPCell();
		cellCuerpo.setBackgroundColor(new Color(249, 248, 250));
		cellCuerpo.setBorder(0);
		cellCuerpo.setBorderColorLeft(new Color(86, 61, 124));
		cellCuerpo.setBorderColorRight(new Color(86, 61, 124));
		cellCuerpo.setBorderColorBottom(new Color(86, 61, 124));
		cellCuerpo.setBorderColorTop(Color.LIGHT_GRAY);
		cellCuerpo.setPadding(8f);
		cellCuerpo.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		
		PdfPTable tabla2 = new PdfPTable(2);
		cellCuerpo.setPaddingTop(10);
		cellCuerpo.setPaddingBottom(2);
		tabla2.setWidthPercentage(75);
		tabla2.setWidths(new int [] {20,330});
		
		cellCuerpo.setPhrase(new Phrase("", fuentePunto));
		cellCuerpo.setBorderWidthLeft(0.5f);	
		tabla2.addCell(cellCuerpo);
		
		cellCuerpo.setPhrase(new Phrase("Porciones: " + (int)receta.getPorciones(), fuenteCuerpoTitulo));
		cellCuerpo.setBorderWidthLeft(0);
		cellCuerpo.setBorderWidthRight(0.5f);		
		tabla2.addCell(cellCuerpo);
		
		cellCuerpo.setPhrase(new Phrase(""));
		cellCuerpo.setBorderWidthLeft(0.5f);
		cellCuerpo.setBorderWidthRight(0);
		tabla2.addCell(cellCuerpo);
		cellCuerpo.setBorderWidthLeft(0);
		cellCuerpo.setBorderWidthRight(0.5f);
		tabla2.addCell(cellCuerpo);
		
		PdfPTable tabla3 = new PdfPTable(3);
		cellCuerpo.setPaddingTop(10);
		cellCuerpo.setPaddingBottom(6);
		tabla3.setWidthPercentage(75);
		tabla3.setWidths(new int [] {20,165,165});
		
		cellCuerpo.setPhrase(new Phrase(""));
		cellCuerpo.setBorderWidthLeft(0.5f);
		cellCuerpo.setBorderWidthRight(0);
		cellCuerpo.setBorderWidthTop(0.5f);		
		tabla3.addCell(cellCuerpo);
		
		cellCuerpo.setPhrase(new Phrase("Ingredientes:", fuenteCuerpoTitulo));
		cellCuerpo.setBorderWidthLeft(0);
		cellCuerpo.setBorderWidthRight(0);
		tabla3.addCell(cellCuerpo);
		
		cellCuerpo.setPhrase(new Phrase(""));
		cellCuerpo.setBorderWidthLeft(0);
		cellCuerpo.setBorderWidthRight(0.5f);
		tabla3.addCell(cellCuerpo);
		
		cellCuerpo.setBorderWidthTop(0);
		
		for(Ingrediente ingrediente:receta.getIngredientes()) {
			System.out.println("INGREDIENTE PRODUCTO ID: " + ingrediente.getProductoId());
			Producto producto = productoService.findOne(ingrediente.getProductoId());
			String productoNombre = producto.getNombre();
			cellCuerpo.setPaddingBottom(4);
			cellCuerpo.setPaddingTop(4);
			
			cellCuerpo.setPhrase(new Phrase("•", fuentePunto));
			cellCuerpo.setBorderWidthLeft(0.5f);
			cellCuerpo.setBorderWidthRight(0);
			tabla3.addCell(cellCuerpo);
			
			cellCuerpo.setPhrase(new Phrase(productoNombre, fuenteCuerpo));
			cellCuerpo.setBorderWidthLeft(0);
			cellCuerpo.setBorderWidthRight(0);
			tabla3.addCell(cellCuerpo);
			
			cellCuerpo.setPhrase(new Phrase(ingrediente.getCantidadFormateado() + " " + ingrediente.getUnidadDeMedida().getSimbolo() + "   ", fuenteCuerpo));
			cellCuerpo.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			cellCuerpo.setBorderWidthLeft(0);
			cellCuerpo.setBorderWidthRight(0.5f);
			tabla3.addCell(cellCuerpo);
			cellCuerpo.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		}
		cellCuerpo.setPhrase(new Phrase(""));
		cellCuerpo.setBorderWidthLeft(0.5f);
		cellCuerpo.setBorderWidthRight(0);
		tabla3.addCell(cellCuerpo);
		cellCuerpo.setBorderWidthLeft(0);
		cellCuerpo.setBorderWidthRight(0);
		tabla3.addCell(cellCuerpo);
		cellCuerpo.setBorderWidthLeft(0);
		cellCuerpo.setBorderWidthRight(0.5f);
		tabla3.addCell(cellCuerpo);
		
		PdfPTable tabla4 = new PdfPTable(3);
		cellCuerpo.setPaddingTop(10);
		cellCuerpo.setPaddingBottom(6);
		tabla4.setWidthPercentage(75);
		tabla4.setWidths(new int [] {20,165,165});
		
		cellCuerpo.setPhrase(new Phrase(""));
		cellCuerpo.setBorderWidthLeft(0.5f);
		cellCuerpo.setBorderWidthRight(0);
		cellCuerpo.setBorderWidthTop(0.5f);
		tabla4.addCell(cellCuerpo);
		
		cellCuerpo.setPhrase(new Phrase("Artefactos:", fuenteCuerpoTitulo));
		cellCuerpo.setBorderWidthLeft(0);
		cellCuerpo.setBorderWidthRight(0);
		tabla4.addCell(cellCuerpo);
		
		cellCuerpo.setPhrase(new Phrase(""));
		cellCuerpo.setBorderWidthLeft(0);
		cellCuerpo.setBorderWidthRight(0.5f);
		tabla4.addCell(cellCuerpo);
		
		cellCuerpo.setBorderWidthTop(0);
		
		for( ArtefactoEnUso artefacto:receta.getArtefactosUtilizados()) {
			cellCuerpo.setPaddingBottom(4);
			cellCuerpo.setPaddingTop(4);
			
			cellCuerpo.setPhrase(new Phrase("•", fuentePunto));
			cellCuerpo.setBorderWidthLeft(0.5f);
			cellCuerpo.setBorderWidthRight(0);
			tabla4.addCell(cellCuerpo);
			
			Artefacto a = artefactoService.findOne(artefacto.getArtefactoId());
			
			cellCuerpo.setPhrase(new Phrase(a.getNombre(), fuenteCuerpo));
			cellCuerpo.setBorderWidthLeft(0);
			cellCuerpo.setBorderWidthRight(0);
			tabla4.addCell(cellCuerpo);
			
			if(a.isEsHorno()) {
				cellCuerpo.setPhrase(new Phrase(artefacto.getMinutosDeUso() + " minutos a " + artefacto.getTemperatura() + " " + artefacto.getUnidadDeTemperatura().getSimbolo(), fuenteCuerpo));
			} 
			else {
				cellCuerpo.setPhrase(new Phrase(artefacto.getMinutosDeUso() + " minutos a intensidad " + artefacto.getIntensidadDeUso(), fuenteCuerpo));
			}
			cellCuerpo.setBorderWidthLeft(0);
			cellCuerpo.setBorderWidthRight(0.5f);
			cellCuerpo.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
			tabla4.addCell(cellCuerpo);
			
			cellCuerpo.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		}
		cellCuerpo.setPhrase(new Phrase(""));
		cellCuerpo.setBorderWidthLeft(0.5f);
		cellCuerpo.setBorderWidthRight(0);
		tabla4.addCell(cellCuerpo);
		cellCuerpo.setBorderWidthLeft(0);
		cellCuerpo.setBorderWidthRight(0);
		tabla4.addCell(cellCuerpo);
		cellCuerpo.setBorderWidthLeft(0);
		cellCuerpo.setBorderWidthRight(0.5f);
		tabla4.addCell(cellCuerpo);
		
		PdfPTable tabla5 = new PdfPTable(2);
		cellCuerpo.setPaddingTop(10);
		cellCuerpo.setPaddingBottom(6);
		tabla5.setWidthPercentage(75);
		tabla5.setWidths(new int [] {20,330});
		cellCuerpo.setPhrase(new Phrase(""));
		cellCuerpo.setBorderWidthLeft(0.5f);
		cellCuerpo.setBorderWidthRight(0);
		cellCuerpo.setBorderWidthTop(0.5f);
		tabla5.addCell(cellCuerpo);
		cellCuerpo.setPhrase(new Phrase("Intrucciones:", fuenteCuerpoTitulo));
		cellCuerpo.setBorderWidthLeft(0);
		cellCuerpo.setBorderWidthRight(0.5f);
		tabla5.addCell(cellCuerpo);
		cellCuerpo.setBorderWidthTop(0);
		for( Instruccion instruccion:receta.getInstrucciones()) {			
			cellCuerpo.setPaddingTop(4);
			cellCuerpo.setPaddingBottom(2);
			cellCuerpo.setPhrase(new Phrase("•", fuentePunto));
			cellCuerpo.setBorderWidthLeft(0.5f);
			cellCuerpo.setBorderWidthRight(0);
			tabla5.addCell(cellCuerpo);
			cellCuerpo.setPhrase(new Phrase("Paso " + instruccion.getOrden(), fuenteCuerpoSubtitulo));
			cellCuerpo.setBorderWidthLeft(0);
			cellCuerpo.setBorderWidthRight(0.5f);
			tabla5.addCell(cellCuerpo);		
			cellCuerpo.setPaddingTop(2);
			cellCuerpo.setPaddingBottom(5);
			cellCuerpo.setPhrase(new Phrase(""));
			cellCuerpo.setBorderWidthLeft(0.5f);
			cellCuerpo.setBorderWidthRight(0);
			tabla5.addCell(cellCuerpo);
			cellCuerpo.setPhrase(new Phrase(instruccion.getInstruccion(), fuenteCuerpo));
			cellCuerpo.setBorderWidthLeft(0);
			cellCuerpo.setBorderWidthRight(0.5f);
			tabla5.addCell(cellCuerpo);
		}
		cellCuerpo.setPhrase(new Phrase(""));
		cellCuerpo.setBorderWidthLeft(0.5f);
		cellCuerpo.setBorderWidthRight(0);
		tabla5.addCell(cellCuerpo);
		cellCuerpo.setBorderWidthLeft(0);
		cellCuerpo.setBorderWidthRight(0.5f);
		tabla5.addCell(cellCuerpo);
		cellCuerpo.setBorderWidthLeft(0.5f);
		cellCuerpo.setBorderWidthRight(0);
		cellCuerpo.setBorderWidthBottom(0.5f);
		tabla5.addCell(cellCuerpo);
		cellCuerpo.setBorderWidthLeft(0);
		cellCuerpo.setBorderWidthRight(0.5f);
		tabla5.addCell(cellCuerpo);
		
		document.add(tabla1);
		document.add(tabla2);
		document.add(tabla3);
		document.add(tabla4);
		document.add(tabla5);
         
        document.close();       
    }
	
}	