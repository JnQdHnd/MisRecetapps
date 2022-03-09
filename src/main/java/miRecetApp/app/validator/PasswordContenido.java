package miRecetApp.app.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Anotacion para implementar la regla de validacion especial PasswordValidador
 * @author Julian Quenard
 * @see PasswordIdenticaValidador
 */
@Constraint(validatedBy = PasswordContenidoValidador.class)
@Retention(RUNTIME)
@Target({FIELD, METHOD })
public @interface PasswordContenido {
	String message() default "La contraseña debe estar compuesta por al menos 8 caracteres, entre los que debe haber al menos: 1 número, 1 símbolo no alfanumérico, letras mayúsculas y minísculas.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
