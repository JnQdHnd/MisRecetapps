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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import miRecetApp.app.model.entity.Artefacto;
import miRecetApp.app.model.entity.ArtefactoEnUso;
import miRecetApp.app.model.entity.Ingrediente;
import miRecetApp.app.model.entity.Instruccion;
import miRecetApp.app.model.entity.ManoDeObra;
import miRecetApp.app.model.entity.Preparacion;
import miRecetApp.app.model.entity.Producto;
import miRecetApp.app.model.entity.Receta;
import miRecetApp.app.service.IArtefactoService;
import miRecetApp.app.service.IManoDeObraService;
import miRecetApp.app.service.IPreparacionService;
import miRecetApp.app.service.IProductoService;
import miRecetApp.app.service.IRecetaService;
import miRecetApp.app.service.IUploadFileService;
import miRecetApp.app.service.implementation.IdentificaDevice;


/**
 * @author Julián Quenard *
 * 01-09-2021
 */

@Controller
@SessionAttributes("recetaPreparacionCreacion")
public class RecetaPreparacionCreacionController {

	// ATRIBUTOS-----------------------------------------------------------------------------------------
	
	@Autowired
	private IdentificaDevice identificaDevices;
	@Autowired
	private IRecetaService recetaService;	
	@Autowired
	private IPreparacionService preparacionService;	
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
	List<Receta> recetas;
	boolean esNuevaReceta;
	Receta recetaProvisoria;
	int preparacionAGestionar = 0;
	List<Receta> preparacionesProvisorias = new ArrayList<>();
	long contadorDeRecetasIncorporadas = 0;

	// METODOS-----------------------------------------------------------------------------------------

	/**
	 * Método que gestiona la información que se pasa al formulario.
	 * 
	 * @param model
	 * @param locale
	 * @param device
	 * @param authentication
	 * @return String
	 */
	@RequestMapping(value = {"/receta/inicio"})
	public String seleccionaTipoDeReceta(Model model, Locale locale, Device device) {
		
		productos = productoService.findAll(Sort.by("nombre").ascending());
		artefactos = artefactoService.findAll(Sort.by("nombre").ascending());
		trabajadores = manoDeObraService.findAll(Sort.by("nombre").ascending());	
		
		deviceType = identificaDevices.getDevice(device);
		esNuevaReceta = true;

		model.addAttribute("titulo", messageSource.getMessage("text.receta.configurar.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.receta.configurar.btn", null, locale));
		model.addAttribute("deviceType", deviceType);

		return "receta/inicio";
	}
	
