package miRecetApp.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.derby.tools.sysinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import miRecetApp.app.model.entity.Artefacto;
import miRecetApp.app.model.entity.ArtefactoEnUso;
import miRecetApp.app.model.entity.Ingrediente;
import miRecetApp.app.model.entity.Instruccion;
import miRecetApp.app.model.entity.Producto;
import miRecetApp.app.model.entity.Receta;
import miRecetApp.app.service.IArtefactoService;
import miRecetApp.app.service.IProductoService;
import miRecetApp.app.service.IRecetaService;
import miRecetApp.app.service.implementation.IdentificaDevice;
import miRecetApp.app.util.paginator.PageRender;

/**
 * Clase que controla la gestión de la pestaña Productos de la vista. 
 * 
 * @author Julian Quenard
 */

@Controller
@SessionAttributes("producto")
public class ProductoController {
	
	// ATRIBUTOS

	@Autowired
	private IProductoService productoService;

	@Autowired
	private IRecetaService recetaService;

	@Autowired
	private IArtefactoService artefactoService;

	@Autowired
	private MessageSource messageSource;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private IdentificaDevice identificaDevices;

	String deviceType = "browser";

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
	@RequestMapping(value = { "/producto/listar", "/browser/producto/listar",
			"/mobile/producto/listar" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request, Locale locale, Device device) {

		List<Producto> todosLosProductos = productoService.findAll();

		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request,
				"ROLE_");

		deviceType = identificaDevices.getDevice(device);

		if (authentication != null) {
			log.info("Hola, tu username es: ".concat(authentication.getName()));
		}

		if (securityContext.isUserInRole("ADMIN")) {
			log.info("ACCESO CONCEDIDO: Hola, " + authentication.getName() + ", tienes acceso.");
		} else {
			log.info("ACCESO DENEGADO: Hola, NO tienes acceso.");
		}

		Pageable pageRequest = PageRequest.of(page, 50, Sort.by("nombre").ascending());

		Page<Producto> productos = productoService.findAll(pageRequest);

		PageRender<Producto> pageRender = new PageRender<Producto>("/producto/listar", productos);
		model.addAttribute("deviceType", deviceType);
		model.addAttribute("titulo", messageSource.getMessage("text.producto.listar.titulo", null, locale));
		model.addAttribute("productos", productos);
		model.addAttribute("page", pageRender);
		model.addAttribute("todosLosItems", todosLosProductos);

		System.out.println("PRUEBA PROD COD BARRA: " + productoService.findByCodigoDeBarra("1234"));

		return "producto/listar";
	}

