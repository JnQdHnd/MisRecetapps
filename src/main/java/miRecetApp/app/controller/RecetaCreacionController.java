package miRecetApp.app.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import miRecetApp.app.model.entity.Artefacto;
import miRecetApp.app.model.entity.ArtefactoEnUso;
import miRecetApp.app.model.entity.Ingrediente;
import miRecetApp.app.model.entity.Instruccion;
import miRecetApp.app.model.entity.ManoDeObra;
import miRecetApp.app.model.entity.Producto;
import miRecetApp.app.model.entity.Receta;
import miRecetApp.app.service.IArtefactoService;
import miRecetApp.app.service.IManoDeObraService;
import miRecetApp.app.service.IProductoService;
import miRecetApp.app.service.IRecetaService;
import miRecetApp.app.service.IUploadFileService;
import miRecetApp.app.service.implementation.IdentificaDevice;

@Controller
@SessionAttributes("recetaCreacion")
public class RecetaCreacionController {

	// ATRIBUTOS-----------------------------------------------------------------------------------------
	
	@Autowired
	private IdentificaDevice identificaDevices;
	@Autowired
	private IRecetaService recetaService;	
	@Autowired
	private IUploadFileService uploadFileService;			
	@Autowired
	private IProductoService productoService;	
	@Autowired
	private IArtefactoService artefactoService;	
	@Autowired
	private IManoDeObraService manoDeObraService;
	@Autowired
	private MessageSource messageSource;
	private final Logger log = LoggerFactory.getLogger(getClass());		
	String deviceType = "browser";
	List<Producto> productos;
	List<Artefacto> artefactos;
	List<ManoDeObra> trabajadores;
	
	Receta recetaTemp;
	boolean esRecetaConPreparacionesTemp;
	// METODOS-----------------------------------------------------------------------------------------
	
