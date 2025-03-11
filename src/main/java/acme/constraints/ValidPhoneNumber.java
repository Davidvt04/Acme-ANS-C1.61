
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation

@NotBlank
@Length(min = 6, max = 16)
@Pattern(regexp = "^\\+?\\d{6,15}$")

public @interface ValidPhoneNumber {

	// Standard validation properties -----------------------------------------

	String message() default "{acme.validation.header.message}";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
