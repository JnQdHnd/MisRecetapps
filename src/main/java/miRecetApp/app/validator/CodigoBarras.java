package miRecetApp.app.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Anotacion para implementar la regla de validacion especial SeleccionaProductoValidador
 * @author Julian Quenard
 * @see SeleccionaProductoValidador
 */
@Constraint(validatedBy = CodigoBarrasValidador.class)
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface CodigoBarras {
	String message() default "El código indicado está asignado a otro Producto.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
