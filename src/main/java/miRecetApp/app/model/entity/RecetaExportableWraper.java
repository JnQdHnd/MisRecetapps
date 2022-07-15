package miRecetApp.app.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Clase entity de envoltura para generar el archivo json de recetas en el proyecto.
 * @author Julian Quenard
 * 01-09-2021
 */
public class RecetaExportableWraper implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//ATRIBUTOS----------------------------------------------------------
	private Receta receta;
	private List<Producto> productosEnReceta;
	private List<Artefacto> artefactosEnReceta;
	private List<Receta> preparaciones;
	
	//METODOS----------------------------------------------------------
	
	public Receta getReceta() {
		return receta;
	}
	public void setReceta(Receta receta) {
		this.receta = receta;
	}
	public List<Producto> getProductosEnReceta() {
		return productosEnReceta;
	}
	public void setProductosEnReceta(List<Producto> productosEnReceta) {
		this.productosEnReceta = productosEnReceta;
	}
	public List<Artefacto> getArtefactosEnReceta() {
		return artefactosEnReceta;
	}
	public void setArtefactosEnReceta(List<Artefacto> artefactosEnReceta) {
		this.artefactosEnReceta = artefactosEnReceta;
	}
	public List<Receta> getPreparaciones() {
		return preparaciones;
	}
	public void setPreparaciones(List<Receta> preparaciones) {
		this.preparaciones = preparaciones;
	}
	
	
}
