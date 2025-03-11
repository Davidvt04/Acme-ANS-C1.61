
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.UserAccount;
import acme.realms.Customer;

public class CustomerIdentifierValidator implements ConstraintValidator<ValidCustomerIdentifier, String> {

	private Customer customer;


	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		if (value == null || value.isBlank() || !value.matches("^[A-Z]{2,3}\\\\d{6}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("The identifier must not be null or blank and must follow the pattern").addConstraintViolation();
			return false;
		}

		if (this.customer == null) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Customer must not be null").addConstraintViolation();
			return false;
		}

		UserAccount userAccount = this.customer.getUserAccount();
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
			context.buildConstraintViolationWithTemplate("User Name must be fullfilled").addConstraintViolation();
			return false;
		}

		String nombre = userAccount.getIdentity().getName();
		String apellidos = userAccount.getIdentity().getSurname();
		String inicialNombre = String.valueOf(nombre.charAt(0)).toUpperCase();
		String inicial1Apellido = String.valueOf(apellidos.charAt(0)).toUpperCase();
		String inicial2Apellido = "";
		int initialsLenght = 2;
		if (apellidos.contains(" ")) {
			inicial2Apellido = String.valueOf(apellidos.split(" ")[1].charAt(0)).toUpperCase();
			initialsLenght = 3;
		}
		String iniciales = inicialNombre + inicial1Apellido + inicial2Apellido;

		String identifierInitials = value.substring(0, initialsLenght);

		if (!iniciales.equals(identifierInitials)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Identifier must start with initials of the user").addConstraintViolation();
			return false;
		}
		return true;
	}

}
