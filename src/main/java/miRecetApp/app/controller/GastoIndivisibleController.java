package miRecetApp.app.controller;

import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
import miRecetApp.app.model.entity.GastoIndivisible;
import miRecetApp.app.service.IGastoIndivisibleService;
import miRecetApp.app.service.implementation.IdentificaDevice;
import miRecetApp.app.util.paginator.PageRender;

@Controller
@SessionAttributes("gastoIndivisible")
public class GastoIndivisibleController {
	// ATRIBUTOS

	@Autowired
	private IGastoIndivisibleService gastoIndivisibleService;

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
	@RequestMapping(value = { "/gastoIndivisible/listar", "/browser/gastoIndivisible/listar",
							  "/mobile/gastoIndivisible/listar" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, 
						 Model model,
						 Authentication authentication, 
						 HttpServletRequest request, 
						 Locale locale, 
						 Device device) {

		List<GastoIndivisible> todosLosGastosIndivisibles = gastoIndivisibleService.findAll();

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

		Pageable pageRequest = PageRequest.of(page, 20, Sort.by("nombre").ascending());

		Page<GastoIndivisible> gastosIndivisibles = gastoIndivisibleService.findAll(pageRequest);

		PageRender<GastoIndivisible> pageRender = new PageRender<GastoIndivisible>("/gastoIndivisible/listar", gastosIndivisibles);

		model.addAttribute("titulo", messageSource.getMessage("text.gastoIndivisible.listar.titulo", null, locale));
		model.addAttribute("gastosIndivisibles", gastosIndivisibles);
		model.addAttribute("page", pageRender);
		model.addAttribute("todosLosGastosIndivisibles", todosLosGastosIndivisibles);
		model.addAttribute("deviceType", deviceType);

		return "gastoIndivisible/listar";
	}

	/**
	 * Método que gestiona la información que se pasa al formulario.
	 * 
	 * @param codigo
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "/gastoIndivisible/form", "/browser/gastoIndivisible/form", "/mobile/gastoIndivisible/form"})
	public String crear(@PathVariable(value = "codigo", required = false) String codigo, Model model, Locale locale,
			Device device) {

		GastoIndivisible gastoIndivisible = new GastoIndivisible();

		deviceType = identificaDevices.getDevice(device);

		model.addAttribute("titulo", messageSource.getMessage("text.gastoIndivisible.crear.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.gastoIndivisible.crear.btn", null, locale));
		model.addAttribute("gastoIndivisible", gastoIndivisible);
		model.addAttribute("deviceType", deviceType);

		return "gastoIndivisible/form";
	}

	/**
	 * Método que gestiona la información que se pasa al formulario al editar un item.
	 * 
	 * @param id
	 * @param flash
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "/gastoIndivisible/form/{id}", "/browser/gastoIndivisible/form/{id}", "/mobile/gastoIndivisible/form/{id}" })
	public String editar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Model model, Locale locale,
			Device device) {

		GastoIndivisible gastoIndivisible = null;

		deviceType = identificaDevices.getDevice(device);

		if (id > 0) {

			gastoIndivisible = gastoIndivisibleService.findOne(id);

			if (gastoIndivisible == null) {
				flash.addFlashAttribute("error", "El ID del Trabajador no existe!");
				return "redirect:/gastoIndivisible/listar";
			}

		} else {
			flash.addFlashAttribute("error", "El ID del Trabajador no puede ser 0!");
			return "redirect:/gastoIndivisible/listar";
		}

		model.addAttribute("titulo", messageSource.getMessage("text.gastoIndivisible.editar.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.gastoIndivisible.editar.btn", null, locale));
		model.addAttribute("gastoIndivisible", gastoIndivisible);
		model.addAttribute("deviceType", deviceType);

		return "gastoIndivisible/form";
	}

	/**
	 * Método que elimina items de la BD.
	 * 
	 * @param id
	 * @param flash
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/gastoIndivisible/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Model model) {

		if (id > 0) {

			gastoIndivisibleService.delete(id);

			flash.addFlashAttribute("success", "Trabajador eliminado con éxito de la BD!");

		}
		return "redirect:/gastoIndivisible/listar";
	}

	/**
	 * Método que guarda y modifica datos en la BD.
	 * 
	 * @param gastoIndivisible
	 * @param result
	 * @param model
	 * @param flash
	 * @param request
	 * @return String
	 */
	@PostMapping(value = { "/gastoIndivisible/form", "/browser/gastoIndivisible/form", "/mobile/gastoIndivisible/form" })
	public String guardar(@Valid GastoIndivisible gastoIndivisible, BindingResult result, Model model, RedirectAttributes flash,
			HttpServletRequest request, Locale locale, Device device) {

		deviceType = identificaDevices.getDevice(device);

		if (result.hasErrors() && gastoIndivisible.getId() == null) {

			if (result.hasErrors()) {
				System.out.println("HAY ERRORES PRODUCTO: " + result.getFieldError().getDefaultMessage());
				System.out.println("HAY ERRORES PRODUCTO: " + result.getFieldError().getCode());
			}
			model.addAttribute("deviceType", deviceType);
			model.addAttribute("titulo", messageSource.getMessage("text.gastoIndivisible.crear.titulo", null, locale));
			model.addAttribute("btnText", messageSource.getMessage("text.gastoIndivisible.crear.btn", null, locale));
			return "gastoIndivisible/form";
			

		} else if (result.hasErrors() && gastoIndivisible.getId() != null && gastoIndivisible.getId() > 0) {
			System.out.println("HAY ERRORES PRODUCTO ID" + result.getFieldError().getDefaultMessage());
			System.out.println("HAY ERRORES PRODUCTO ID" + result.getFieldError().getCode());
			model.addAttribute("deviceType", deviceType);
			model.addAttribute("titulo", messageSource.getMessage("text.gastoIndivisible.editar.titulo", null, locale));
			model.addAttribute("btnText", messageSource.getMessage("text.gastoIndivisible.editar.btn", null, locale));
			return "gastoIndivisible/form";
		}

		String mensajeFlash = (gastoIndivisible.getId() != null) ? "Trabajador editada con éxito!" : "Trabajador creada con éxito!";
		
		gastoIndivisibleService.save(gastoIndivisible);
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/gastoIndivisible/listar";
	}
}