	/**
	 * Método que gestiona la información que se pasa al formulario.
	 * 
	 * @param codigo
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = {"/receta/form"})
	public String crear(Model model, 
						Locale locale,	
						Device device, 
						Authentication authentication) { 
						
		
		deviceType = identificaDevices.getDevice(device);
		
		System.out.println("ABRE CREAR");	
		Receta receta = new Receta();	
		receta.setAutor(authentication.getName());
		List<Ingrediente> ingredientes = new ArrayList<>();
		Ingrediente ingrediente = new Ingrediente();
		ingredientes.add(ingrediente);
		List <ArtefactoEnUso> artefactosEnUso = new ArrayList<>();
		ArtefactoEnUso artefacto = new ArtefactoEnUso();
		artefactosEnUso.add(artefacto);
		List <Instruccion> instrucciones = new ArrayList<>();
		Instruccion instruccion = new Instruccion();
		instrucciones.add(instruccion);

		receta.setArtefactosUtilizados(artefactosEnUso);
		receta.setIngredientes(ingredientes);
		receta.setInstrucciones(instrucciones);
		
		System.out.println("INGREDIENTES: " + receta.getIngredientes().size());
		System.out.println("ARTEFACTOS: " + receta.getArtefactosUtilizados().size());
		System.out.println("INSTRUCCIONES: " + receta.getInstrucciones().size());
		
		productos = productoService.findAll(Sort.by("nombre").ascending());
		artefactos = artefactoService.findAll(Sort.by("nombre").ascending());
		trabajadores = manoDeObraService.findAll(Sort.by("nombre").ascending());	
		
		model.addAttribute("titulo", messageSource.getMessage("text.receta.crear.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.receta.crear.btn", null, locale));
		model.addAttribute("receta", receta);
		model.addAttribute("productos", productos);
		model.addAttribute("artefactos", artefactos);
		model.addAttribute("trabajadores", trabajadores);		
		model.addAttribute("deviceType", deviceType);
		model.addAttribute("esNuevo", true);
		model.addAttribute("esRecetaConPreparaciones", false);
		return "receta/form";
	}
	
	/**
	 * Método que gestiona la información que se pasa al formulario al editar un
	 * item.
	 * 
	 * @param id
	 * @param flash
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = {"/receta/form/{id}"})
	public String editar(@PathVariable(value = "id") Long id, 
						 RedirectAttributes flash, 
						 Model model, 
						 Locale locale) {
		
		System.out.println("EDITANDO RECETA");
		
		productos = productoService.findAll(Sort.by("nombre").ascending());
		artefactos = artefactoService.findAll(Sort.by("nombre").ascending());
		trabajadores = manoDeObraService.findAll(Sort.by("nombre").ascending());
		
		Receta receta = null;
		if (id > 0) {
			System.out.println("ID MAYOR QUE 0 EN EDICION");
			receta = recetaService.findOne(id);
			if (receta == null) {
				flash.addFlashAttribute("error", "El ID del Receta no existe!");
				return "redirect:/receta/listar";
			}			
			if(receta.getIngredientes().isEmpty()) {
				List<Ingrediente> ingredientes = new ArrayList<>();
				Ingrediente ingrediente = new Ingrediente();
				ingredientes.add(ingrediente);
				receta.setIngredientes(ingredientes);
			}
			if(receta.getArtefactosUtilizados().isEmpty()) {
				System.out.println("ARTEFACTOS VACIO EN EDICION - CREANDO ARTEFACTO POR DEFECTO");
				List<ArtefactoEnUso> artefactosEnUso = new ArrayList<>();
				ArtefactoEnUso artefacto = new ArtefactoEnUso();
				artefactosEnUso.add(artefacto);
				receta.setArtefactosUtilizados(artefactosEnUso);
			}
			if(receta.getInstrucciones().isEmpty()) {
				System.out.println("INSTRUCCIONES VACIO EN EDICION - CREANDO INSTRUCCION POR DEFECTO");
				List<Instruccion> instrucciones = new ArrayList<>();
				Instruccion instruccion = new Instruccion();
				instrucciones.add(instruccion);
				receta.setInstrucciones(instrucciones);
			}
		} else {
			flash.addFlashAttribute("error", "El ID del Receta no puede ser 0!");
			return "redirect:/receta/listar";
		}
		
		
		System.out.println("///////////////////EDITANDO RECETA: ID " + receta.getId());			
		
		
		model.addAttribute("titulo", messageSource.getMessage("text.receta.editar.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.receta.editar.btn", null, locale));
		model.addAttribute("productos", productos);
		model.addAttribute("artefactos", artefactos);
		model.addAttribute("trabajadores", trabajadores);
		model.addAttribute("receta", receta);
		model.addAttribute("deviceType", deviceType);
		model.addAttribute("esNuevo", false);
		model.addAttribute("esRecetaConPreparaciones", false);

		System.out.println("LLAMANDO A FORM");
		
		return "receta/form";
	}
	
	/**
	 * Método que guarda y modifica datos en la BD.
	 * 
	 * @param receta
	 * @param result
	 * @param model
	 * @param flash
	 * @param request
	 * @return String
	 */
	@PostMapping(value = {"/receta/form"})
	public String guardar(@Valid Receta receta, 
						  BindingResult result, 
						  Model model, 
						  RedirectAttributes flash,
						  HttpServletRequest request,
						  MultipartHttpServletRequest multipartRequest,
						  Locale locale,
						  Authentication authentication) {	
		
		System.out.println("************GUARDANDO RECETA***********");
		
		if (result.hasErrors() && receta.getId() == null) {
			if (result.hasErrors()) {
				System.out.println("HAY ERRORES RECETA: " + result.getFieldError().getDefaultMessage());
				System.out.println("HAY ERRORES RECETA: " + result.getFieldError().getCode());
				System.out.println("HAY ERRORES RECETA: " + result.getObjectName());
			}
			model.addAttribute("titulo", messageSource.getMessage("text.receta.crear.titulo", null, locale));
			model.addAttribute("btnText", messageSource.getMessage("text.receta.crear.btn", null, locale));
			model.addAttribute("receta", receta);
			model.addAttribute("productos", productos);
			model.addAttribute("artefactos", artefactos);
			model.addAttribute("deviceType", deviceType);
			return "receta/form";

		} else if (result.hasErrors() && receta.getId() != null && receta.getId() > 0) {
			System.out.println("HAY ERRORES RECETA ID" + result.getFieldError().getDefaultMessage());
			System.out.println("HAY ERRORES RECETA ID" + result.getFieldError().getCode());

			model.addAttribute("titulo", messageSource.getMessage("text.receta.editar.titulo", null, locale));
			model.addAttribute("btnText", messageSource.getMessage("text.receta.editar.btn", null, locale));
			model.addAttribute("receta", receta);
			model.addAttribute("productos", productos);
			model.addAttribute("artefactos", artefactos);
			model.addAttribute("deviceType", deviceType);
			return "receta/form";
		}
		
		boolean hayArtefactosUtilizados = receta.getArtefactosUtilizados() != null;
		boolean hayInstrucciones = receta.getInstrucciones() != null;
		System.out.println("hayArtefactosUtilizados: " + hayArtefactosUtilizados);
		System.out.println("hayInstrucciones: " + hayInstrucciones);
		if(hayArtefactosUtilizados) {
			if(receta.getArtefactosUtilizados().get(0).getArtefactoId() == null) {
				receta.getArtefactosUtilizados().clear();
			}
			else {
				int cantidadDeArtefactos = Integer.parseInt(request.getParameter("cantidadDeArtefactos"));
				int artefactosEnReceta = receta.getArtefactosUtilizados().size();
				if(cantidadDeArtefactos < artefactosEnReceta) {
					System.out.println("---------Hay mas Artefactos en la Receta que los enviados en el Request----------");
					System.out.println("Cantidad de Artefactos del Request: " + cantidadDeArtefactos);
					System.out.println("Artefactos en Receta: " + artefactosEnReceta);
					System.out.println("Se procede a eliminar los items sobrantes del listado en la Receta");
					cantidadDeArtefactos--;
					artefactosEnReceta--;
					for(int i = artefactosEnReceta; i > cantidadDeArtefactos; i--) {
						receta.getArtefactosUtilizados().remove(i);
					}
				}
			}
		}
		if(hayInstrucciones) {
			if(receta.getInstrucciones().get(0).getInstruccion() == null) {
				receta.getInstrucciones().clear();
			}
			else {
				int cantidadDeInstrucciones = Integer.parseInt(request.getParameter("cantidadDeInstrucciones"));		
				int instruccionesEnReceta = receta.getInstrucciones().size();
				System.out.println("Instrucciones en Receta: " + instruccionesEnReceta);
				System.out.println("Cantidad de Instrucciones del Request: " + cantidadDeInstrucciones);
				if(cantidadDeInstrucciones < instruccionesEnReceta) {
					System.out.println("---------Hay mas Instrucciones en la Receta que los enviados en el Request----------");
					System.out.println("Cantidad de Instrucciones del Request: " + cantidadDeInstrucciones);
					System.out.println("Instrucciones en Receta: " + instruccionesEnReceta);
					System.out.println("Se procede a eliminar los items sobrantes del listado en la Receta");
					cantidadDeInstrucciones--;
					instruccionesEnReceta--;
					for(int i = instruccionesEnReceta; i > cantidadDeInstrucciones; i--) {
						receta.getInstrucciones().remove(i);
					}
				}					
				for(int i = 0; i < instruccionesEnReceta; i++) {
					System.out.println("CICLO DE GUARDADO DE FOTOS");
					Instruccion instruccion = receta.getInstrucciones().get(i); 
					i++;
					String contieneFoto = request.getParameter("contieneFoto" + i);
					if(contieneFoto.equals("false") && instruccion.getFoto() != null && instruccion.getFoto().length() > 0) {
						uploadFileService.delete(instruccion.getFoto());
						instruccion.setFoto(null);
					}
					MultipartFile foto = multipartRequest.getFile("pasoFoto" + i);			
					System.out.println("Se ha recuperado la foto del request multipart: " + !foto.isEmpty());
					guardaFoto(foto, instruccion);
				}				
			}
		}		

		if(receta.getAutor() == null) {
			receta.setAutor(authentication.getName());
		}			
		
		int cantidadDeIngredientes = Integer.parseInt(request.getParameter("cantidadDeIngredientes"));		
		int ingredientesEnReceta = receta.getIngredientes().size();		
		if(cantidadDeIngredientes < ingredientesEnReceta) {
			System.out.println("---------Hay mas Ingredientes en la Receta que los enviados en el Request----------");
			System.out.println("Cantidad de Ingredientes del Request : " + cantidadDeIngredientes);
			System.out.println("Ingredientes en Receta: " + ingredientesEnReceta);
			System.out.println("Se procede a eliminar los items sobrantes del listado en la Receta");
			cantidadDeIngredientes--;
			ingredientesEnReceta--;
			for(int i = ingredientesEnReceta; i > cantidadDeIngredientes; i--) {
				receta.getIngredientes().remove(i);
			}
		}
		
		String mensajeFlash = (receta.getId() != null) ? "Receta editada con éxito!" : "Receta creada con éxito!";
		flash.addFlashAttribute("success", mensajeFlash);	
		
		recetaService.save(receta);	
		
		return "redirect:/receta/verReceta/" + receta.getId();
	}
	