	/**
	 * Método que gestiona la información que se pasa al formulario.
	 * 
	 * @param codigo
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = {"/receta/formPreparaciones"})
	public String crearConPreparaciones(Model model, 
									   Locale locale,	
									   Device device, 
									   Authentication authentication) {
		
		deviceType = identificaDevices.getDevice(device);		
		Receta receta = null;		
		System.out.println("COMENZAMOS CON LA RECETA CON PREPARACIONES");
		receta = new Receta();
		receta.setAutor(authentication.getName());
		List<Preparacion> preparaciones = new ArrayList<>();
		Preparacion preparacion = new Preparacion();
		preparaciones.add(preparacion);		
		receta.setPreparaciones(preparaciones);	
		recetaProvisoria = receta;
		preparacionAGestionar = 0;
		contadorDeRecetasIncorporadas = 0;
		preparacionesProvisorias = new ArrayList<>();
		
		recetas = recetaService.findAll(Sort.by("nombre").ascending());

		model.addAttribute("titulo", messageSource.getMessage("text.receta.configurar.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.receta.configurar.btn", null, locale));
		model.addAttribute("recetaConPreparaciones", receta);
		model.addAttribute("recetas", recetas);
		model.addAttribute("esNuevo", esNuevaReceta);
		model.addAttribute("deviceType", deviceType);

		return "receta/formPreparaciones";
	}
	
	/**
	 * Método que gestiona la información que se pasa al formulario.
	 * 
	 * @param codigo
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = {"/receta/formPreparacionesContinua"})
	public String seguirConPreparaciones(Model model, 
									   Locale locale,	
									   Device device, 
									   Authentication authentication) {
		
		Receta r = recetaProvisoria;
		System.out.println("SEGUIMOS CON LA RECETA CON PREPARACIONES: " + r.getNombre());
		deviceType = identificaDevices.getDevice(device);	

		model.addAttribute("titulo", messageSource.getMessage("text.receta.configurar.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.receta.configurar.btn", null, locale));
		model.addAttribute("recetaConPreparaciones", recetaProvisoria);
		model.addAttribute("recetas", recetas);
		model.addAttribute("preparacionAGestionar", preparacionAGestionar);
		model.addAttribute("preparacionesProvisorias", preparacionesProvisorias);
		model.addAttribute("esNuevo", esNuevaReceta);
		model.addAttribute("deviceType", deviceType);

		return "receta/formPreparaciones";
	}
	
	/**
	 * Método que gestiona la información que se pasa al formulario.
	 * 
	 * @param codigo
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = {"/receta/preparaciones/{id}"})
	public String editaRecetaConPreparacion(@PathVariable(value = "id", required = false) Long id,
											Model model, 
									   		Locale locale,	
									   		Device device, 
									   		Authentication authentication) {
		
		productos = productoService.findAll(Sort.by("nombre").ascending());
		artefactos = artefactoService.findAll(Sort.by("nombre").ascending());
		trabajadores = manoDeObraService.findAll(Sort.by("nombre").ascending());
		
		deviceType = identificaDevices.getDevice(device);
		esNuevaReceta = false;			

		if (id > 0) {
			recetaProvisoria = recetaService.findOne(id);
			preparacionesProvisorias.clear();
			for(Preparacion p : recetaProvisoria.getPreparaciones()) {
				Receta r = recetaService.findOne(p.getRecetaId());
				preparacionesProvisorias.add(r);
				preparacionAGestionar = preparacionesProvisorias.size();
				System.out.println(r.toString());
			}
		}
		else {
			System.out.println("ERROR AL RECUPERAR RECETA CON PREPARACIONES");
			return "redirect:/receta/listar";
		}			
		
		recetas = recetaService.findAll(Sort.by("nombre").ascending());

		model.addAttribute("titulo", messageSource.getMessage("text.receta.configurar.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.receta.configurar.btn", null, locale));
		model.addAttribute("recetaConPreparaciones", recetaProvisoria);
		model.addAttribute("preparacionAGestionar", preparacionAGestionar);
		model.addAttribute("recetas", recetas);
		model.addAttribute("esNuevo", esNuevaReceta);
		model.addAttribute("preparacionesProvisorias", preparacionesProvisorias);
		model.addAttribute("deviceType", deviceType);

		return "receta/formPreparaciones";
	}
	
	/**
	 * Método que gestiona la información que se pasa al formulario.
	 * 
	 * @param codigo
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = {"/receta/gestionaPreparaciones"})
	public String gestionaPreparacion(Model model, 
									  Locale locale,	
									  Device device, 
									  RedirectAttributes flash,
									  Authentication authentication,
									  Receta recetaConPreparaciones,
									  HttpServletRequest request) {
		
		System.out.println("GESTIONANDO PREPARACIONES");	
		boolean esPreparacionNueva = Boolean.parseBoolean(request.getParameter("esPreparacionNueva"));
		System.out.println("esPreparacionNueva: " + esPreparacionNueva);
		deviceType = identificaDevices.getDevice(device);
		int cantidadDePreparacionesEnReceta = recetaConPreparaciones.getPreparaciones().size();
		int cantidadDePreparacionesEnForm = Integer.parseInt(request.getParameter("cantidadDePreparaciones"));
		System.out.println("cantidadDePreparacionesEnReceta: " + cantidadDePreparacionesEnReceta);
		System.out.println("cantidadDePreparacionesEnForm: " + cantidadDePreparacionesEnForm);
		System.out.println("preparacionAGestionar: " + preparacionAGestionar);
		
		recetaProvisoria = recetaConPreparaciones; //TODO Agregar opcion para editar preparaciones al ser llamado este metodo
		
		if(cantidadDePreparacionesEnReceta < cantidadDePreparacionesEnForm) {
			System.out.println("DIFERENCIA ENTRE RECETA Y FORM: HAY < PREPARACIONES EN RECETRA DE LO DEBIDO");
			for(int i = cantidadDePreparacionesEnReceta; i < cantidadDePreparacionesEnForm; i++) {
				Preparacion p = new Preparacion();
				recetaConPreparaciones.getPreparaciones().add(p);
				System.out.println("PREPARACION AGREGADA A LA RECETA");
				System.out.println("cantidadDePreparacionesEnReceta: " + cantidadDePreparacionesEnReceta);
				System.out.println("cantidadDePreparacionesEnForm: " + cantidadDePreparacionesEnForm);
			}
		}				
		if(preparacionAGestionar < cantidadDePreparacionesEnReceta) {
			String p = request.getParameter("preparaciones["+ preparacionAGestionar +"].recetaId");
			System.out.println("Valor de preparacionSelect: " + p);
			
			boolean isNumeric = p.matches("^-?[0-9]+[0-9]*");
			System.out.println("p isNumeric: " + isNumeric);
			if (isNumeric) {
				Long id = Long.parseLong(p);
				Receta receta = null;
				if(id > 0) {
					System.out.println("ID MAYOR QUE 0 EN EDICION");
					receta = recetaService.findOne(id);
					if (receta == null) {
						flash.addFlashAttribute("error", "El ID del Receta no existe!");
						return "redirect:/receta/listar";
					}
					editarRecetaParaPreparacion(receta);
					
					model.addAttribute("titulo", messageSource.getMessage("text.receta.editar.titulo", null, locale));
					model.addAttribute("btnText", messageSource.getMessage("text.receta.editar.btn", null, locale));
					model.addAttribute("receta", receta);
					model.addAttribute("productos", productos);
					model.addAttribute("artefactos", artefactos);
					model.addAttribute("trabajadores", trabajadores);
					model.addAttribute("deviceType", deviceType);
					model.addAttribute("esRecetaConPreparaciones", true);
					model.addAttribute("esPreparacionNueva", true);

					System.out.println("LLAMANDO A FORM");
					
					return "receta/form";
				}
				else if(id < 0) {					
					int num = preparacionAGestionar + 1;
					System.out.println("preparacionAGestionar: " + preparacionAGestionar);
					System.out.println("num: " + num);
					String nombreDePreparacion = "";
					for(Receta r : recetas) {
						if(id == r.getId()) {
							nombreDePreparacion = r.getNombre();
						}
					}
					System.out.println("recetaPreparacionInput" + num + ": " + nombreDePreparacion);
					
					receta = crearPreparacion(authentication, nombreDePreparacion);
					
					System.out.println("receta tras creacion: " + receta.getNombre());
					
					model.addAttribute("titulo", messageSource.getMessage("text.receta.crear.titulo", null, locale));
					model.addAttribute("btnText", messageSource.getMessage("text.receta.crear.btn", null, locale));
					model.addAttribute("receta", receta);
					model.addAttribute("productos", productos);
					model.addAttribute("artefactos", artefactos);
					model.addAttribute("trabajadores", trabajadores);
					model.addAttribute("deviceType", deviceType);
					model.addAttribute("esRecetaConPreparaciones", true);
					model.addAttribute("esPreparacionNueva", true);
					
					System.out.println("LLAMANDO A FORM");
					
					return "receta/form";
				}
				else {
					System.out.println("VALOR DE ID ES 0 O NULL");
				}
			}						
		}
		else if(preparacionAGestionar == cantidadDePreparacionesEnReceta) {
			System.out.println("GUARDANDO PREPARACIONES EN BD TOTAL: " + preparacionesProvisorias.size());
			recetaConPreparaciones.getPreparaciones().clear();
			for(Receta r: preparacionesProvisorias) {
				System.out.println("GUARDANDO PREPARACION EN BD: " + r.getNombre());
				r.setPreparacionEnRecetaNombre(recetaConPreparaciones.getNombre());
				boolean idEsNull = r.getId() == null;				
				System.out.println("idEsNull: " + idEsNull);
				recetaService.save(r);
				if(!idEsNull) {
					System.out.println("EDITANDO PREPARACION EN RECETA");
					Preparacion p = preparacionService.findByRecetaId(r.getId());
					if(p != null) {
						recetaConPreparaciones.getPreparaciones().add(p);
					}
				}
				else {
					System.out.println("INCORPORANDO PREPARACION EN RECETA");
					Long id = recetaService.findByNombre(r.getNombre()).getId();
					Preparacion p = new Preparacion();
					p.setRecetaId(id);
					preparacionService.save(p);
					System.out.println("PREPARACION ID: " + preparacionService.findByRecetaId(id).getId());
					recetaConPreparaciones.getPreparaciones().add(p);
				}				
			}			
			recetaConPreparaciones.setAutor(authentication.getName());
			recetaService.save(recetaConPreparaciones);
			System.out.println("GUARDANDO RECETA CON PREPARACIONES YA CONFIGURADA");
			String mensajeFlash = "¡Receta con preparación guardada con éxito!";
			flash.addFlashAttribute("success", mensajeFlash);	
			return "redirect:/receta/verRecetaConPreparaciones/" + recetaConPreparaciones.getId();			
		}
		else {
			String mensajeFlash = "ERROR: ALGO SALIO MAL, gestionando una Preparación mayor que la cantidad en la receta.";
			System.out.println(mensajeFlash);
			flash.addFlashAttribute("error", mensajeFlash);
		}
		
		model.addAttribute("titulo", messageSource.getMessage("text.receta.configurar.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.receta.configurar.btn", null, locale));
		model.addAttribute("recetaConPreparaciones", recetaConPreparaciones);
		model.addAttribute("tienePreparaciones", "true");

		return "receta/formPreparaciones";
	}
	
	/**
	 * Método que gestiona la información que se pasa al formulario al editar un
	 * item.
	 * 
	 * @param receta
	 * @param flash
	 * @param recetaConPreparaciones
	 * @param locale
	 * @return receta
	 */
	public void editarRecetaParaPreparacion(Receta receta) {
		
		System.out.println("ABRE EDITAR RECETA PARA PREPARACION");
		
		productos = productoService.findAll(Sort.by("nombre").ascending());
		artefactos = artefactoService.findAll(Sort.by("nombre").ascending());
		trabajadores = manoDeObraService.findAll(Sort.by("nombre").ascending());
					
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
		System.out.println("///////////////////PREPARACION A PARTIR DE RECETA PREVIA: ID " + receta.getId() + " en " + receta.getPreparacionEnRecetaNombre());
		
		if(receta.getPreparacionEnRecetaNombre() != null && !receta.getPreparacionEnRecetaNombre().isEmpty()) {
			String soloNombrePreparacion = receta.simplificaNombreDePreparacion();
			receta.setNombre(soloNombrePreparacion);
		}
		String aIncorporar = " (en " + recetaProvisoria.getNombre() + ")";
		receta.setNombre(receta.getNombre() + aIncorporar);
		receta.setPreparacionEnRecetaNombre(recetaProvisoria.getNombre());
		receta.setPorciones(recetaProvisoria.getPorciones());
	}
	
