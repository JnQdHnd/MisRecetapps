package miRecetApp.app.controller;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.math3.util.Precision;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import miRecetApp.app.model.entity.Artefacto;
import miRecetApp.app.model.entity.Ingrediente;
import miRecetApp.app.model.entity.ManoDeObra;
import miRecetApp.app.model.entity.Preparacion;
import miRecetApp.app.model.entity.Producto;
import miRecetApp.app.model.entity.Receta;
import miRecetApp.app.model.entity.UnidadDeMedida;
import miRecetApp.app.model.entity.Usuario;
import miRecetApp.app.service.IArtefactoService;
import miRecetApp.app.service.IProductoService;
import miRecetApp.app.service.IRecetaService;
import miRecetApp.app.service.IUploadFileService;
import miRecetApp.app.service.implementation.IdentificaDevice;
import miRecetApp.app.service.implementation.UsuarioService;
import miRecetApp.app.util.paginator.PageRender;

@Controller
@SessionAttributes("recetario")
public class RecetaVerController {

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
		
		List<Producto> productos = productoService.findAll(Sort.by("nombre").ascending());		
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
		model.addAttribute("productos", productos);
		
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
	@RequestMapping(value = { "/receta/listarFavoritas"}, method = RequestMethod.GET)
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
	
	@RequestMapping(value = { "/receta/verReceta/{recetaId}"})
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
	
	@RequestMapping(value = { "/receta/actualizaPrecios"}) 
	public String actualizaPrecios(Receta r,
								   Model model, 
								   HttpServletRequest request) {

		List<Producto> productos = productoService.findAll(Sort.by("nombre").ascending());
		List<Artefacto> artefactos = artefactoService.findAll(Sort.by("nombre").ascending());

		System.out.println("ID DE RECETA RECIBIDO EN ACTUALIZACION: " + r.getId());
		Receta receta = recetaService.findOne(r.getId());
		double porcionesDesdePrincipal = Double.parseDouble(request.getParameter("porcionesDesdePrincipal"));
		System.out.println("porcionesDesdePrincipal: " + porcionesDesdePrincipal);
		double porcionesOriginales = receta.getPorciones();
		if(porcionesDesdePrincipal != receta.getPorciones()) {
			for(Ingrediente i:receta.getIngredientes()) {
				double cantidadOriginal = i.getCantidad();
				double cantidadFinal = porcionesDesdePrincipal * cantidadOriginal / porcionesOriginales;
				i.setCantidad(cantidadFinal);
			}
			receta.setPorciones(porcionesDesdePrincipal);
		}		
		double costoTotal = calculaCostoTotal(receta, request);	
		double costoPorcion = costoTotal / receta.getPorciones();
		costoPorcion = Precision.round(costoPorcion, 2);
		
		model.addAttribute("titulo", receta.getNombre());	
		model.addAttribute("receta", receta);		
		model.addAttribute("productos", productos);
		model.addAttribute("artefactos", artefactos);
		model.addAttribute("costoPorcion", costoPorcion);
		model.addAttribute("deviceType", deviceType);

		return "receta/verReceta";
	}
	
	@RequestMapping(value = { "/receta/verRecetaConPreparaciones/{recetaId}"})
	public String verRecetaConPreparacion(@PathVariable(name = "recetaId") long recetaId,
							Authentication authentication, 
							Model model,
							HttpServletRequest request, 
							Locale locale, 
							Device device) {

		deviceType = identificaDevices.getDevice(device);
		
		System.out.println("ID DE RECETA RECIBIDO: " + recetaId);
		
		List<Producto> productos = productoService.findAll(Sort.by("nombre").ascending());
		List<Artefacto> artefactos = artefactoService.findAll(Sort.by("nombre").ascending());
		List<Receta> preparaciones = new ArrayList<>();
		LinkedHashSet<Long> ingredientesSinRepetir = new LinkedHashSet<Long>();		
		double costoTotal = 0;
		double costoPorcion = 0;		
		
		Receta receta = recetaService.findOne(recetaId);
		for(Preparacion p : receta.getPreparaciones()) {
			Long id = p.getRecetaId();
			Receta r = recetaService.findOne(id);
			preparaciones.add(r);
			double costoDePreparacionTotal = calculaCostoTotal(r);
			costoTotal = costoTotal + costoDePreparacionTotal;
			System.out.println("PREPARACION: " + r.getNombre() + " - Costo: " + costoDePreparacionTotal);
			for (Ingrediente ingrediente : r.getIngredientes()) {
				Long productoId = ingrediente.getProductoId();
				ingredientesSinRepetir.add(productoId);							
			}
		}
		
		costoPorcion = costoTotal / receta.getPorciones();				
		costoPorcion = Precision.round(costoPorcion, 2);
		costoTotal = Precision.round(costoTotal, 2);
		
		System.out.println("RECETA: " + receta.getNombre() + " - CostoTotal: " + costoTotal);
		System.out.println("RECETA: " + receta.getNombre() + " - CostoPorcion: " + costoPorcion);
		
		for(Usuario u:receta.getUsuariosFanaticos()) {
			if(u.getUsername().equals(authentication.getName())) {
				receta.setEsFavorita(true);
				break;
			}
		}
		
		model.addAttribute("titulo", receta.getNombre());	
		model.addAttribute("receta", receta);	
		model.addAttribute("preparaciones", preparaciones);	
		model.addAttribute("ingredientesSinRepetir", ingredientesSinRepetir);	
		model.addAttribute("productos", productos);
		model.addAttribute("artefactos", artefactos);
		model.addAttribute("costoPorcion", costoPorcion);
		model.addAttribute("costoTotal", costoTotal);
		model.addAttribute("deviceType", deviceType);

		return "receta/verRecetaConPreparaciones";
	}
	
