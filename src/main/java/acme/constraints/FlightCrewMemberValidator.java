
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.UserAccount;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.flightCrewMembers.FlightCrewMember;

@Validator
public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMember> {

	@Override
	protected void initialise(final ValidFlightCrewMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightCrewMember flightCrewMember, final ConstraintValidatorContext context) {
		if (flightCrewMember == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("FlightCrewMember must not be null").addConstraintViolation();
			return false;
		}

		if (flightCrewMember.getEmployeeCode() == null || flightCrewMember.getEmployeeCode().isBlank() || !flightCrewMember.getEmployeeCode().matches("^[A-Z]{2,3}\\d{6}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("The identifier must not be null or blank and must follow the pattern").addConstraintViolation();
			return false;
		}

		UserAccount userAccount = flightCrewMember.getUserAccount();
		if (userAccount == null || userAccount.getIdentity() == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("User Account and Identity must not be null").addConstraintViolation();
			return false;
		}

		if (userAccount.getIdentity().getName() == null || userAccount.getIdentity().getName().isBlank()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("User Name must be fullfilled").addConstraintViolation();
			return false;
		}

		if (userAccount.getIdentity().getSurname() == null || userAccount.getIdentity().getSurname().isBlank()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("User Surname must be fullfilled").addConstraintViolation();
			return false;
		}

		String nombre = userAccount.getIdentity().getName();
		String[] apellidos = userAccount.getIdentity().getSurname().split(" ");

		String inicialNombre = nombre.isEmpty() ? "" : String.valueOf(nombre.charAt(0)).toUpperCase();
		String inicial1Apellido = apellidos.length > 0 ? String.valueOf(apellidos[0].charAt(0)).toUpperCase() : "";
		String inicial2Apellido = apellidos.length > 1 ? String.valueOf(apellidos[1].charAt(0)).toUpperCase() : "";

		String iniciales = inicialNombre + inicial1Apellido + inicial2Apellido;

		String identifierInitials = flightCrewMember.getEmployeeCode().substring(0, iniciales.length());

		if (!iniciales.equals(identifierInitials)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Identifier must start with initials of the user: Should be " + iniciales + " but is " + identifierInitials).addConstraintViolation();
			return false;
		}
		return true;
	}

}
