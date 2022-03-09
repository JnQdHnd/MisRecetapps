package miRecetApp.app.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import miRecetApp.app.model.entity.Producto;
import miRecetApp.app.service.IProductoService;


/**
 * Clase que establece una regla de validación especial para la constraseña de la clase Usuarios, 
 * implementando ConstraintValidator.
 * @author Julian Quenard
 * @see ConstraintValidator
 */
public class ProductoExisteEnDBValidador implements ConstraintValidator<ProductoExisteEnDB, String> {
	
	@Autowired
	IProductoService productoService;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if (!value.isBlank() && value !=null) {
			
			Producto mismoProducto = productoService.findByNombre(value);
			
			if(mismoProducto != null) {
				return false;
			}			
		}		
		
		return true;
		
	}
}


