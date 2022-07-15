package miRecetApp.app.model.view.json;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Julián Quenard *
 * 15 jul. 2022
 */
@Component("receta/verRecetaConPreparaciones.json")
public class RecetaConPreparacionJson extends MappingJackson2JsonView {
	
	protected String modelKey;
	
	@Override
	protected Object filterModel(Map<String, Object> model) {
		System.out.println("Filtrando Modelo en JSON");
		
		model.remove("titulo");
		model.remove("productos");
		model.remove("receta");
		model.remove("recetaImportada");
		model.remove("artefactos");
		model.remove("costoPorcion");
		model.remove("costoTotal");
		model.remove("deviceType");
		model.remove("preparaciones");
		model.remove("ingredientesSinRepetir");
		
		return super.filterModel(model);
	}

	protected RecetaConPreparacionJson(@Autowired ObjectMapper objectMapper) {	
		super(objectMapper);
		setExtractValueFromSingleKeyModel(true);
		System.out.println("Creando JSON ################################");
	}	
 
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String titulo = (String) model.get("titulo");
		System.out.println(titulo);
		String attachment = "attachment; filename=";
		response.setHeader("Content-Disposition", attachment + titulo + " - MisRecetapps.json");
		response.setContentType(getContentType());
		super.renderMergedOutputModel(model, request, response);
	}
 
	@Override
	public void setModelKey(String modelKey) {
		this.modelKey = modelKey;
	}	
}
