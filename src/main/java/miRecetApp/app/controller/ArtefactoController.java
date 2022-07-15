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
import miRecetApp.app.model.entity.Artefacto;
import miRecetApp.app.model.entity.ArtefactoEnUso;
import miRecetApp.app.model.entity.Receta;
import miRecetApp.app.service.IArtefactoService;
import miRecetApp.app.service.IRecetaService;
import miRecetApp.app.service.implementation.IdentificaDevice;
import miRecetApp.app.util.paginator.PageRender;


/**
 * @author Julián Quenard *
 * 01-09-2021
 */

@Controller
@SessionAttributes("artefacto")
public class ArtefactoController {

	// ATRIBUTOS

	@Autowired
	private IArtefactoService artefactoService;
			
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
	@RequestMapping(value = { "/artefacto/listar", "/browser/artefacto/listar",
							  "/mobile/artefacto/listar" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, 
						 Model model,
						 Authentication authentication, 
						 HttpServletRequest request, 
						 Locale locale, 
						 Device device) {

		List<Artefacto> todosLosArtefactos = artefactoService.findAll();

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

		Page<Artefacto> artefactos = artefactoService.findAll(pageRequest);

		PageRender<Artefacto> pageRender = new PageRender<Artefacto>("/artefacto/listar", artefactos);

		model.addAttribute("titulo", messageSource.getMessage("text.artefacto.listar.titulo", null, locale));
		model.addAttribute("artefactos", artefactos);
		model.addAttribute("page", pageRender);
		model.addAttribute("todosLosItems", todosLosArtefactos);
		model.addAttribute("deviceType", deviceType);

		return "artefacto/listar";
	}

	/**
	 * Método que gestiona la información que se pasa al formulario.
	 * 
	 * @param codigo
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "/artefacto/form", "/artefacto/form/scann/{codigo}", "/browser/artefacto/form",
			"/browser/artefacto/form/scann/{codigo}", "/mobile/artefacto/form", "/mobile/artefacto/form/scann/{codigo}" })
	public String crear(@PathVariable(value = "codigo", required = false) String codigo, Model model, Locale locale,
			Device device) {

		Artefacto artefacto = new Artefacto();

		deviceType = identificaDevices.getDevice(device);

		model.addAttribute("titulo", messageSource.getMessage("text.artefacto.crear.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.artefacto.crear.btn", null, locale));
		model.addAttribute("artefacto", artefacto);
		model.addAttribute("deviceType", deviceType);

		return "artefacto/form";
	}
	
	/**
	 * Método que gestiona la información que se pasa al formulario.
	 * 
	 * @param codigo
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "/artefacto/formModal", "/artefacto/formModal/scann/{codigo}", "/browser/artefacto/formModal",
			"/browser/artefacto/formModal/scann/{codigo}", "/mobile/artefacto/formModal", "/mobile/artefacto/formModal/scann/{codigo}" })
	public String crearModal(@PathVariable(value = "codigo", required = false) String codigo, Model model, Locale locale,
			Device device) {

		Artefacto artefacto = new Artefacto();

		deviceType = identificaDevices.getDevice(device);

		model.addAttribute("titulo", messageSource.getMessage("text.artefacto.crear.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.artefacto.crear.btn", null, locale));
		model.addAttribute("artefacto", artefacto);
		model.addAttribute("deviceType", deviceType);

		return "artefacto/formModal";
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
	@RequestMapping(value = { "/artefacto/form/{id}", "/browser/artefacto/form/{id}", "/mobile/artefacto/form/{id}" })
	public String editar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Model model, Locale locale,
			Device device) {

		Artefacto artefacto = null;

		deviceType = identificaDevices.getDevice(device);

		if (id > 0) {

			artefacto = artefactoService.findOne(id);

			if (artefacto == null) {
				flash.addFlashAttribute("error", "El ID del Artefacto no existe!");
				return "redirect:/artefacto/listar";
			}

		} else {
			flash.addFlashAttribute("error", "El ID del Artefacto no puede ser 0!");
			return "redirect:/artefacto/listar";
		}

		model.addAttribute("titulo", messageSource.getMessage("text.artefacto.editar.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.artefacto.editar.btn", null, locale));
		model.addAttribute("artefacto", artefacto);
		model.addAttribute("deviceType", deviceType);

		return "artefacto/form";
	}

	/**
	 * Método que elimina items de la BD.
	 * 
	 * @param id
	 * @param flash
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/artefacto/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Model model) {

		if (id > 0) {

			List<Receta> recetas = recetaService.findAll();

			for (Receta receta : recetas) {
				List<ArtefactoEnUso> artefactosUtilizados = receta.getArtefactosUtilizados();
				for (ArtefactoEnUso artefactoUtilizado : artefactosUtilizados) {
					if (id == artefactoUtilizado.getArtefactoId()) {
						flash.addFlashAttribute("error",
								"No se puede eliminar un Artefacto, si se usa en una receta. Modifique las recetas antes de eliminar. El Artefacto se ha encontrado en la receta "
										+ receta.getNombre() + " , pero puede que esté en más.");
						return "redirect:/artefacto/listar";
					}
				}
			}
			artefactoService.delete(id);

			flash.addFlashAttribute("success", "Artefacto eliminado con éxito!");

		}
		return "redirect:/artefacto/listar";
	}

	/**
	 * Método que guarda y modifica datos en la BD.
	 * 
	 * @param artefacto
	 * @param result
	 * @param model
	 * @param flash
	 * @param request
	 * @return String
	 */
	@PostMapping(value = { "/artefacto/form", "/browser/artefacto/form", "/mobile/artefacto/form" })
	public String guardar(@Valid Artefacto artefacto, BindingResult result, Model model, RedirectAttributes flash,
			HttpServletRequest request, Locale locale, Device device) {

		deviceType = identificaDevices.getDevice(device);

		if (artefacto.getId() == null) {

			if (result.hasErrors() || existeNombreEnDB(artefacto.getNombre())) {
				if (result.hasErrors()) {
					System.out.println("HAY ERRORES ARTEFACTO: " + result.getFieldError().getDefaultMessage());
					System.out.println("HAY ERRORES ARTEFACTO: " + result.getFieldError().getCode());
					if(artefacto.getNombre().isBlank()) {
						model.addAttribute("deviceType", deviceType);
						model.addAttribute("nombreEnBlanco", true);
						model.addAttribute("nombreError", messageSource.getMessage("NotBlank", null, locale));
					}
				}
				if (existeNombreEnDB(artefacto.getNombre())) {
					System.out.println("HAY ERRORES ARTEFACTO NOMBRE REPETIDO");
					model.addAttribute("deviceType", deviceType);
					model.addAttribute("nombreError", messageSource.getMessage("nombreRepetido", null, locale));
					model.addAttribute("repetido", true);
				}
				model.addAttribute("deviceType", deviceType);
				model.addAttribute("titulo", messageSource.getMessage("text.artefacto.crear.titulo", null, locale));
				model.addAttribute("btnText", messageSource.getMessage("text.artefacto.crear.btn", null, locale));
				return "artefacto/form";
			}

		} else if (result.hasErrors() && artefacto.getId() != null && artefacto.getId() > 0) {
			System.out.println("HAY ERRORES PRODUCTO ID" + result.getFieldError().getDefaultMessage());
			System.out.println("HAY ERRORES PRODUCTO ID" + result.getFieldError().getCode());
			
			model.addAttribute("deviceType", deviceType);
			model.addAttribute("titulo", messageSource.getMessage("text.artefacto.editar.titulo", null, locale));
			model.addAttribute("btnText", messageSource.getMessage("text.artefacto.editar.btn", null, locale));
			return "artefacto/form";
		}

		String mensajeFlash = (artefacto.getId() != null) ? "Artefacto editado con éxito!" : "Artefacto creado con éxito!";

		System.out.println(artefacto.getUnidadDeConsumo().getNombre());

		artefactoService.save(artefacto);
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/artefacto/listar";
	}
	
	public boolean existeNombreEnDB(String nombre) {
		boolean existeNombre = false;
		List<Artefacto> artefactos = (List<Artefacto>) artefactoService.findAll();
		for(Artefacto a:artefactos) {
			if(a.getNombre().equals(nombre)) {
				existeNombre=true;
				break;
			}
		}
		return existeNombre;
	}
	
}
