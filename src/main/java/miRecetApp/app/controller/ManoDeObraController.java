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
import miRecetApp.app.model.entity.ManoDeObra;
import miRecetApp.app.model.entity.ManoDeObraCocinando;
import miRecetApp.app.model.entity.Receta;
import miRecetApp.app.service.IManoDeObraService;
import miRecetApp.app.service.IRecetaService;
import miRecetApp.app.service.implementation.IdentificaDevice;
import miRecetApp.app.util.paginator.PageRender;


@Controller
@SessionAttributes("manoDeObra")
public class ManoDeObraController {
	// ATRIBUTOS

	@Autowired
	private IManoDeObraService manoDeObraService;
			
	@Autowired
	private IRecetaService recetaService;

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
	@RequestMapping(value = { "/manoDeObra/listar", "/browser/manoDeObra/listar",
							  "/mobile/manoDeObra/listar" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, 
						 Model model,
						 Authentication authentication, 
						 HttpServletRequest request, 
						 Locale locale, 
						 Device device) {

		List<ManoDeObra> todosLosManoDeObras = manoDeObraService.findAll();

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

		Page<ManoDeObra> manosDeObra = manoDeObraService.findAll(pageRequest);

		PageRender<ManoDeObra> pageRender = new PageRender<ManoDeObra>("/manoDeObra/listar", manosDeObra);
		model.addAttribute("deviceType", deviceType);
		model.addAttribute("titulo", messageSource.getMessage("text.manoDeObra.listar.titulo", null, locale));
		model.addAttribute("manosDeObra", manosDeObra);
		model.addAttribute("page", pageRender);
		model.addAttribute("todosLosItems", todosLosManoDeObras);

		return "manoDeObra/listar";
	}

	/**
	 * Método que gestiona la información que se pasa al formulario.
	 * 
	 * @param codigo
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "/manoDeObra/form", "/browser/manoDeObra/form", "/mobile/manoDeObra/form"})
	public String crear(@PathVariable(value = "codigo", required = false) String codigo, Model model, Locale locale,
			Device device) {

		ManoDeObra manoDeObra = new ManoDeObra();

		deviceType = identificaDevices.getDevice(device);
		model.addAttribute("deviceType", deviceType);
		model.addAttribute("titulo", messageSource.getMessage("text.manoDeObra.crear.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.manoDeObra.crear.btn", null, locale));
		model.addAttribute("manoDeObra", manoDeObra);

		return "manoDeObra/form";
	}

	/**
	 * Método que gestiona la información que se pasa al formulario al editar un item.
	 * 
	 * @param id
	 * @param flash
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "/manoDeObra/form/{id}", "/browser/manoDeObra/form/{id}", "/mobile/manoDeObra/form/{id}" })
	public String editar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Model model, Locale locale,
			Device device) {

		ManoDeObra manoDeObra = null;

		deviceType = identificaDevices.getDevice(device);

		if (id > 0) {

			manoDeObra = manoDeObraService.findOne(id);

			if (manoDeObra == null) {
				flash.addFlashAttribute("error", "El ID del Trabajador no existe!");
				return "redirect:/manoDeObra/listar";
			}

		} else {
			flash.addFlashAttribute("error", "El ID del Trabajador no puede ser 0!");
			return "redirect:/manoDeObra/listar";
		}
		model.addAttribute("deviceType", deviceType);
		model.addAttribute("titulo", messageSource.getMessage("text.manoDeObra.editar.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.manoDeObra.editar.btn", null, locale));
		model.addAttribute("manoDeObra", manoDeObra);

		return "manoDeObra/form";
	}

	/**
	 * Método que elimina items de la BD.
	 * 
	 * @param id
	 * @param flash
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/manoDeObra/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Model model) {

		if (id > 0) {

			List<Receta> recetas = recetaService.findAll();

			for (Receta receta : recetas) {
				List<ManoDeObraCocinando> manoDeObraUtilizada = receta.getManoDeObra();
				for (ManoDeObraCocinando trabajador : manoDeObraUtilizada) {
					if (id == trabajador.getTrabajadorId()) {
						flash.addFlashAttribute("error","No se puede eliminar un trabajador, "
								+ "si está asignado a una receta. Modifique las recetas antes de eliminarlo de la BD. "
								+ "El trabajador se ha encontrado en la receta "
								+ receta.getNombre() + " , pero puede que esté en más.");
						return "redirect:/manoDeObra/listar";
					}
				}
			}
			manoDeObraService.delete(id);

			flash.addFlashAttribute("success", "Trabajador eliminado con éxito de la BD!");

		}
		return "redirect:/manoDeObra/listar";
	}

	/**
	 * Método que guarda y modifica datos en la BD.
	 * 
	 * @param manoDeObra
	 * @param result
	 * @param model
	 * @param flash
	 * @param request
	 * @return String
	 */
	@PostMapping(value = { "/manoDeObra/form", "/browser/manoDeObra/form", "/mobile/manoDeObra/form" })
	public String guardar(@Valid ManoDeObra manoDeObra, BindingResult result, Model model, RedirectAttributes flash,
			HttpServletRequest request, Locale locale, Device device) {

		deviceType = identificaDevices.getDevice(device);

		if (result.hasErrors() && manoDeObra.getId() == null) {

			if (result.hasErrors()) {
				System.out.println("HAY ERRORES PRODUCTO: " + result.getFieldError().getDefaultMessage());
				System.out.println("HAY ERRORES PRODUCTO: " + result.getFieldError().getCode());
			}
			model.addAttribute("deviceType", deviceType);
			model.addAttribute("titulo", messageSource.getMessage("text.manoDeObra.crear.titulo", null, locale));
			model.addAttribute("btnText", messageSource.getMessage("text.manoDeObra.crear.btn", null, locale));
			return "manoDeObra/form";

		} else if (result.hasErrors() && manoDeObra.getId() != null && manoDeObra.getId() > 0) {
			System.out.println("HAY ERRORES PRODUCTO ID" + result.getFieldError().getDefaultMessage());
			System.out.println("HAY ERRORES PRODUCTO ID" + result.getFieldError().getCode());
			model.addAttribute("deviceType", deviceType);
			model.addAttribute("titulo", messageSource.getMessage("text.manoDeObra.editar.titulo", null, locale));
			model.addAttribute("btnText", messageSource.getMessage("text.manoDeObra.editar.btn", null, locale));
			return "manoDeObra/form";
		}

		String mensajeFlash = (manoDeObra.getId() != null) ? "Trabajador editada con éxito!" : "Trabajador creada con éxito!";
		
		manoDeObra.setCargasSociales(30);
		manoDeObraService.save(manoDeObra);
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/manoDeObra/listar";
	}
}