	/**
	 * Método que gestiona la información que se pasa al formulario.
	 * 
	 * @param codigo
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "/producto/form", "/producto/form/scann/{codigo}", "/browser/producto/form",
			"/browser/producto/form/scann/{codigo}", "/mobile/producto/form", "/mobile/producto/form/scann/{codigo}" })
	public String crear(@PathVariable(value = "codigo", required = false) String codigo, Model model, Locale locale,
			Device device) {

		Producto producto = new Producto();

		deviceType = identificaDevices.getDevice(device);

		if (codigo != null) {
			producto.setCodigoDeBarra(codigo);
		}
		model.addAttribute("deviceType", deviceType);
		model.addAttribute("titulo", messageSource.getMessage("text.producto.crear.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.producto.crear.btn", null, locale));
		model.addAttribute("producto", producto);

		return "producto/form";
	}

	/**
	 * Método que gestiona la información que se pasa al formulario.
	 * 
	 * @param codigo
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "/producto/formModal", "/producto/formModal/scann/{codigo}",
			"/browser/producto/formModal", "/browser/producto/formModal/scann/{codigo}", "/mobile/producto/formModal",
			"/mobile/producto/formModal/scann/{codigo}" })
	public String crearModal(@PathVariable(value = "codigo", required = false) String codigo, Model model,
			Locale locale, Device device) {

		Producto producto = new Producto();

		List<Producto> productos = productoService.findAll(Sort.by("nombre").ascending());
		List<Artefacto> artefactos = artefactoService.findAll(Sort.by("nombre").ascending());

		deviceType = identificaDevices.getDevice(device);

		if (codigo != null) {
			producto.setCodigoDeBarra(codigo);
		}
		model.addAttribute("deviceType", deviceType);
		model.addAttribute("titulo", messageSource.getMessage("text.producto.crear.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.producto.crear.btn", null, locale));
		model.addAttribute("producto", producto);
		model.addAttribute("productos", productos);
		model.addAttribute("artefactos", artefactos);

		return "producto/formModal";
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
	@RequestMapping(value = { "/producto/form/{id}", "/browser/producto/form/{id}", "/mobile/producto/form/{id}" })
	public String editar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Model model, Locale locale,
			Device device) {

		Producto producto = null;

		deviceType = identificaDevices.getDevice(device);

		if (id > 0) {

			producto = productoService.findOne(id);

			if (producto == null) {
				flash.addFlashAttribute("error", "El ID del Producto no existe!");
				return "redirect:/producto/listar";
			}

		} else {
			flash.addFlashAttribute("error", "El ID del Producto no puede ser 0!");
			return "redirect:/producto/listar";
		}
		model.addAttribute("deviceType", deviceType);
		model.addAttribute("titulo", messageSource.getMessage("text.producto.editar.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.producto.editar.btn", null, locale));
		model.addAttribute("producto", producto);

		return "producto/form";
	}

	/**
	 * Método que elimina items de la BD.
	 * 
	 * @param id
	 * @param flash
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/producto/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Model model) {

		if (id > 0) {

			List<Receta> recetas = recetaService.findAll();

			for (Receta receta : recetas) {
				List<Ingrediente> ingredientes = receta.getIngredientes();
				for (Ingrediente ingrediente : ingredientes) {
					if (id == ingrediente.getProductoId()) {
						flash.addFlashAttribute("error",
								"No se puede eliminar un Producto, si se usa en una receta. Modifique las recetas antes de eliminar. El Producto se ha encontrado en la receta "
										+ receta.getNombre() + " , pero puede que esté en más.");
						return "redirect:/producto/listar";
					}
				}
			}
			productoService.delete(id);

			flash.addFlashAttribute("success", "Producto eliminado con éxito!");

		}
		return "redirect:/producto/listar";
	}

	/**
	 * Método que guarda y modifica datos en la BD.
	 * 
	 * @param producto
	 * @param result
	 * @param model
	 * @param flash
	 * @param request
	 * @return String
	 */
	@PostMapping(value = { "/producto/form", "/browser/producto/form", "/mobile/producto/form" })
	public String guardar(@Valid Producto producto, BindingResult result, Model model, RedirectAttributes flash,
			HttpServletRequest request, Locale locale, Device device) {

		deviceType = identificaDevices.getDevice(device);

		if (producto.getId() == null) {
			if (result.hasErrors() || existeNombreEnDB(producto.getNombre())) {
				if (result.hasErrors()) {
					System.out.println("HAY ERRORES PRODUCTO: " + result.getFieldError().getDefaultMessage());
					System.out.println("HAY ERRORES PRODUCTO: " + result.getFieldError().getCode());
					if(producto.getNombre().isBlank()) {
						model.addAttribute("deviceType", deviceType);
						model.addAttribute("nombreEnBlanco", true);
						model.addAttribute("nombreError", messageSource.getMessage("NotBlank", null, locale));
					}
				}
				if (existeNombreEnDB(producto.getNombre())) {
					System.out.println("HAY ERRORES PRODUCTO NOMBRE REPETIDO");
					model.addAttribute("deviceType", deviceType);
					model.addAttribute("nombreError", messageSource.getMessage("nombreRepetido", null, locale));
					model.addAttribute("repetido", true);
				}
				model.addAttribute("deviceType", deviceType);
				model.addAttribute("titulo", messageSource.getMessage("text.producto.crear.titulo", null, locale));
				model.addAttribute("btnText", messageSource.getMessage("text.producto.crear.btn", null, locale));
				return "producto/form";
			}

		} else if (result.hasErrors() && producto.getId() != null && producto.getId() > 0) {
			System.out.println("HAY ERRORES EDITANDO EN PRODUCTO ID" + result.getFieldError().getDefaultMessage());
			model.addAttribute("deviceType", deviceType);
			model.addAttribute("titulo", messageSource.getMessage("text.producto.editar.titulo", null, locale));
			model.addAttribute("btnText", messageSource.getMessage("text.producto.editar.btn", null, locale));
			return "producto/form";
		}

		String mensajeFlash = (producto.getId() != null) ? "Producto editado con éxito!" : "Producto creado con éxito!";

		System.out.println(producto.getUnidadDeMedida().getNombre());

		double cantidadSinDesperdicio = producto.getCantidad()
				- (producto.getDesperdicio() * producto.getCantidad() / 100);
		producto.setPrecioSinDesperdicio(producto.getCantidad() * producto.getPrecio() / cantidadSinDesperdicio);

		System.out.println(producto.getPrecioSinDesperdicio());

		productoService.save(producto);
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/producto/listar";
	}

	public boolean existeNombreEnDB(String nombre) {
		boolean existeNombre = false;
		List<Producto> productos = (List<Producto>) productoService.findAll();
		for (Producto p : productos) {
			if (p.getNombre().equals(nombre)) {
				existeNombre = true;
				break;
			}
		}
		return existeNombre;
	}

}
