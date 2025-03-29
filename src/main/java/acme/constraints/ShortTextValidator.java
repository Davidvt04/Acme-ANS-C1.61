
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class ShortTextValidator extends AbstractValidator<ValidShortText, String> {

	@Override
	protected void initialise(final ValidShortText annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String shortText, final ConstraintValidatorContext context) {

		assert context != null;

		if (shortText == null || shortText.isBlank() || shortText.length() <= 50 && shortText.length() > 0)
			return true;
		else
			return false;

	}

}
