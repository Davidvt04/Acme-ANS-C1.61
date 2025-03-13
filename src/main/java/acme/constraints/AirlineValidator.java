package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.airline.Airline;

@Validator
public class AirlineValidator extends AbstractValidator<ValidAirline, Airline> {

    @Override
    protected void initialise(final ValidAirline annotation) {
        assert annotation != null;
    }

    @Override
    public boolean isValid(final Airline airline, final ConstraintValidatorContext context) {
        if (airline == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Airline must not be null")
                   .addConstraintViolation();
            return false;
        }
        boolean valid = true;
        String iata = airline.getIataCode();
        if (iata == null || iata.isBlank() || !iata.matches("^[A-Z]{3}$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("IATA code must be 3 uppercase letters")
                   .addPropertyNode("iataCode")
                   .addConstraintViolation();
            valid = false;
        }
        return valid;
    }
}
