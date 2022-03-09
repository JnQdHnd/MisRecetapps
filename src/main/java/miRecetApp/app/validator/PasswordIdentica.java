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
 * Anotacion para implementar la regla de validacion especial PasswordValidador
 * @author Julian Quenard
 * @see PasswordIdenticaValidador
 */
@Constraint(validatedBy = PasswordIdenticaValidador.class)
@Retention(RUNTIME)
@Target({ElementType.TYPE, FIELD, METHOD })
public @interface PasswordIdentica {
	String message() default "Las contraseñas ingresadas deben ser iguales.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
