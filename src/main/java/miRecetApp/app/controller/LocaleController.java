package miRecetApp.app.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Clase que controla la de idiomas. 
 * 
 * @author Julian Quenard
 * 01-09-2021
 */

@Controller
public class LocaleController {
	
	@PostMapping ("/locale")
	public String locale(HttpServletRequest request) {
		String ultimaUrl = request.getHeader("referer");
		
		return "redirect:".concat(ultimaUrl);
	}
}
