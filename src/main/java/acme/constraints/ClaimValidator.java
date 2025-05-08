
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.entities.claim.Claim;

public class ClaimValidator extends AbstractValidator<ValidClaim, Claim> {

	@Override
	protected void initialise(final ValidClaim annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Claim claim, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;

		if (claim == null)
			super.state(context, false, "Claim", "No hay Claim");
		else if (claim.getLeg() != null && claim.getRegistrationMoment() != null) {

			boolean valid = claim.getRegistrationMoment().after(claim.getLeg().getScheduledArrival());
			super.state(context, valid, "leg", "assistanceAgent.claim.form.error.badLeg");
		}
		result = !super.hasErrors(context);
		return result;
	}

}
