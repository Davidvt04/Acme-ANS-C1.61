
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flightAssignament.FlightAssignament;
import acme.realms.flightCrewMembers.AvailabilityStatus;

@Validator
public class FlightAssignamentValidator extends AbstractValidator<ValidFlightAssignament, FlightAssignament> {

	@Override
	protected void initialise(final ValidFlightAssignament annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightAssignament flightAssignament, final ConstraintValidatorContext context) {
		if (flightAssignament == null)
			return false;
		if (flightAssignament.getFlightCrewMember() == null)
			return false;

		boolean flightCrewMemberAvailable;
		flightCrewMemberAvailable = AvailabilityStatus.AVAILABLE.equals(flightAssignament.getFlightCrewMember().getAvailabilityStatus());
		super.state(context, flightCrewMemberAvailable, "flightCrewMember", "{acme.validation.FlightAssignament.flightCrewMemberNotAvailable.message}");

		return !super.hasErrors(context);
	}

}