	@RequestMapping(value = { "/receta/actualizaPreciosConPreparaciones"}) 
	public String actualizaPreciosConPreparacion(Receta r,
								   Model model, 
								   HttpServletRequest request) {
		System.out.println("ID DE RECETA RECIBIDO EN ACTUALIZACION: " + r.getId() + " " + r.getNombre());
		
		List<Producto> productos = productoService.findAll(Sort.by("nombre").ascending());
		List<Artefacto> artefactos = artefactoService.findAll(Sort.by("nombre").ascending());
		List<Receta> preparaciones = new ArrayList<>();
		LinkedHashSet<Long> ingredientesSinRepetir = new LinkedHashSet<Long>();		
		double costoTotal = 0;
		double costoPorcion = 0;
		Receta receta = recetaService.findOne(r.getId());
		double porcionesDesdePrincipal = Double.parseDouble(request.getParameter("porcionesDesdePrincipal"));
		double porcionesOriginales = receta.getPorciones();
		
		for(Preparacion p : receta.getPreparaciones()) {
			Long id = p.getRecetaId();
			Receta preparacion = recetaService.findOne(id);
			if(porcionesDesdePrincipal != receta.getPorciones()) {
				for(Ingrediente i:preparacion.getIngredientes()) {
					double cantidadOriginal = i.getCantidad();
					double cantidadFinal = porcionesDesdePrincipal * cantidadOriginal / porcionesOriginales;
					i.setCantidad(cantidadFinal);
				}
				preparacion.setPorciones(porcionesDesdePrincipal);
			}
			preparaciones.add(preparacion);
			double costoDePreparacionTotal = calculaCostoTotal(preparacion, request);
			costoTotal = costoTotal + costoDePreparacionTotal;
			System.out.println("PREPARACION: " + preparacion.getNombre() + " - Costo: " + costoDePreparacionTotal);
			for (Ingrediente ingrediente : preparacion.getIngredientes()) {
				Long productoId = ingrediente.getProductoId();
				ingredientesSinRepetir.add(productoId);							
			}
			
		}		
		receta.setPorciones(porcionesDesdePrincipal);
		costoPorcion = costoTotal / receta.getPorciones();				
		costoPorcion = Precision.round(costoPorcion, 2);
		costoTotal = Precision.round(costoTotal, 2);
		
		model.addAttribute("titulo", receta.getNombre());	
		model.addAttribute("receta", receta);	
		model.addAttribute("preparaciones", preparaciones);	
		model.addAttribute("ingredientesSinRepetir", ingredientesSinRepetir);	
		model.addAttribute("productos", productos);
		model.addAttribute("artefactos", artefactos);
		model.addAttribute("costoPorcion", costoPorcion);
		model.addAttribute("costoTotal", costoTotal);
		model.addAttribute("deviceType", deviceType);

		return "receta/verRecetaConPreparaciones";
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
	
	public double calculaCostoTotal(Receta receta) {
		System.out.println("****************  CALCULANDO COSTO TOTAL  **********************");
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
		return costo;
	}
	
	public double calculaCostoTotal(Receta receta, HttpServletRequest request) {
		System.out.println("****************  CALCULANDO COSTO TOTAL CON PRECIOS ACTUALIZADOS **********************");
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
		return costo;
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
