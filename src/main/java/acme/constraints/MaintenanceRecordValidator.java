
package acme.constraints;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.maintenanceRecord.MaintenanceRecord;

@Validator
public class MaintenanceRecordValidator extends AbstractValidator<ValidMaintenanceRecord, MaintenanceRecord> {

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidMaintenanceRecord annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final MaintenanceRecord maintenanceRecord, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (maintenanceRecord == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			Date minimumNextInspection;

			boolean correctNextInspection;

			minimumNextInspection = MomentHelper.deltaFromMoment(maintenanceRecord.getMoment(), 1L, ChronoUnit.HOURS);
			correctNextInspection = MomentHelper.isAfterOrEqual(maintenanceRecord.getNextInspectionDueTime(), minimumNextInspection);

			super.state(context, correctNextInspection, "*", "acme.validation.nextInspection.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
