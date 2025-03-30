
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class LongTextValidator extends AbstractValidator<ValidLongText, String> {

	@Override
	protected void initialise(final ValidLongText annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String longText, final ConstraintValidatorContext context) {

		assert context != null;

		if (longText == null || longText.isBlank() || longText.length() <= 255 && longText.length() > 0)
			return true;
		else
			return false;

	}

}
