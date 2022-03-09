package miRecetApp.app.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Anotacion para implementar la regla de validacion especial ArtefactoExisteEnDBValidador
 * @author Julian Quenard
 * @see ArtefactoExisteEnDBValidador
 */
@Constraint(validatedBy = ArtefactoExisteEnDBValidador.class)
@Retention(RUNTIME)
@Target({ElementType.TYPE, FIELD, METHOD })
public @interface ArtefactoExisteEnDB {
	String message() default "Ya existe un artefacto con ese nombre, debe elegir otro.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
