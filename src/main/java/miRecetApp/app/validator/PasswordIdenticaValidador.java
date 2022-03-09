package miRecetApp.app.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import miRecetApp.app.model.entity.Usuario;

/**
 * Clase que establece una regla de validación especial para la constraseña de la clase Usuarios, 
 * implementando ConstraintValidator.
 * @author Julian Quenard
 * @see ConstraintValidator
 */
public class PasswordIdenticaValidador implements ConstraintValidator<PasswordIdentica, Usuario> {

	@Override
	public boolean isValid(Usuario value, ConstraintValidatorContext context) {
		if(value.getPassword()!=null && 
		   value.getReiteraPassword()!=null &&
		   value.getPassword().equals(value.getReiteraPassword())) {
			return true;
		}
		return false;
	}
}


