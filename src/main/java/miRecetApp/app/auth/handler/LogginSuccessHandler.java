package miRecetApp.app.auth.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;


/**
 *Clase que maneja el Loggin en la vista principal de acceso a la aplicación.
 *
 *@author Julian Quenard
 *01-09-2021
 */

@Component
public class LogginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	/**
	 *Este método maneja el Loggeo en la página principal del proyecto, 
	 *autenticando mediante Spring Security al usuario y su rol asignado.
	 *
	 *@throws IOException
	 *@throws ServletException
	 *@param HttpServletRequest request
	 *@param HttpServletResponse response
	 *@param Authentication authentication
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
		
		FlashMap flashMap = new FlashMap();
		
		flashMap.put("success", "¡Hola " + authentication.getName() + ", has iniciado sesión con éxito!");
		
		flashMapManager.saveOutputFlashMap(flashMap, request, response);
		
		super.onAuthenticationSuccess(request, response, authentication);
		
	}
}
