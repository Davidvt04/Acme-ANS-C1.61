
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.leg.Leg;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		if (leg == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Leg must not be null").addConstraintViolation();
			return false;
		}

		String flightNumber = leg.getFlightNumber();
		if (flightNumber == null || flightNumber.isBlank() || !flightNumber.matches("^[A-Z]{3}\\d{4}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Flight number must be composed of 3 uppercase letters followed by 4 digits").addPropertyNode("flightNumber").addConstraintViolation();
			return false;
		}

		if (leg.getAirline() == null || leg.getAirline().getIataCode() == null || leg.getAirline().getIataCode().isBlank()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Airline and its IATA code must not be null or blank").addPropertyNode("airline").addConstraintViolation();
			return false;
		}

		String airlineIata = leg.getAirline().getIataCode();
		String flightNumberPrefix = flightNumber.substring(0, 3);
		if (!airlineIata.equals(flightNumberPrefix)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Flight number must start with the airline's IATA code: expected " + airlineIata + ", but found " + flightNumberPrefix).addPropertyNode("flightNumber").addConstraintViolation();
			return false;
		}

		return true;
	}
}
