package miRecetApp.app.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import miRecetApp.app.model.entity.Producto;
import miRecetApp.app.service.IProductoService;

/**
 * Clase que establece una regla de validación especial, implementando ConstraintValidator,
 * para verificar que el nuevo Código de Barras introducido no esté ya asignado.
 * 
 * @author Julian Quenard
 * @see ConstraintValidator
 */
public class CodigoBarrasValidador implements ConstraintValidator<CodigoBarras, String> {
	
	@Autowired
	IProductoService productoService;
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if (!value.isBlank() && value !=null) {
			
			Producto mismoCodigo = productoService.findByCodigoDeBarra(value);
			
			if(mismoCodigo != null) {
				return false;
			}
			
		}		
		
		return true;
	}

}
