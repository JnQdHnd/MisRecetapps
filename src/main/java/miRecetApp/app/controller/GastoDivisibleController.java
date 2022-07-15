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
import miRecetApp.app.model.entity.GastoDivisible;
import miRecetApp.app.service.IGastoDivisibleService;
import miRecetApp.app.service.implementation.IdentificaDevice;
import miRecetApp.app.util.paginator.PageRender;


/**
 * @author Julián Quenard *
 * 01-09-2021
 */

@Controller
@SessionAttributes("gastoDivisible")
public class GastoDivisibleController {
	// ATRIBUTOS

	@Autowired
	private IGastoDivisibleService gastoDivisibleService;

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
	@RequestMapping(value = { "/gastoDivisible/listar", "/browser/gastoDivisible/listar",
							  "/mobile/gastoDivisible/listar" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, 
						 Model model,
						 Authentication authentication, 
						 HttpServletRequest request, 
						 Locale locale, 
						 Device device) {

		List<GastoDivisible> todosLosGastosDivisibles = gastoDivisibleService.findAll();

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

		Pageable pageRequest = PageRequest.of(page, 20, Sort.by("id").ascending());

		Page<GastoDivisible> gastosDivisibles = gastoDivisibleService.findAll(pageRequest);

		PageRender<GastoDivisible> pageRender = new PageRender<GastoDivisible>("/gastoDivisible/listar", gastosDivisibles);

		model.addAttribute("titulo", messageSource.getMessage("text.gastoDivisible.listar.titulo", null, locale));
		model.addAttribute("gastosDivisibles", gastosDivisibles);
		model.addAttribute("page", pageRender);
		model.addAttribute("todosLosGastosDivisibles", todosLosGastosDivisibles);
		model.addAttribute("deviceType", deviceType);

		return "gastoDivisible/listar";
	}

	/**
	 * Método que gestiona la información que se pasa al formulario.
	 * 
	 * @param codigo
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "/gastoDivisible/form", "/browser/gastoDivisible/form", "/mobile/gastoDivisible/form"})
	public String crear(@PathVariable(value = "codigo", required = false) String codigo, Model model, Locale locale,
			Device device) {

		GastoDivisible gastoDivisible = new GastoDivisible();

		deviceType = identificaDevices.getDevice(device);

		model.addAttribute("titulo", messageSource.getMessage("text.gastoDivisible.crear.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.gastoDivisible.crear.btn", null, locale));
		model.addAttribute("gastoDivisible", gastoDivisible);
		model.addAttribute("deviceType", deviceType);

		return "gastoDivisible/form";
	}

	/**
	 * Método que gestiona la información que se pasa al formulario al editar un item.
	 * 
	 * @param id
	 * @param flash
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "/gastoDivisible/form/{id}", "/browser/gastoDivisible/form/{id}", "/mobile/gastoDivisible/form/{id}" })
	public String editar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Model model, Locale locale,
			Device device) {

		GastoDivisible gastoDivisible = null;

		deviceType = identificaDevices.getDevice(device);

		if (id > 0) {

			gastoDivisible = gastoDivisibleService.findOne(id);

			if (gastoDivisible == null) {
				flash.addFlashAttribute("error", "El ID del Trabajador no existe!");
				return "redirect:/gastoDivisible/listar";
			}

		} else {
			flash.addFlashAttribute("error", "El ID del Trabajador no puede ser 0!");
			return "redirect:/gastoDivisible/listar";
		}

		model.addAttribute("titulo", messageSource.getMessage("text.gastoDivisible.editar.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.gastoDivisible.editar.btn", null, locale));
		model.addAttribute("gastoDivisible", gastoDivisible);
		model.addAttribute("deviceType", deviceType);

		return "gastoDivisible/form";
	}

	/**
	 * Método que elimina items de la BD.
	 * 
	 * @param id
	 * @param flash
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/gastoDivisible/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Model model) {

		if (id > 0) {

			gastoDivisibleService.delete(id);

			flash.addFlashAttribute("success", "Trabajador eliminado con éxito de la BD!");

		}
		return "redirect:/gastoDivisible/listar";
	}

	/**
	 * Método que guarda y modifica datos en la BD.
	 * 
	 * @param gastoDivisible
	 * @param result
	 * @param model
	 * @param flash
	 * @param request
	 * @return String
	 */
	@PostMapping(value = { "/gastoDivisible/form", "/browser/gastoDivisible/form", "/mobile/gastoDivisible/form" })
	public String guardar(@Valid GastoDivisible gastoDivisible, BindingResult result, Model model, RedirectAttributes flash,
			HttpServletRequest request, Locale locale, Device device) {

		deviceType = identificaDevices.getDevice(device);

		if (result.hasErrors() && gastoDivisible.getId() == null) {

			if (result.hasErrors()) {
				System.out.println("HAY ERRORES PRODUCTO: " + result.getFieldError().getDefaultMessage());
				System.out.println("HAY ERRORES PRODUCTO: " + result.getFieldError().getCode());
			}

			model.addAttribute("titulo", messageSource.getMessage("text.gastoDivisible.crear.titulo", null, locale));
			model.addAttribute("btnText", messageSource.getMessage("text.gastoDivisible.crear.btn", null, locale));
			model.addAttribute("deviceType", deviceType);
			return "gastoDivisible/form";

		} else if (result.hasErrors() && gastoDivisible.getId() != null && gastoDivisible.getId() > 0) {
			System.out.println("HAY ERRORES PRODUCTO ID" + result.getFieldError().getDefaultMessage());
			System.out.println("HAY ERRORES PRODUCTO ID" + result.getFieldError().getCode());

			model.addAttribute("titulo", messageSource.getMessage("text.gastoDivisible.editar.titulo", null, locale));
			model.addAttribute("btnText", messageSource.getMessage("text.gastoDivisible.editar.btn", null, locale));
			model.addAttribute("deviceType", deviceType);
			return "gastoDivisible/form";
		}

		String mensajeFlash = (gastoDivisible.getId() != null) ? "Trabajador editada con éxito!" : "Trabajador creada con éxito!";
		
		gastoDivisibleService.save(gastoDivisible);
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/gastoDivisible/listar";
	}
}