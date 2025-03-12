
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.UserAccount;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.AssistanceAgent;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {

		if (assistanceAgent.getEmployeeCode() == null || assistanceAgent.getEmployeeCode().isBlank() || !assistanceAgent.getEmployeeCode().matches("^[A-Z]{2,3}\\d{6}$")) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("The employee code must not be null or blank and must follow the pattern").addConstraintViolation();
			return false;
		}

		UserAccount userAccount = assistanceAgent.getUserAccount();
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

		String identifierInitials = assistanceAgent.getEmployeeCode().substring(0, initialsLenght);

		if (!iniciales.equals(identifierInitials)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Identifier must start with initials of the user: Should be " + identifierInitials + " but is " + iniciales).addConstraintViolation();
			return false;
		}
		return true;
	}

}
