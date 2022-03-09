package miRecetApp.app.controller;

import java.security.Principal;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import miRecetApp.app.service.implementation.IdentificaDevice;

/**
 * Clase que controla la gestión del Logging en la ventana de inicio. 
 * 
 * @author Julian Quenard
 */
@Controller
public class LoginController {
	
	@Autowired
	private IdentificaDevice identificaDevices;
	
	private String deviceType = "browser";
	
	@GetMapping(value = {"/login"})
	public String login(
			@RequestParam(value = "error", required = false) String error, 
			@RequestParam(value = "logout", required = false) String logout, 
			Model model, 
			Principal principal, 
			RedirectAttributes flash,
			Locale locale,
			Device device) {
		
		
		
		System.out.println("TIPO DE DISPOSITIVO: " + identificaDevices.getDevice(device));
		
		deviceType = identificaDevices.getDevice(device);
		
		if(principal!=null) {
			flash.addFlashAttribute("info", "La sesión ya se ha iniciado");
			return "redirect:/inicio"; 			
		}
		
		if(error!=null) {
			model.addAttribute("error",  "¡Nombre de Usario o Contraseña incorrecto!");
		}
		
		if(logout!=null) {
			model.addAttribute("info", "Se ha cerrado la sesión con éxito");
		}
		
		model.addAttribute("titulo", "Inicio de sesión");
		model.addAttribute("deviceType", deviceType);
		
		return "login";
	}	
}
