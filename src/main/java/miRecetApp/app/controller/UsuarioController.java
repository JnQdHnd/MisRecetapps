package miRecetApp.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import miRecetApp.app.model.entity.Usuario;
import miRecetApp.app.service.implementation.IdentificaDevice;
import miRecetApp.app.service.implementation.UsuarioService;
import miRecetApp.app.model.dao.IRoleDao;
import miRecetApp.app.model.dao.IUsuarioDao;
import miRecetApp.app.model.entity.Role;

/**
 * Clase que controla la creación de Usuarios en la vista. 
 * 
 * @author Julian Quenard
 */
@Controller
public class UsuarioController {
	
	//ATRIBUTOS
	
	@Autowired
	private IRoleDao roleDao;
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private IdentificaDevice identificaDevices;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private String deviceType = "browser";
	
	//METODOS
	
	/**
	 * Método que gestiona la información que se pasa al formulario.
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = {"/usuario/form", "/browser/usuario/form", "/mobile/usuario/form"})
	public String crear(Model model, Locale locale, Device device) {		
		boolean errorEnPassword = false;
		deviceType = identificaDevices.getDevice(device);
		
		Usuario usuario = new Usuario();		
		
		System.out.println("Llamando formulario de creacion de Usuario");
		
		if(((List<Role>)roleDao.findAll()).isEmpty()) {
			Role roleA = new Role();
			roleA.setAuthority("ROLE_ADMIN");
			roleDao.save(roleA);
			Role roleU = new Role();
			roleU.setAuthority("ROLE_USER");
			roleDao.save(roleU);			
		}
		
		model.addAttribute("errorEnPassword", errorEnPassword);
		model.addAttribute("titulo", messageSource.getMessage("text.usuario.crear.titulo", null, locale));
		model.addAttribute("btnText", messageSource.getMessage("text.usuario.crear.btn", null, locale));
		model.addAttribute("usuario", usuario);
		model.addAttribute("deviceType", deviceType);
		return "usuario/form";	
	}
	
	@PostMapping(value = {"/usuario/form", "/browser/usuario/form", "/mobile/usuario/form"})
	public String guardar(
			@Valid Usuario usuario, 
			BindingResult result, 
			Model model, 
			RedirectAttributes flash, 
			Locale locale, 
			Device device) {
		
		boolean errorEnPassword = false;
		deviceType = identificaDevices.getDevice(device);
		
		if(result.hasErrors() && usuario.getId()==null) {
			
			System.out.println("HAY ERRORES CREANDO USUARIO");
			
			List<ObjectError> errores = result.getAllErrors();
			for (ObjectError error:errores) {
				if(error.getCode().equals("PasswordIdentica")) {
					errorEnPassword = true;
				}
				System.out.println(error.getDefaultMessage() + " - " + error.getObjectName() + " - " + error.getCode());
			}
			System.out.println("ErrorEnPasword: " + errorEnPassword);
			
			model.addAttribute("errorEnPassword", errorEnPassword);
			model.addAttribute("titulo", messageSource.getMessage("text.usuario.crear.titulo", null, locale));
			model.addAttribute("btnText", messageSource.getMessage("text.usuario.crear.btn", null, locale));
			return "usuario/form";
		}
		
		else if(result.hasErrors() && usuario.getId()!=null && usuario.getId()>0) {
			System.out.println("HAY ERRORES MODIFICANDO USUARIO");
			
			List<ObjectError> errores = result.getAllErrors();
			for (ObjectError error:errores) {
				if(error.getCode().equals("PasswordIdentica")) {
					errorEnPassword = true;
				}
				System.out.println(error.getDefaultMessage() + " - " + error.getObjectName() + " - " + error.getCode());
			}
			System.out.println("ErrorEnPasword: " + errorEnPassword);
			
			model.addAttribute("errorEnPassword", errorEnPassword);
			model.addAttribute("titulo", messageSource.getMessage("text.usuario.editar.titulo", null, locale));
			model.addAttribute("btnText", messageSource.getMessage("text.usuario.editar.btn", null, locale));
			return "usuario/form";
		}
			
		String mensajeFlash = (usuario.getId() != null)?"Usuario editado con éxito!" : "Usuario creado con éxito!";
		
		if(usuario.getRoles()==null) {
			List<Role> roles = new ArrayList<>();
			roles.add(roleDao.findByAuthority("ROLE_USER"));
			usuario.setRoles(roles);
		}
		
		usuario.setEnabled(true);
		usuarioService.saveUser(usuario);
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/login";		
	}
	
	public boolean existeNombreEnDB(String nombre) {
		boolean existeUsuario = false;
		List<Usuario> usuarios = (List<Usuario>) usuarioDao.findAll();
		for(Usuario u:usuarios) {
			if(u.getUsername().equals(nombre)) {
				existeUsuario=true;
				break;
			}
		}
		return existeUsuario;
	}

}
