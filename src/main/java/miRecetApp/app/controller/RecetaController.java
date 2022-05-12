package miRecetApp.app.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.math3.util.Precision;
import org.apache.derby.tools.sysinfo;
import org.apache.poi.util.SystemOutLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
import miRecetApp.app.model.entity.UnidadDeMedida;
import miRecetApp.app.model.entity.Usuario;
import miRecetApp.app.service.IArtefactoEnUsoService;
import miRecetApp.app.service.IArtefactoService;
import miRecetApp.app.service.IGastoDivisibleService;
import miRecetApp.app.service.IGastoIndivisibleService;
import miRecetApp.app.service.IIngredienteService;
import miRecetApp.app.service.IInstruccionService;
import miRecetApp.app.service.IManoDeObraService;
import miRecetApp.app.service.IProductoService;
import miRecetApp.app.service.IRecetaService;
import miRecetApp.app.service.IUploadFileService;
import miRecetApp.app.service.implementation.IdentificaDevice;
import miRecetApp.app.service.implementation.UsuarioService;
import miRecetApp.app.util.paginator.PageRender;

@Controller
@SessionAttributes("receta")
public class RecetaController {

	// ATRIBUTOS

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
	private UsuarioService usuarioService;

	@Autowired
	private MessageSource messageSource;

	private final Logger log = LoggerFactory.getLogger(getClass());
			
	@Autowired
	private IdentificaDevice identificaDevices;
			
	String deviceType = "browser";
	List<Producto> productos;
	List<Artefacto> artefactos;
	List<ManoDeObra> trabajadores;

	// METODOS
			
