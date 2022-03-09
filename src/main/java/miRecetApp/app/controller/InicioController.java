package miRecetApp.app.controller;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import miRecetApp.app.service.implementation.IdentificaDevice;

/**
 * Clase que controla la gestión la ventana de inicio. 
 * 
 * @author Julian Quenard
 */
@Controller
public class InicioController {
	
	@Autowired
	private IdentificaDevice identificaDevices;
	
	@Autowired
	private MessageSource messageSource;
	
	private String deviceType = "browser";
	
	@GetMapping(value = {"/inicio", "/"})
	public String inicio(
			Model model, 
			Locale locale,
			Device device) {
		
		System.out.println("Ingresando a la pantalla de inicio");
		
		deviceType = identificaDevices.getDevice(device);
		
		model.addAttribute("titulo", messageSource.getMessage("text.inicio.titulo", null, locale));
		model.addAttribute("btnCrear", messageSource.getMessage("text.inicio.btn.crear", null, locale));
		model.addAttribute("btnVer", messageSource.getMessage("text.inicio.btn.ver", null, locale));
		model.addAttribute("deviceType", deviceType);
		
		return "inicio";
	}
	
	@GetMapping(value = {"/gastos"})
	public String gastos(
			Model model, 
			Locale locale,
			Device device) {
		
		System.out.println("Ingresando a la pantalla de gastos");
		
		deviceType = identificaDevices.getDevice(device);
		
		model.addAttribute("titulo", messageSource.getMessage("text.inicio.gastos", null, locale));
		model.addAttribute("btnGastosFijos", messageSource.getMessage("text.inicio.btn.gastosFijos", null, locale));
		model.addAttribute("btnGastosConsumo", messageSource.getMessage("text.inicio.btn.gastosConsumo", null, locale));
		model.addAttribute("deviceType", deviceType);
		
		return "gastos/gastos";
	}	
}
