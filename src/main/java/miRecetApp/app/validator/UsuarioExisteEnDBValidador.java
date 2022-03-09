package miRecetApp.app.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import miRecetApp.app.model.dao.IUsuarioDao;
import miRecetApp.app.model.entity.Usuario;

/**
 * Clase que establece una regla de validación especial para la constraseña de la clase Usuarios, 
 * implementando ConstraintValidator.
 * @author Julian Quenard
 * @see ConstraintValidator
 */
public class UsuarioExisteEnDBValidador implements ConstraintValidator<UsuarioExisteEnDB, String> {
	
	@Autowired
	IUsuarioDao usuarioDao;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if (!value.isBlank() && value !=null) {
			
			Usuario mismoUsuario = usuarioDao.findByUsername(value);
			
			if(mismoUsuario != null) {
				return false;
			}			
		}		
		
		return true;
		
	}
}


