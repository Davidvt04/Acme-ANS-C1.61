
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.managers.Manager;

@Validator
public class ManagerValidator extends AbstractValidator<ValidManager, Manager> {

	// Internal state ---------------------------------------------------------

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Manager manager, final ConstraintValidatorContext context) {
		// HINT: manager can be null
		assert context != null;

		boolean result;

		if (manager == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			DefaultUserIdentity identity = manager.getIdentity();
			String nombreInicial = identity.getName().substring(0, 1);
			String surname = identity.getSurname();
			String[] palabras = surname.split("\\s");
			String surnameIniciales = "";
			int limite = Math.min(palabras.length, 2);
			for (int i = 0; i < limite; i++)
				if (!palabras[i].isEmpty())
					surnameIniciales += palabras[i].substring(0, 1);
			surnameIniciales = surnameIniciales.toUpperCase();
			String iniciales = nombreInicial + surnameIniciales;
			String identifier = manager.getIdentifier();

			Boolean esCorrectoIdentificador = identifier.startsWith(iniciales);
			super.state(context, esCorrectoIdentificador, "identifierNumber", "Initials Error");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
