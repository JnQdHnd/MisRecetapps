package miRecetApp.app.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import miRecetApp.app.model.entity.Artefacto;
import miRecetApp.app.model.entity.ArtefactoEnUso;
import miRecetApp.app.model.entity.Ingrediente;
import miRecetApp.app.model.entity.Instruccion;
import miRecetApp.app.model.entity.Preparacion;
import miRecetApp.app.model.entity.Producto;
import miRecetApp.app.model.entity.Receta;
import miRecetApp.app.model.entity.RecetaExportableWraper;
import miRecetApp.app.service.IArtefactoService;
import miRecetApp.app.service.IProductoService;
import miRecetApp.app.service.IRecetaService;

/**
 * Clase que controla la gestión la ventana de inicio. 
 * 
 * @author Julian Quenard
 * 01-09-2021
 */

@Controller
public class JsonController {
	
	@Autowired
    ResourceLoader resourceLoader;
	
	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private IArtefactoService artefactoService;
	
	@Autowired
	private IRecetaService recetaService;
	
	@Autowired
	LocaleResolver localeResolver;
	
	@PostMapping(value = {"/receta/incorporaRecetaJson"})
	public String leyendoRecetaJson(RedirectAttributes flash, 
									MultipartHttpServletRequest multipartRequest) 
											throws IllegalStateException, IOException,  JsonParseException, JsonMappingException {
		
		MultipartFile recetaJson = multipartRequest.getFile("recetaJson");
		if(!recetaJson.isEmpty()) {
			File file = cargaArchivo(recetaJson);
			
		    //Json a Objeto Receta
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			RecetaExportableWraper rw = objectMapper.readValue(file, RecetaExportableWraper.class);
			Receta receta = rw.getReceta();
			List<Producto> productosEnReceta = rw.getProductosEnReceta();
			List<Artefacto> artefactosEnReceta = rw.getArtefactosEnReceta();
			List<Receta> preparacionesEnReceta = rw.getPreparaciones();
			boolean esRecetaConPreparaciones = preparacionesEnReceta != null && preparacionesEnReceta.size() > 0;
			System.out.println("esRecetaConPreparaciones: " + esRecetaConPreparaciones);			
			
			comprobandoProductosEnReceta(productosEnReceta, receta, flash, esRecetaConPreparaciones, preparacionesEnReceta);
			comprobandoArtefactosEnReceta(artefactosEnReceta, receta, flash, esRecetaConPreparaciones, preparacionesEnReceta);			
			comprobandoRecetaEnBD(receta, flash);	
			comprobandoPreparacionesEnBD(preparacionesEnReceta, flash, esRecetaConPreparaciones);
			limpiandoIdsParaGuardadoEnBD(receta, esRecetaConPreparaciones, preparacionesEnReceta);			
			
			if(esRecetaConPreparaciones) {
				List <Preparacion> preparacionesTemp = new ArrayList<>();				
				for(Receta r : preparacionesEnReceta) {
					recetaService.save(r);
					Long recetaId = recetaService.findByNombre(r.getNombre()).getId();
					Preparacion p = new Preparacion();
					p.setRecetaId(recetaId);
					preparacionesTemp.add(p);
				}
				receta.setPreparaciones(preparacionesTemp);	
				recetaService.save(receta);					
				String mensajeFlashReceta = "La receta " + receta.getNombre() + " se ha importado con éxito!";
				flash.addFlashAttribute("success", mensajeFlashReceta);	
				return "redirect:/receta/verRecetaConPreparaciones/" + recetaService.findByNombre(receta.getNombre()).getId();				
			}
			
			recetaService.save(receta);			
			String mensajeFlashReceta = "La receta " + receta.getNombre() + " se ha importado con éxito!";
			flash.addFlashAttribute("success", mensajeFlashReceta);	
			return "redirect:/receta/verReceta/" + recetaService.findByNombre(receta.getNombre()).getId();
		}
		
		String mensajeFlashProducto = "¡Error al leer el archivo o al convertirlo en receta!";
		flash.addFlashAttribute("danger", mensajeFlashProducto);
		return "redirect:/inicio";	
	}	
	
	public File cargaArchivo(MultipartFile recetaJson) throws IOException {
		File file = null;
		System.out.println("GUARDANDO RECETA COMPARTIDA DESDE ARCHIVO JSON");
		String uniqueNameFile = UUID.randomUUID().toString() + " - " + recetaJson.getOriginalFilename();	
		Path rootPath = Paths.get("recetas-importadas").resolve(uniqueNameFile);
		File archivo = rootPath.toFile();				
		if(archivo.exists() && archivo.canRead()) {
			archivo.delete();
		}
		Path rootAbsolutePath = rootPath.toAbsolutePath();			
		Files.copy(recetaJson.getInputStream(), rootAbsolutePath);			
		String ruta = rootAbsolutePath.toString();
		file = new File(ruta);	
		recetaJson.transferTo(file);
		
		return file;
	}
	