	/**
	 * Método que gestiona la información que se pasa al formulario al crear un
	 * item.
	 * 
	 * @param authentication
	 * @param nombreDePreparacion
	 * @return receta
	 */
	public Receta crearPreparacion(Authentication authentication, String nombreDePreparacion) {
		
		System.out.println("ABRE CREAR PREPARACION");	
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
		productos = productoService.findAll(Sort.by("nombre").ascending());
		artefactos = artefactoService.findAll(Sort.by("nombre").ascending());
		trabajadores = manoDeObraService.findAll(Sort.by("nombre").ascending());
		String recetaPrincipal = recetaProvisoria.getNombre();
		String aIncorporar = " (en " + recetaPrincipal + ")";
		receta.setNombre(nombreDePreparacion + aIncorporar);
		receta.setPreparacionEnRecetaNombre(recetaPrincipal);
		receta.setPorciones(recetaProvisoria.getPorciones());
		
		return receta;
				
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
	@PostMapping(value = {"/receta/formPreparaciones"})
	public String guardar(@Valid Receta receta, 
						  BindingResult result, 
						  Model model, 
						  RedirectAttributes flash,
						  HttpServletRequest request,
						  MultipartHttpServletRequest multipartRequest,
						  Locale locale,
						  Authentication authentication) {	
		
		System.out.println("************GUARDANDO PREPARACION***********");
		
		if (result.hasErrors() && receta.getId() == null) {
			if (result.hasErrors()) {
				System.out.println("HAY ERRORES RECETA: " + result.getFieldError().getDefaultMessage());
				System.out.println("HAY ERRORES RECETA: " + result.getFieldError().getCode());
				System.out.println("HAY ERRORES RECETA: " + result.getObjectName());			}
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
		
		boolean esPreparacionNueva = Boolean.parseBoolean(request.getParameter("esPreparacionNueva"));
		
		
		boolean hayArtefactosUtilizados = receta.getArtefactosUtilizados() != null;
		boolean hayInstrucciones = receta.getInstrucciones() != null;
		
		System.out.println(receta.toString());
		System.out.println("esPreparacionNueva: " + esPreparacionNueva);
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
		String mensajeFlash;
		if(esPreparacionNueva) {
			System.out.println("GUARDANDO PREPARACION EN LISTADO DE PREPARACIONES PROVISORIO");		
			receta.cleanId();
			preparacionesProvisorias.add(receta);
			mensajeFlash = "Preparación creada con éxito!";
			preparacionAGestionar++;
		}
		else {			
			int index = Integer.parseInt(request.getParameter("indexPreparacion"));
			System.out.println("EDITANDO PREPARACION EN EL INDEX: " + index);
			preparacionesProvisorias.set(index, receta);
			mensajeFlash = "Preparación editada con éxito!";
		}		
		flash.addFlashAttribute("success", mensajeFlash);	

		return "redirect:/receta/formPreparacionesContinua";
	}
	
	/**
	 * Método que elimina items de la BD.
	 * 
	 * @param id
	 * @param flash
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/receta/eliminarConPreparacion/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Model model) {
		if (id > 0) {
			Receta receta = recetaService.findOne(id);
			for(Preparacion p : receta.getPreparaciones()) {
				recetaService.delete(p.getRecetaId());
			}
			recetaService.delete(id);
			flash.addFlashAttribute("success", "Receta y preparaciones eliminadas con éxito!");
		}
		return "redirect:/receta/listar";
	}
	
	/**
	 * Método que elimina un preparacion provisoria durante la edición de una receta.
	 * 
	 * @param index
	 * @param recetaConPreparaciones
	 */
	@RequestMapping(value = { "/receta/eliminaPreparacion/{index}"})
	public String eliminaPreparacion(@PathVariable(value = "index") int index, Receta recetaConPreparaciones) {
		
		System.out.println("ELIMINANDO PREPARACION PROVISORIA EN RECETA: " + recetaConPreparaciones.getNombre());		
		
		recetaProvisoria = recetaConPreparaciones;
		preparacionesProvisorias.remove(index);
		recetaProvisoria.getPreparaciones().remove(index);
		preparacionAGestionar--;
		if(recetaProvisoria.getPreparaciones().size() == 0) {
			Preparacion p = new Preparacion();
			recetaProvisoria.getPreparaciones().add(p);
		}
		
		return "redirect:/receta/formPreparacionesContinua";
	}
	
	/**
	 * Método que incorpora una preparaciona al listado de recetas para elegir.
	 * 
	 * @param index
	 * @param recetaConPreparaciones
	 */
	@RequestMapping(value = { "/receta/agregaRecetaEnListado/{index}"})
	public String agregaPreparacionEnListado(@PathVariable(value = "index") int index, Receta recetaConPreparaciones, HttpServletRequest request) {
		
		System.out.println("AGREGANDO RECETA EN LISTADO: " + recetaConPreparaciones.getNombre());		
		
		contadorDeRecetasIncorporadas--;
		
		int cantidadDePreparaciones = Integer.parseInt(request.getParameter("cantidadDePreparaciones"));
		int cantidadDePreparacionesEnReceta = recetaConPreparaciones.getPreparaciones().size(); 
		
		System.out.println("cantidadDePreparacionesEnForm: " + cantidadDePreparaciones);
		System.out.println("cantidadDePreparacionesEnReceta: " + cantidadDePreparacionesEnReceta);
		
		for(int i = cantidadDePreparacionesEnReceta; i < cantidadDePreparaciones; i++) {
			System.out.println("Hay menos preparaciones en receta que en el formulario. Incorporando al array.");	
			Preparacion p = new Preparacion();
			recetaConPreparaciones.getPreparaciones().add(p);
		}
		
		System.out.println("Valor de INDEX: " + index);
		int idPreparacionEnRecetaPrincipal = index - 1;
		System.out.println("Valor de idPreparacionEnRecetaPrincipal: " + idPreparacionEnRecetaPrincipal);
		System.out.println("RECETA ID PREVIO: " + recetaConPreparaciones.getPreparaciones().get(idPreparacionEnRecetaPrincipal).getRecetaId());
		recetaConPreparaciones.getPreparaciones().get(idPreparacionEnRecetaPrincipal).setRecetaId(contadorDeRecetasIncorporadas);
		System.out.println("RECETA ID POSTERIOR: " + recetaConPreparaciones.getPreparaciones().get(idPreparacionEnRecetaPrincipal).getRecetaId());
		recetaProvisoria = recetaConPreparaciones;
		
		String nombreReceta = request.getParameter("recetaPreparacionInput" + index);
		Long idNegativo = contadorDeRecetasIncorporadas;
		System.out.println("idNegativo: " + idNegativo);

		Receta receta = new Receta();
		receta.setNombre(nombreReceta);
		receta.setId(idNegativo);
		recetas.add(receta);
		
		return "redirect:/receta/formPreparacionesContinua";
	}
	
	/**
	 * Método que incorpora una preparaciona al listado de recetas para elegir.
	 * 
	 * @param index
	 * @param recetaConPreparaciones
	 */
	@RequestMapping(value = { "/receta/editaPreparacion/{index}"})
	public String editaPreparacion(@PathVariable(value = "index") int index, Model model, Locale locale, Receta recetaConPreparaciones) {
		
		Receta receta = preparacionesProvisorias.get(index);
		
		if(receta.getIngredientes() == null || receta.getIngredientes().size() == 0) {
			System.out.println("INGREDIENTES VACIO EN EDICION - CREANDO INGREDIENTES POR DEFECTO");
			List<Ingrediente> ingredientes = new ArrayList<>();
			Ingrediente ingrediente = new Ingrediente();
			ingredientes.add(ingrediente);
			receta.setIngredientes(ingredientes);
		}
		if(receta.getArtefactosUtilizados() == null || receta.getArtefactosUtilizados().size() == 0) {
			System.out.println("ARTEFACTOS VACIO EN EDICION - CREANDO ARTEFACTO POR DEFECTO");
			List<ArtefactoEnUso> artefactosEnUso = new ArrayList<>();
			ArtefactoEnUso artefacto = new ArtefactoEnUso();
			artefactosEnUso.add(artefacto);
			receta.setArtefactosUtilizados(artefactosEnUso);
		}
		if(receta.getInstrucciones() == null || receta.getInstrucciones().size() == 0) {
			System.out.println("INSTRUCCIONES VACIO EN EDICION - CREANDO INSTRUCCION POR DEFECTO");
			List<Instruccion> instrucciones = new ArrayList<>();
			Instruccion instruccion = new Instruccion();
			instrucciones.add(instruccion);
			receta.setInstrucciones(instrucciones);
		}
		
		model.addAttribute("titulo", messageSource.getMessage("text.receta.editar.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.receta.editar.btn", null, locale));
		model.addAttribute("receta", receta);
		model.addAttribute("recetaConPreparaciones", recetaConPreparaciones);
		model.addAttribute("productos", productos);
		model.addAttribute("artefactos", artefactos);
		model.addAttribute("trabajadores", trabajadores);
		model.addAttribute("deviceType", deviceType);
		model.addAttribute("esRecetaConPreparaciones", true);
		model.addAttribute("esPreparacionNueva", false);
		model.addAttribute("indexPreparacion", index);

		System.out.println("LLAMANDO A FORM");
		
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
			else if(cantidadDeIngredientes == 1 && productoId == null && productoCantidad.isBlank()) {
				Ingrediente ingrediente = new Ingrediente();
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
			else if(!artefactoIdNotNull && !minutosDeUsoNotNullandNotBlank) {
				ArtefactoEnUso artefactoEnUso = new ArtefactoEnUso();
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
