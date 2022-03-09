package miRecetApp.app.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import miRecetApp.app.model.entity.Artefacto;
import miRecetApp.app.service.IArtefactoService;



/**
 * Clase que establece una regla de validación especial para verificar que no exista un artefacto con el mismo nombre, 
 * implementando ConstraintValidator.
 * @author Julian Quenard
 * @see ConstraintValidator
 */
public class ArtefactoExisteEnDBValidador implements ConstraintValidator<ArtefactoExisteEnDB, String> {
	
	@Autowired
	IArtefactoService artefactoService;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if (!value.isBlank() && value !=null) {
			
			Artefacto mismoArtefacto = artefactoService.findByNombre(value);
			
			if(mismoArtefacto != null) {
				return false;
			}			
		}		
		
		return true;
		
	}
}