	public void comprobandoRecetaEnBD(Receta receta, RedirectAttributes flash) {
		//VERIFICANDO SI EXISTE LA RECETA !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		boolean existeRecetaEnBD = recetaService.existsByNombre(receta.getNombre());
		if(existeRecetaEnBD) {				
			for(int i = 1; existeRecetaEnBD; i++) {
				String versionPrevia = " ~ v" + (i-1);
				String version = " ~ v" + i;
				String nombreBase = receta.getNombre().replaceAll(versionPrevia, "");					
				String nombreNuevo = nombreBase + version;
				receta.setNombre(nombreNuevo);
				existeRecetaEnBD = recetaService.existsByNombre(receta.getNombre());	
			}
			String mensajeFlashReceta = "Ya existe una receta con ese nombre. Se importará como una nueva versión!";				
			flash.addFlashAttribute("info", mensajeFlashReceta);			
		}
	}
	
	public void comprobandoPreparacionesEnBD(List<Receta> preparacionesEnReceta, RedirectAttributes flash, boolean esRecetaConPreparaciones) {
		if(esRecetaConPreparaciones) {
			//VERIFICANDO PREPARACIONES DE LA RECETA !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!	
			for(Receta r : preparacionesEnReceta) {
				comprobandoRecetaEnBD(r, flash);
			}
		}	
	}
	
	public void comprobandoProductosEnReceta(List<Producto> productosEnReceta, Receta receta, RedirectAttributes flash, boolean esRecetaConPreparaciones, List<Receta> preparacionesEnReceta) {
		//VERIFICANDO SI EXISTEN LOS PRODUCTOS EN LA BD
		for(Producto pEnReceta : productosEnReceta) {
			String nombrePR = pEnReceta.getNombre();
			boolean exitePRenBD = productoService.existsByNombre(nombrePR); 
			System.out.println("¿¿¿¿¿¿exitePRenBD???????: " + exitePRenBD);
			if(exitePRenBD) {
				Producto pBD = productoService.findByNombre(nombrePR);
				boolean coincideId = pBD.getId() == pEnReceta.getId();
				System.out.println("¿¿¿¿¿¿coincideId???????: " + coincideId);
				if(!coincideId) {
					if(esRecetaConPreparaciones) {
						for(Receta r : preparacionesEnReceta) {
							for(Ingrediente i : r.getIngredientes()) {
								if (i.getProductoId() == pEnReceta.getId()) {
									i.setProductoId(pBD.getId());
									break;
								}
							}
						}
					}
					else {
						for(Ingrediente i : receta.getIngredientes()) {
							if (i.getProductoId() == pEnReceta.getId()) {
								i.setProductoId(pBD.getId());
								break;
							}
						}
					}					
				}
			}
			else {					
				productoService.save(pEnReceta);
				String mensajeFlashProducto = "Se ha importado el producto " + pEnReceta.getNombre() + ", ya que es necesario para esta receta.";
				flash.addFlashAttribute("info", mensajeFlashProducto);
			}				
		}
	}
	
	public void comprobandoArtefactosEnReceta(List<Artefacto> artefactosEnReceta, Receta receta, RedirectAttributes flash, boolean esRecetaConPreparaciones, List<Receta> preparacionesEnReceta) {
		//VERIFICANDO SI EXISTEN LOS ARTEFACTOS EN LA BD
		for(Artefacto a : artefactosEnReceta) {
			String nombreAR = a.getNombre();
			boolean exiteARenBD = artefactoService.existsByNombre(nombreAR); 
			System.out.println("¿¿¿¿¿¿exitePRenBD???????: " + exiteARenBD);
			if(exiteARenBD) {
				Artefacto aBD = artefactoService.findByNombre(nombreAR);
				boolean coincideId = aBD.getId() == a.getId();
				System.out.println("¿¿¿¿¿¿coincideId???????: " + coincideId);
				if(!coincideId) {
					if(esRecetaConPreparaciones) {
						for(Receta r : preparacionesEnReceta) {
							for(ArtefactoEnUso i : r.getArtefactosUtilizados()) {
								if (i.getArtefactoId() == a.getId()) {
									i.setArtefactoId(aBD.getId());
									break;
								}
							}
						}
					}
					else {
						for(ArtefactoEnUso i : receta.getArtefactosUtilizados()) {
							if (i.getArtefactoId() == a.getId()) {
								i.setArtefactoId(aBD.getId());
								break;
							}
						}
					}
				}
			}
			else {					
				artefactoService.save(a);
				String mensajeFlashProducto = "Se ha importado el artefacto " + a.getNombre() + ", ya que es necesario para esta receta.";
				flash.addFlashAttribute("info", mensajeFlashProducto);
			}				
		}
	}
	
	public void limpiandoIdsParaGuardadoEnBD(Receta receta, boolean esRecetaConPreparacion, List<Receta> preparacionesEnReceta) {
		receta.setId((long) 0);
		if(esRecetaConPreparacion) {
			for (Receta r : preparacionesEnReceta) {
				r.setId((long) 0);
				for(Ingrediente i : r.getIngredientes()) {
					i.setId((long) 0);
				}
				for(ArtefactoEnUso i : r.getArtefactosUtilizados()) {
					i.setId((long) 0);
				}
				for(Instruccion i : r.getInstrucciones()) {
					i.setId((long) 0);
				}
			}
		}
		else {
			for(Ingrediente i : receta.getIngredientes()) {
				i.setId((long) 0);
			}
			for(ArtefactoEnUso i : receta.getArtefactosUtilizados()) {
				i.setId((long) 0);
			}
			for(Instruccion i : receta.getInstrucciones()) {
				i.setId((long) 0);
			}
		}
		
	}
	
	
	
}	