	/**
	 * Método que gestiona la información que se envía a la vista del listado.
	 * 
	 * @param page
	 * @param model
	 * @param authentication
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = { "/receta/listar", "/browser/receta/listar", "/mobile/receta/listar" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, 
						 Model model,
						 Authentication authentication, 
						 HttpServletRequest request, 
						 Locale locale, 
						 Device device) {
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request,"ROLE_");
		
		if (authentication != null) {
			log.info("Hola, tu username es: ".concat(authentication.getName()));
		}

		if (securityContext.isUserInRole("ADMIN")) {
			log.info("ACCESO CONCEDIDO: Hola, " + authentication.getName() + ", tienes acceso.");
		} else {
			log.info("ACCESO DENEGADO: Hola, NO tienes acceso.");
		}

		deviceType = identificaDevices.getDevice(device);		
		
		model.addAttribute("titulo", messageSource.getMessage("text.receta.listar.titulo", null, locale));		
		model.addAttribute("tituloVisibles", messageSource.getMessage("text.receta.listar.titulo.visibles", null, locale));		
		model.addAttribute("tituloMias", messageSource.getMessage("text.receta.listar.titulo.mias", null, locale));		
		model.addAttribute("tituloFavoritas", messageSource.getMessage("text.receta.listar.titulo.favoritas", null, locale));
		model.addAttribute("deviceType", deviceType);
		
		return "receta/listar";
	}
	
	/**
	 * Método que gestiona la información que se envía a la vista del listado.
	 * 
	 * @param page
	 * @param model
	 * @param authentication
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = { "/receta/listarVisibles", "/browser/receta/listarVisibles", "/mobile/receta/listarVisibles" }, method = RequestMethod.GET)
	public String listarVisibles(@RequestParam(name = "page", defaultValue = "0") int page, 
						 Model model,
						 Authentication authentication, 
						 HttpServletRequest request, 
						 Locale locale, 
						 Device device) {
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request,
				"ROLE_");		
		if (authentication != null) {
			log.info("Hola, tu username es: ".concat(authentication.getName()));
		}
		if (securityContext.isUserInRole("ADMIN")) {
			log.info("ACCESO CONCEDIDO: Hola, " + authentication.getName() + ", tienes acceso.");
		} else {
			log.info("ACCESO DENEGADO: Hola, NO tienes acceso.");
		}
		deviceType = identificaDevices.getDevice(device);
		Pageable pageRequest = PageRequest.of(page, 50, Sort.by("nombre").ascending());
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		List<Receta> recetasCompratidas = usuario.getRecetasCompartidas();
		List<Long> recetasCompartidasIds = new ArrayList<>();
		for(Receta receta:recetasCompratidas) {
			recetasCompartidasIds.add(receta.getId());
		}
		List<Receta> recetasVisibles = recetaService.findAllByEsPublicaTrueOrAutorOrIdIn("pepe", recetasCompartidasIds);		
		Page<Receta> recetasVisiblesPage = recetaService.findAllByEsPublicaTrueOrAutorOrIdIn(authentication.getName(), recetasCompartidasIds, pageRequest);
		PageRender<Receta> pageRenderVisibles = new PageRender<Receta>("/receta/listarVisibles", recetasVisiblesPage);	
		
		model.addAttribute("titulo", messageSource.getMessage("text.receta.listar.titulo", null, locale));		
		model.addAttribute("tituloVisibles", messageSource.getMessage("text.receta.listar.titulo.visibles", null, locale));
		model.addAttribute("recetasVisibles", recetasVisiblesPage);
		model.addAttribute("page", pageRenderVisibles);
		model.addAttribute("todosRecetasVisibles", recetasVisibles);
		model.addAttribute("deviceType", deviceType);

		return "receta/listarVisibles";
	}
	
	/**
	 * Método que gestiona la información que se envía a la vista del listado.
	 * 
	 * @param page
	 * @param model
	 * @param authentication
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = { "/receta/listarFavoritas", "/browser/receta/listarFavoritas", "/mobile/receta/listarFavoritas" }, method = RequestMethod.GET)
	public String listarFavoritas(@RequestParam(name = "page", defaultValue = "0") int page, 
						 Model model,
						 Authentication authentication, 
						 HttpServletRequest request, 
						 Locale locale, 
						 Device device) {
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request,
				"ROLE_");
		
		if (authentication != null) {
			log.info("Hola, tu username es: ".concat(authentication.getName()));
		}

		if (securityContext.isUserInRole("ADMIN")) {
			log.info("ACCESO CONCEDIDO: Hola, " + authentication.getName() + ", tienes acceso.");
		} else {
			log.info("ACCESO DENEGADO: Hola, NO tienes acceso.");
		}

		deviceType = identificaDevices.getDevice(device);
		
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Pageable pageRequest = PageRequest.of(page, 50, Sort.by("nombre").ascending());
		
		List<Receta> recetasFavoritas = usuario.getRecetasFavoritas();		
		List<Long> recetasFavoritasIds = new ArrayList<>();
		for(Receta receta:recetasFavoritas) {
			recetasFavoritasIds.add(receta.getId());
		}
		Page<Receta> recetasFavoritasPage = recetaService.findByIdIn(recetasFavoritasIds, pageRequest);
		PageRender<Receta> pageRenderFavoritas = new PageRender<Receta>("/receta/listarFavoritos", recetasFavoritasPage);		
		
		model.addAttribute("titulo", messageSource.getMessage("text.receta.listar.titulo", null, locale));
		
		model.addAttribute("tituloFavoritas", messageSource.getMessage("text.receta.listar.titulo.favoritas", null, locale));
		model.addAttribute("recetasFavoritas", recetasFavoritasPage);
		model.addAttribute("page", pageRenderFavoritas);
		model.addAttribute("todosRecetasFavoritas", recetasFavoritas);
		model.addAttribute("deviceType", deviceType);

		return "receta/listarFavoritas";
	}
	
	/**
	 * Método que gestiona la información que se envía a la vista del listado.
	 * 
	 * @param page
	 * @param model
	 * @param authentication
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = { "/receta/listarMias", "/browser/receta/listarMias",
							  "/mobile/receta/listarMias" }, method = RequestMethod.GET)
	public String listarMias(@RequestParam(name = "page", defaultValue = "0") int page, 
						 Model model,
						 Authentication authentication, 
						 HttpServletRequest request, 
						 Locale locale, 
						 Device device) {
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request,
				"ROLE_");
		
		if (authentication != null) {
			log.info("Hola, tu username es: ".concat(authentication.getName()));
		}

		if (securityContext.isUserInRole("ADMIN")) {
			log.info("ACCESO CONCEDIDO: Hola, " + authentication.getName() + ", tienes acceso.");
		} else {
			log.info("ACCESO DENEGADO: Hola, NO tienes acceso.");
		}

		deviceType = identificaDevices.getDevice(device);
		
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		List<Receta> recetasFavoritas = usuario.getRecetasFavoritas();		
		List<Receta> todasMisRecetas = recetaService.findAllByAutor(authentication.getName(), Sort.by("nombre").ascending());	
		for(Receta rf : recetasFavoritas) {
			System.out.println("************comparando favoritas con recetas***********");
			for(Receta r : todasMisRecetas) {
				if(r.getId() == rf.getId()) {
					r.setEsFavorita(true);
					System.out.println("************es favorita: " + r.getNombre() + " - " + r.isEsFavorita());					
					break;
				}
			}
		}
		Pageable pageRequest = PageRequest.of(page, 50, Sort.by("nombre").ascending());

		Page<Receta> misRecetas = recetaService.findAllByAutor(authentication.getName(), pageRequest);			

		PageRender<Receta> pageRender = new PageRender<Receta>("/receta/listarMias", misRecetas);

		model.addAttribute("tituloMias", messageSource.getMessage("text.receta.listar.titulo", null, locale));
		model.addAttribute("recetasMias", misRecetas);
		model.addAttribute("page", pageRender);
		model.addAttribute("todosLosItems", todasMisRecetas);
		model.addAttribute("deviceType", deviceType);

		return "receta/listarMias";
	}

	/**
	 * Método que gestiona la información que se pasa al formulario.
	 * 
	 * @param codigo
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "/receta/form", "/browser/receta/form","/mobile/receta/form"})
	public String crear(Model model, Locale locale,	Device device, Authentication authentication) {
		
		deviceType = identificaDevices.getDevice(device);
		
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
		
		boolean esNuevo = true;
		
		productos = productoService.findAll(Sort.by("nombre").ascending());
		artefactos = artefactoService.findAll(Sort.by("nombre").ascending());
		trabajadores = manoDeObraService.findAll(Sort.by("nombre").ascending());

		model.addAttribute("titulo", messageSource.getMessage("text.receta.crear.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.receta.crear.btn", null, locale));
		model.addAttribute("receta", receta);
		model.addAttribute("productos", productos);
		model.addAttribute("artefactos", artefactos);
		model.addAttribute("trabajadores", trabajadores);
		model.addAttribute("esNuevo", esNuevo);
		model.addAttribute("deviceType", deviceType);

		return "receta/form";
	}
	
//	/**
//	 * Método que gestiona la información que se pasa al formulario.
//	 * 
//	 * @param codigo
//	 * @param model
//	 * @return String
//	 */
//	@RequestMapping(value = { "/receta/formModal", "/browser/receta/formModal", "/mobile/receta/formModal"})
//	public String crearModal(Model model, Locale locale) {
//		
//		productos = productoService.findAll(Sort.by("nombre").ascending());
//		artefactos = artefactoService.findAll(Sort.by("nombre").ascending());
//		trabajadores = manoDeObraService.findAll(Sort.by("nombre").ascending());
//		
//		Receta r = (Receta) model.getAttribute("receta");
//		System.out.println("EN FORM RECETA: " + r.getNombre());
//
//		model.addAttribute("titulo", messageSource.getMessage("text.receta.crear.titulo", null, locale));
//		model.addAttribute("btnText", messageSource.getMessage("text.receta.crear.btn", null, locale));
//		model.addAttribute("productos", productos);
//		model.addAttribute("artefactos", artefactos);
//		model.addAttribute("trabajadores", trabajadores);
//
//		return "receta/form";
//	}

