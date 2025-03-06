
package acme.constraints;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;

@Validator
public class NextInspectionValidator extends AbstractValidator<ValidNextInspection, Date> {

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidNextInspection annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Date nextInspection, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (nextInspection == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			Date minimumNextInspection;

			boolean correctNextInspection;

			minimumNextInspection = MomentHelper.deltaFromCurrentMoment(1, ChronoUnit.HOURS);
			correctNextInspection = MomentHelper.isAfterOrEqual(nextInspection, minimumNextInspection);

			super.state(context, correctNextInspection, "*", "acme.validation.nextInspection.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
