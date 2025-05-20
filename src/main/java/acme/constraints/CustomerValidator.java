
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.UserAccount;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Customer;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {

		if (customer.getIdentifier() == null || customer.getIdentifier().isBlank() || !customer.getIdentifier().matches("^[A-Z]{2,3}\\d{6}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.identifier.invalidIdentifier.message").addConstraintViolation();
			return false;
		}

		UserAccount userAccount = customer.getUserAccount();
		if (userAccount == null || userAccount.getIdentity() == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("").addConstraintViolation();
			return false;
		}
		if (userAccount.getIdentity().getName() == null || userAccount.getIdentity().getName().isBlank()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("").addConstraintViolation();
			return false;
		}

		if (userAccount.getIdentity().getSurname() == null || userAccount.getIdentity().getSurname().isBlank()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("").addConstraintViolation();
			return false;
		}

		String nombre = userAccount.getIdentity().getName();
		String[] apellidos = userAccount.getIdentity().getSurname().split(" ");
		String inicialNombre = String.valueOf(nombre.charAt(0)).toUpperCase();
		String inicial1Apellido = String.valueOf(apellidos[0].charAt(0)).toUpperCase();
		String inicial2Apellido = "";
		int initialsLenght = 2;
		if (apellidos.length > 1) {
			inicial2Apellido = String.valueOf(apellidos[1].charAt(0)).toUpperCase();
			initialsLenght = 3;
		}
		String iniciales = inicialNombre + inicial1Apellido + inicial2Apellido;

		String identifierInitials = customer.getIdentifier().substring(0, initialsLenght);

		if (!iniciales.equals(identifierInitials)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.lastNibble.startIdentifier.message").addConstraintViolation();
			return false;
		}
		return true;
	}

}