	/**
	 * Método que gestiona la información que se pasa al formulario al editar un
	 * item.
	 * 
	 * @param id
	 * @param flash
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "/receta/form/{id}", "/browser/receta/form/{id}", "/mobile/receta/form/{id}" })
	public String editar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Model model, Locale locale) {
		
		productos = productoService.findAll(Sort.by("nombre").ascending());
		artefactos = artefactoService.findAll(Sort.by("nombre").ascending());
		trabajadores = manoDeObraService.findAll(Sort.by("nombre").ascending());
		
		Receta receta = null;
		if (id > 0) {
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
				List<ArtefactoEnUso> artefactosEnUso = new ArrayList<>();
				ArtefactoEnUso artefacto = new ArtefactoEnUso();
				artefactosEnUso.add(artefacto);
				receta.setArtefactosUtilizados(artefactosEnUso);
			}
			if(receta.getInstrucciones().isEmpty()) {
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

		return "receta/form";
	}
	
	/**
	 * Método que elimina ingredientes durante la edición de una receta.
	 * 
	 * @param idIngrediente
	 * @param receta
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "/receta/eliminaIngrediente/{index}", "/browser/receta/eliminaIngrediente/{index}", "/mobile/receta/eliminaIngrediente/{index}" })
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
	@RequestMapping(value = { "/receta/eliminaInstruccion/{index}", "/browser/receta/eliminaInstruccion/{index}", "/mobile/receta/eliminaInstruccion/{index}" })
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
	@RequestMapping(value = { "/receta/eliminaArtefactoEnUso/{index}", "/browser/receta/eliminaArtefactoEnUso/{index}", "/mobile/receta/eliminaArtefactoEnUso/{index}" })
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
	 * Método que guarda y modifica datos en la BD.
	 * 
	 * @param receta
	 * @param result
	 * @param model
	 * @param flash
	 * @param request
	 * @return String
	 */
	@PostMapping(value = { "/receta/form", "/browser/receta/form", "/mobile/receta/form" })
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
		
