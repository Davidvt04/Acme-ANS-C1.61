
package acme.constraints;

import java.util.Calendar;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.service.Service;

@Validator
public class PromotionCodeValidator extends AbstractValidator<ValidPromotionCode, Service> {

	@Override
	protected void initialise(final ValidPromotionCode annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Service service, final ConstraintValidatorContext context) {
		boolean result;
		assert context != null;
		if (service == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			Date currentMoment = MomentHelper.getCurrentMoment();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentMoment);
			Integer currentYear = calendar.get(Calendar.YEAR);
			String last2DigitsYear = String.valueOf(currentYear).substring(2);

			if (service.getPromotionCode() != null && service.getPromotionCode().length() >= 2) {
				String last2DigirsCode = service.getPromotionCode().substring(service.getPromotionCode().length() - 2);
				super.state(context, last2DigirsCode.equals(last2DigitsYear), "PromotionCode", "acme.validation.promotion-code.not-current-year");
			}
		}

		result = !super.hasErrors(context);
		return result;

	}

}