	/**
	 * Método que elimina items de la BD.
	 * 
	 * @param id
	 * @param flash
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/receta/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Model model) {
		if (id > 0) {
			recetaService.delete(id);
			flash.addFlashAttribute("success", "Receta eliminado con éxito!");
		}
		return "redirect:/receta/listar";
	}
	
	/**
	 * Método que elimina ingredientes durante la edición de una receta.
	 * 
	 * @param idIngrediente
	 * @param receta
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = {"/receta/eliminaIngrediente/{index}"})
	public String eliminaIngrediente(@PathVariable(value = "index") int index, Model model, HttpServletRequest request, Locale locale) {
		
		Receta receta = obtieneRecetaProvisoria(request);
		receta.getIngredientes().remove(index);
		if(receta.getIngredientes().isEmpty()) {
			Ingrediente i = new Ingrediente();
			receta.getIngredientes().add(i);
		}
		
		model.addAttribute("titulo", messageSource.getMessage("text.receta.editar.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.receta.editar.btn", null, locale));
		model.addAttribute("productos", productos);
		model.addAttribute("artefactos", artefactos);
		model.addAttribute("trabajadores", trabajadores);
		model.addAttribute("receta", receta);
		model.addAttribute("deviceType", deviceType);
		
		return "receta/form";
	}
	
	/**
	 * Método que elimina instrucciones durante la edición de una receta.
	 * 
	 * @param idInstruccion
	 * @param receta
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = {"/receta/eliminaInstruccion/{index}"})
	public String eliminaInstruccion(@PathVariable(value = "index") int index, Receta receta, Model model, Locale locale) {

		receta.getInstrucciones().remove(index);
		if(receta.getInstrucciones().isEmpty()) {
			Instruccion i = new Instruccion();
			receta.getInstrucciones().add(i);
		}	
		
		model.addAttribute("titulo", messageSource.getMessage("text.receta.editar.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.receta.editar.btn", null, locale));
		model.addAttribute("productos", productos);
		model.addAttribute("artefactos", artefactos);
		model.addAttribute("trabajadores", trabajadores);
		model.addAttribute("receta", receta);
		model.addAttribute("deviceType", deviceType);
		
		return "receta/form";
	}
	
	/**
	 * Método que elimina artefacto en uso durante la edición de una receta.
	 * 
	 * @param idArtefactoEnUso
	 * @param receta
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = {"/receta/eliminaArtefactoEnUso/{index}"})
	public String eliminaArtefactoEnUso(@PathVariable(value = "index") int index, Receta receta, Model model, Locale locale) {
		
		receta.getArtefactosUtilizados().remove(index);
		if(receta.getArtefactosUtilizados().isEmpty()) {
			ArtefactoEnUso i = new ArtefactoEnUso();
			receta.getArtefactosUtilizados().add(i);
		}			
		model.addAttribute("titulo", messageSource.getMessage("text.receta.editar.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.receta.editar.btn", null, locale));
		model.addAttribute("productos", productos);
		model.addAttribute("artefactos", artefactos);
		model.addAttribute("trabajadores", trabajadores);
		model.addAttribute("receta", receta);
		model.addAttribute("deviceType", deviceType);
		
		return "receta/form";
	}
	
	@PostMapping(value = {"/receta/itemIncorporado/{item}"})
	public String guardarItemIncorporado(@PathVariable(value = "item") String item,
										Producto producto, 
										Artefacto artefacto, 							  
										Model model, 
										RedirectAttributes flash,
										HttpServletRequest request, 
										Locale locale) {
		
		boolean esProducto = item.equalsIgnoreCase("producto");
		boolean esArtefacto = item.equalsIgnoreCase("artefacto");
		
		System.out.println("HAYPRODUCTO: " + esProducto);
		System.out.println("HAYARTEFACTO: " + esArtefacto);
		
		String mensajeFlash = esProducto ? guardaProducto(producto) : guardaArtefacto(artefacto);
		flash.addFlashAttribute("success", mensajeFlash);
		
		recetaTemp = obtieneRecetaProvisoria(request);
		esRecetaConPreparacionesTemp = Boolean.parseBoolean(request.getParameter("esRecetaConPreparaciones"));
		
		if(esProducto) {
			Ingrediente ingrediente = new Ingrediente();
			ingrediente.setProductoId(producto.getId());
			recetaTemp.getIngredientes().add(ingrediente);
		}
		else {
			ArtefactoEnUso a = new ArtefactoEnUso();
			a.setArtefactoId(artefacto.getId());
			recetaTemp.getArtefactosUtilizados().add(a);
		}		
		if(recetaTemp.getIngredientes().isEmpty()) {
			Ingrediente ingrediente = new Ingrediente();
			recetaTemp.getIngredientes().add(ingrediente);
		}
		if(recetaTemp.getArtefactosUtilizados().isEmpty()) {
			ArtefactoEnUso artefactoEnUso = new ArtefactoEnUso();
			recetaTemp.getArtefactosUtilizados().add(artefactoEnUso);
		}
		
		return "redirect:/receta/itemIncorporado";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = {"/receta/itemIncorporado"})
	public String crearConItemIncorporado(Model model, 
							   			  Locale locale,
							   			  Device device) {
		
		System.out.println("esRecetaConPreparaciones: " + esRecetaConPreparacionesTemp);
		System.out.println("Cantidad de Ingredeintes en Receta Recibida: " + recetaTemp.getIngredientes().size());
		
		productos = productoService.findAll(Sort.by("nombre").ascending());
		artefactos = artefactoService.findAll(Sort.by("nombre").ascending());
		trabajadores = manoDeObraService.findAll(Sort.by("nombre").ascending());

		model.addAttribute("titulo", messageSource.getMessage("text.receta.crear.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.receta.crear.btn", null, locale));
		model.addAttribute("receta", recetaTemp);
		model.addAttribute("esRecetaConPreparaciones", esRecetaConPreparacionesTemp);
		model.addAttribute("productos", productos);
		model.addAttribute("artefactos", artefactos);
		model.addAttribute("trabajadores", trabajadores);
		model.addAttribute("seHaCreadoItem", true);
		model.addAttribute("deviceType", deviceType);

		return "receta/form";
	}
	
	public Receta obtieneRecetaProvisoria(HttpServletRequest request) {
		System.out.println("***************RECETA PROVISORIA************");
		Receta receta = new Receta();
		String recetaId = request.getParameter("idModal");
		System.out.println("recetaId en provisoria: " + recetaId);
		receta.setId(recetaId);
		String autor = request.getParameter("autorModal");
		System.out.println("autor en provisoria: " + autor);
		receta.setAutor(autor);
		String nombre = request.getParameter("nombreModal");
		System.out.println("nombre en provisoria: " + nombre);
		receta.setNombre(nombre);
		String porciones = request.getParameter("porcionesModal");
		receta.setPorciones(porciones);	
		System.out.println("porciones en provisoria: " + porciones);
		
		int cantidadDeIngredientes = Integer.parseInt(request.getParameter("cantidadDeIngredientesModal"));
		System.out.println("cantidadDeIngredientes provisoria: " + cantidadDeIngredientes);
		List<Ingrediente> ingredientes = new ArrayList<>();
		for(int i = 0; i<cantidadDeIngredientes; i++) {
			System.out.println("Ingrediente" + i + ":");
			String productoId= request.getParameter("ingredientes["+ i +"].productoIdModal"); 
			String productoCantidad= request.getParameter("ingredientes["+ i +"].cantidadModal");
			String productoUnidad= request.getParameter("ingredientes["+ i +"].unidadDeMedidaModal");
			System.out.println("productoId: " + productoId);
			System.out.println("productoCantidad: " + productoCantidad);
			System.out.println("productoUnidad: " + productoUnidad);
			if(productoId != null || !productoCantidad.isBlank()) {
				Ingrediente ingrediente = new Ingrediente();
				ingrediente.setProductoId(productoId);
				ingrediente.setCantidad(productoCantidad);
				ingrediente.setUnidadDeMedida(productoUnidad);
				ingredientes.add(ingrediente);
			}
		}
		receta.setIngredientes(ingredientes);		
		int cantidadDeArtefactos = Integer.parseInt(request.getParameter("cantidadDeArtefactosModal"));
		System.out.println("cantidadDeArtefactos provisoria: " + cantidadDeArtefactos);
		List<ArtefactoEnUso> artefactos = new ArrayList<>();
		for(int i = 0; i<cantidadDeArtefactos; i++) {
			System.out.println("Artefacto" + i + ":");
			String artefactoId= request.getParameter("artefactosUtilizados["+ i +"].artefactoIdModal"); 
			String minutosDeUso= request.getParameter("artefactosUtilizados["+ i +"].minutosDeUsoModal");
			String esHorno = request.getParameter("artefactosUtilizados["+ i +"].esHornoModal");
			String intensidad= request.getParameter("artefactosUtilizados["+ i +"].intensidadDeUsoModal");
			String temperatura= request.getParameter("artefactosUtilizados["+ i +"].temperaturaModal");
			String unidadDeTemperatura= request.getParameter("artefactosUtilizados["+ i +"].unidadDeTemperaturaModal");
			System.out.println("artefactoId: " + artefactoId);
			System.out.println("minutosDeUso: " + minutosDeUso);
			System.out.println("esHorno: " + esHorno);
			System.out.println("intensidad: " + intensidad);
			System.out.println("temperatura: " + temperatura);
			System.out.println("unidadDeTemperatura: " + unidadDeTemperatura);
			boolean artefactoIdNotNull = artefactoId != null;
			boolean minutosDeUsoNotNullandNotBlank = minutosDeUso != null && !minutosDeUso.isBlank();
			System.out.println("artefactoIdNotNull: " + artefactoIdNotNull);
			System.out.println("minutosDeUsoNotNullandNotBlank: " + minutosDeUsoNotNullandNotBlank);
			
			if(artefactoIdNotNull || minutosDeUsoNotNullandNotBlank) { 
				ArtefactoEnUso artefactoEnUso = new ArtefactoEnUso();
				artefactoEnUso.setArtefactoId(artefactoId);
				artefactoEnUso.setMinutosDeUso(minutosDeUso);
				artefactoEnUso.setEsHorno(esHorno);
				artefactoEnUso.setIntensidadDeUso(intensidad);
				artefactoEnUso.setTemperatura(temperatura);
				artefactoEnUso.setUnidadDeTemperatura(unidadDeTemperatura);
				artefactos.add(artefactoEnUso);
			}
		}
		receta.setArtefactosUtilizados(artefactos);	
		
		int cantidadDeInstrucciones = Integer.parseInt(request.getParameter("cantidadDeInstruccionesModal"));
		System.out.println("cantidadDeInstrucciones provisoria: " + cantidadDeInstrucciones);
		List<Instruccion> instrucciones = new ArrayList<>();
		for(int i = 0; i<cantidadDeInstrucciones; i++) {
			System.out.println("Instruccion" + i + ":");
			String paso = request.getParameter("instrucciones["+ i +"].instruccionModal"); 
			Instruccion instruccion = new Instruccion();
			instruccion.setOrden((long) i);
			instruccion.setInstruccion(paso);
			instrucciones.add(instruccion);
		}
		receta.setInstrucciones(instrucciones);
		return receta;
	} 
	
	public String guardaProducto(Producto producto) {
		System.out.println("GUARDANDO PRODUCTO: " + producto.getNombre());
		String mensaje = "";
		if(producto.getNombre() != null && !producto.getNombre().isBlank()) {
			double cantidadSinDesperdicio = producto.getCantidad() - (producto.getDesperdicio() * producto.getCantidad() / 100);
			producto.setPrecioSinDesperdicio(producto.getCantidad() * producto.getPrecio() / cantidadSinDesperdicio);
			
			productoService.save(producto);
			mensaje = "¡Producto creado con éxito! Continuemos...";
		} 
		return mensaje;
	}
	
	public String guardaArtefacto(Artefacto artefacto) {
		System.out.println("GUARDANDO ARTEFACTO: " + artefacto.getNombre());
		String mensaje = "";
		if(artefacto.getNombre() != null && !artefacto.getNombre().isBlank()) {			
			artefactoService.save(artefacto);
			mensaje = "¡Artefacto creado con éxito! Continuemos...";
		} 
		return mensaje;
	}
	
	public void guardaFoto(MultipartFile foto, Instruccion instruccion) {
		if(!foto.isEmpty()) {
			System.out.println("INSTRUCCION " + instruccion.getOrden() + ": GUARDANDO FOTO.");
			if(instruccion.getFoto() !=null
			   && instruccion.getFoto().length() > 0) {				
				Path rootPath = Paths.get("fotos").resolve(instruccion.getFoto()).toAbsolutePath();
				log.info("rootPath FOTO A REEMPLAZAR: " + rootPath);
				File archivo = rootPath.toFile();				
				if(archivo.exists() && archivo.canRead()) {
					archivo.delete();
					System.out.println("Imagen ELIMINADA");
				}
			}			
			String uniqueNameFile = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();			
			Path rootPath = Paths.get("fotos").resolve(uniqueNameFile);	
			Path rootAbsolutePath = rootPath.toAbsolutePath();			
			log.info("rootPath: " + rootPath);
			log.info("rootAbsolutPath: " + rootAbsolutePath);			
			try {				
				Files.copy(foto.getInputStream(), rootAbsolutePath);
				instruccion.setFoto(uniqueNameFile);
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}
		else {
			System.out.println("INSTRUCCION " + instruccion.getOrden() + ": NO HAY FOTO PARA GUARDAR.");
		}		
	}
}