		if(receta.getArtefactosUtilizados().get(0).getArtefactoId() == null) {
			receta.getArtefactosUtilizados().clear();
		}
		if(receta.getInstrucciones().get(0).getInstruccion() == null) {
			receta.getInstrucciones().clear();
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
		int cantidadDeInstrucciones = Integer.parseInt(request.getParameter("cantidadDeInstrucciones"));
		for(int i = 0; i < cantidadDeInstrucciones;) {
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
		int instruccionesEnReceta = receta.getInstrucciones().size();
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
		String mensajeFlash = (receta.getId() != null) ? "Receta editada con éxito!" : "Receta creada con éxito!";
		flash.addFlashAttribute("success", mensajeFlash);		
		recetaService.save(receta);	
				
		return "redirect:/receta/verReceta/" + receta.getId();
	}
	
	
	@PostMapping(value = {"/receta/itemIncorporado", "/browser/receta/itemIncorporado", "/mobile/receta/itemIncorporado"})
	public String guardarItemIncorporado(Producto producto, 
							  Artefacto artefacto, 
							  Model model, 
							  RedirectAttributes flash,
							  HttpServletRequest request, 
							  Locale locale) {
		
		boolean nombreProductoNotNull = producto.getCantidad() != 0;
		
		String mensajeFlash = nombreProductoNotNull ? guardaProducto(producto) : guardaArtefacto(artefacto);
		flash.addFlashAttribute("success", mensajeFlash);
		
		Receta receta = obtieneRecetaProvisoria(request);
		
		if(nombreProductoNotNull) {
			Ingrediente ingrediente = new Ingrediente();
			ingrediente.setProductoId(producto.getId());
			receta.getIngredientes().add(ingrediente);
		}
		else {
			ArtefactoEnUso a = new ArtefactoEnUso();
			a.setArtefactoId(artefacto.getId());
			receta.getArtefactosUtilizados().add(a);
		}
		
		model.addAttribute("receta", receta);
		return "redirect:/receta/itemIncorporado";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = { "/receta/itemIncorporado", "/browser/receta/itemIncorporado",	"/mobile/receta/itemIncorporado"})
	public String crearConItemIncorporado(Receta receta, 	
							   			  Model model, 
							   			  Locale locale,
							   			  Device device) {
		
		productos = productoService.findAll(Sort.by("nombre").ascending());
		artefactos = artefactoService.findAll(Sort.by("nombre").ascending());
		trabajadores = manoDeObraService.findAll(Sort.by("nombre").ascending());
		
		boolean seHaCreadoItem = true;

		model.addAttribute("titulo", messageSource.getMessage("text.receta.crear.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.receta.crear.btn", null, locale));
		model.addAttribute("receta", receta);
		model.addAttribute("productos", productos);
		model.addAttribute("artefactos", artefactos);
		model.addAttribute("trabajadores", trabajadores);
		model.addAttribute("seHaCreadoItem", seHaCreadoItem);
		model.addAttribute("deviceType", deviceType);

		return "receta/form";
	}
	
	@RequestMapping(value = { "/receta/verReceta/{recetaId}", "/browser/receta/verReceta/{recetaId}", "/mobile/receta/verReceta/{recetaId}"})
	public String verReceta(@PathVariable(name = "recetaId") long recetaId,
							Authentication authentication, 
							Model model,
							HttpServletRequest request, 
							Locale locale, 
							Device device) {

		deviceType = identificaDevices.getDevice(device);
		
		System.out.println("ID DE RECETA RECIBIDO: " + recetaId);
		
		List<Producto> productos = productoService.findAll(Sort.by("nombre").ascending());
		List<Artefacto> artefactos = artefactoService.findAll(Sort.by("nombre").ascending());
		
		Receta receta = recetaService.findOne(recetaId);
		for(Usuario u:receta.getUsuariosFanaticos()) {
			if(u.getUsername().equals(authentication.getName())) {
				receta.setEsFavorita(true);
				break;
			}
		}
		double costoPorcion = calculaCostoPorcion(receta);
		double costoTotal = costoPorcion * receta.getPorciones();		
		costoPorcion = Precision.round(costoPorcion, 2);
		costoTotal = Precision.round(costoTotal, 2);
		
		model.addAttribute("titulo", receta.getNombre());	
		model.addAttribute("receta", receta);		
		model.addAttribute("productos", productos);
		model.addAttribute("artefactos", artefactos);
		model.addAttribute("costoPorcion", costoPorcion);
		model.addAttribute("costoTotal", costoTotal);
		model.addAttribute("deviceType", deviceType);

		return "receta/verReceta";
	}
	
	@RequestMapping(value = { "/receta/actualizaPrecios", "/browser/receta/actualizaPrecios", "/mobile/receta/actualizaPrecios"})
	public String actualizaPrecios(Receta receta,
								   Model model, 
								   HttpServletRequest request) {
		
		System.out.println("ID DE RECETA RECIBIDO EN ACTUALIZACION: " + receta.getId() + " " + receta.getNombre());
		
		List<Producto> productos = productoService.findAll(Sort.by("nombre").ascending());
		List<Artefacto> artefactos = artefactoService.findAll(Sort.by("nombre").ascending());
		double porcionesDesdePrincipal = Double.parseDouble(request.getParameter("porcionesDesdePrincipal"));
		double porcionesOriginales = receta.getPorciones();
		if(porcionesDesdePrincipal != receta.getPorciones()) {
			for(Ingrediente i:receta.getIngredientes()) {
				double cantidadOriginal = i.getCantidad();
				double cantidadFinal = porcionesDesdePrincipal * cantidadOriginal / porcionesOriginales;
				i.setCantidad(cantidadFinal);
			}
			receta.setPorciones(porcionesDesdePrincipal);
		}
		double costoPorcion = calculaCostoPorcion(receta, request);	
		
		model.addAttribute("titulo", receta.getNombre());	
		model.addAttribute("receta", receta);		
		model.addAttribute("productos", productos);
		model.addAttribute("artefactos", artefactos);
		model.addAttribute("costoPorcion", costoPorcion);
		model.addAttribute("deviceType", deviceType);

		return "receta/verReceta";
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
	
	public double calculaCostoPorcion(Receta receta) {
		System.out.println("****************  CALCULANDO COSTO PORCION  **********************");
		double porciones = receta.getPorciones();
		System.out.println("porciones: " + porciones);
		double costoIngredientes = 0;
		double costoArtefactos = 0;
		double costoManoDeObra = 0;
		
		for(Ingrediente i : receta.getIngredientes()) {
			Producto p = productoService.findOne(i.getProductoId());
			System.out.println("Producto: " + p.getNombre());
			double precioP = p.getPrecioSinDesperdicio();
			System.out.println("precioP: " + precioP);
			double cantidadP = p.getCantidad();
			System.out.println("cantidadP: " + cantidadP);			
			UnidadDeMedida uP = p.getUnidadDeMedida();
			System.out.println("uP: " + uP.getNombre());
			
			double cantidadI = i.getCantidad();
			System.out.println("cantidadI: " + cantidadI);
			UnidadDeMedida uI = i.getUnidadDeMedida();
			System.out.println("uI: " + uI.getNombre());
			if(uP != uI) {
				System.out.println("uP != de uI - Convirtiendo cantidadI");
				cantidadI = uI.convertir(cantidadI, uP);
				System.out.println("cantidadI en " + uP.getNombre() + ": " + cantidadI);
			}
			double precioI = cantidadI * precioP / cantidadP;
			System.out.println("precioI: " + precioI);
			costoIngredientes = costoIngredientes + precioI;
			System.out.println("costoIngredientes provisorio: " + costoIngredientes);
		}
		
		double costo = costoIngredientes + costoArtefactos + costoManoDeObra;
		System.out.println("costo: " + costo);
		double costoPorcion = costo / porciones;
		System.out.println("costoPorcion: " + costoPorcion);
		return costoPorcion;
	}
	
	public double calculaCostoPorcion(Receta receta, HttpServletRequest request) {
		System.out.println("****************  CALCULANDO COSTO PORCION CON PRECIOS ACTUALIZADOS **********************");
		double porciones = receta.getPorciones();
		System.out.println("porciones: " + porciones);
		double costoIngredientes = 0;
		double costoArtefactos = 0;
		double costoManoDeObra = 0;
		
		List<Producto> productosConPrecioActualizado = new ArrayList<>();
		int cantidadPCPA = Integer.parseInt(request.getParameter("cantidadDeCompras"));
		for(int i = 0; i<cantidadPCPA; i++) {
			int index = i+1;
			System.out.println("Producto " + index);
			String nombre = request.getParameter("productoCompra" + index);
			System.out.println("Producto nombre: " + nombre);
			String cantidad = request.getParameter("productoCantidadPresentacion" + index);
			System.out.println("Producto cantidad: " + cantidad);
			String precio = request.getParameter("productoPrecio" + index);
			System.out.println("Producto precio: " + precio);
			String unidad = request.getParameter("productoUnidadDeMedida" + index);
			System.out.println("Producto unidad: " + unidad);
			cantidad = cantidad.replaceAll(",", ".");
			System.out.println("Producto cantidad: " + cantidad);
			precio = precio.replaceAll(",", ".");
			System.out.println("Producto precio: " + precio);
			
			Producto producto = productoService.findByNombre(nombre);
			producto.setCantidad(Double.parseDouble(cantidad));
			producto.setPrecio(Double.parseDouble(precio));
			producto.setPrecioSinDesperdicioDesdeCantidad();
			producto.setUnidadDeMedida(unidad);
			productosConPrecioActualizado.add(producto);
		}
		
		for(Ingrediente i : receta.getIngredientes()) {
			Producto producto = null;
			for(Producto p : productosConPrecioActualizado) {
				if(p.getId() == i.getProductoId()) {
					producto = p;
					break;
				}
			}
			System.out.println("Producto: " + producto.getNombre());
			double precioP = producto.getPrecioSinDesperdicio();
			System.out.println("precioP: " + precioP);
			double cantidadP = producto.getCantidad();
			System.out.println("cantidadP: " + cantidadP);			
			UnidadDeMedida uP = producto.getUnidadDeMedida();
			System.out.println("uP: " + uP.getNombre());			
			double cantidadI = i.getCantidad();
			System.out.println("cantidadI: " + cantidadI);
			UnidadDeMedida uI = i.getUnidadDeMedida();
			System.out.println("uI: " + uI.getNombre());
			if(uP != uI) {
				System.out.println("uP != de uI - Convirtiendo cantidadI");
				cantidadI = uI.convertir(cantidadI, uP);
				System.out.println("cantidadI en " + uP.getNombre() + ": " + cantidadI);
			}
			double precioI = cantidadI * precioP / cantidadP;
			System.out.println("precioI: " + precioI);
			costoIngredientes = costoIngredientes + precioI;
			System.out.println("costoIngredientes provisorio: " + costoIngredientes);
		}
		
		double costo = costoIngredientes + costoArtefactos + costoManoDeObra;
		System.out.println("costo: " + costo);
		double costoPorcion = costo / porciones;
		System.out.println("costoPorcion: " + costoPorcion);
		return costoPorcion;
	}

	@RequestMapping(value = { "/receta/agregaFavorita/{recetaId}", "/receta/ver/agregaFavorita/{recetaId}"}, method = RequestMethod.GET)
	public String agregaFavorita(Authentication authentication, @PathVariable(name = "recetaId") long recetaId, HttpServletRequest request) {
		System.out.println("************AGREGANDO A FAVORITAS***********");
		boolean vieneDeVerReceta = false;
		if(request.getRequestURI().contains("/ver/")) {
			vieneDeVerReceta = true;
		}
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Receta receta = recetaService.findOne(recetaId);
		receta.addUsuarioFanatico(usuario);
		recetaService.save(receta);
		return vieneDeVerReceta ? "redirect:/receta/verReceta/" + recetaId : "redirect:/receta/listar";
	}
	
	@RequestMapping(value = { "/receta/eliminaFavorita/{recetaId}", "/receta/ver/eliminaFavorita/{recetaId}"}, method = RequestMethod.GET)
	public String eliminaFavorita(Authentication authentication, @PathVariable(name = "recetaId") long recetaId, HttpServletRequest request) {
		System.out.println("************ELIMINANDO A FAVORITAS***********");
		boolean vieneDeVerReceta = false;
		if(request.getRequestURI().contains("/ver/")) {
			vieneDeVerReceta = true;
		}
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Receta receta = recetaService.findOne(recetaId);
		receta.removeUsuarioFanatico(usuario);
		recetaService.save(receta);
		return vieneDeVerReceta ? "redirect:/receta/verReceta/" + recetaId : "redirect:/receta/listar";
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
	
	@GetMapping(value = "/fotos/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;

		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}
}
