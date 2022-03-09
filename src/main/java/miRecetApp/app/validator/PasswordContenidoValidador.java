package miRecetApp.app.validator;

import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

/**
 * Clase que establece una regla de validación especial para el contenido de la constraseña de la clase Usuarios, 
 * implementando ConstraintValidator y utilizando la API Passay.
 * La contraseña deberá contener un determinado numero de caracteres, mayúsculas y minúsculas, símbolos no alfanuméricos y números.
 * @author Julian Quenard
 * @see ConstraintValidator
 * @see www.passay.org
 */
public class PasswordContenidoValidador implements ConstraintValidator<PasswordContenido, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
            // at least 8 characters
            new LengthRule(6, 30000),

            // at least one upper-case character
            new CharacterRule(EnglishCharacterData.UpperCase, 1),

            // at least one lower-case character
            new CharacterRule(EnglishCharacterData.LowerCase, 1),

            // at least one digit character
            new CharacterRule(EnglishCharacterData.Digit, 1),

            // at least one symbol (special character)
            new CharacterRule(EnglishCharacterData.Special, 1),

            // no whitespace
            new WhitespaceRule()

        ));
        
        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }

//		  BLOQUE DE CODIGO PARA PASAR LOS ERRORES COMETIDOS AL GENERAL LA CONSTRASEÑA EN VEZ DEL MENSAJE PREDETERMINADO EN LA INTERFACE @PasswordContenido.        
//        List<String> messages = validator.getMessages(result);
//        String messageTemplate = messages.stream()
//            .collect(Collectors.joining(","));
//        context.buildConstraintViolationWithTemplate(messageTemplate)
//            .addConstraintViolation()
//            .disableDefaultConstraintViolation();
        
        return false;
    }
}


