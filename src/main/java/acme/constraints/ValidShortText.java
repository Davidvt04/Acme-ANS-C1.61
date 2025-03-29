
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({
	ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ShortTextValidator.class)
public @interface ValidShortText {

	// Standard validation properties -----------------------------------------

	String message() default "{acme.validation.header.message}";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
