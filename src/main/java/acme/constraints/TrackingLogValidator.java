
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.trackingLog.ClaimStatus;
import acme.entities.trackingLog.TrackingLog;
import acme.entities.trackingLog.TrackingLogRepository;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Autowired
	private TrackingLogRepository repository;


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		boolean result;

		assert context != null;

		if (trackingLog == null)
			super.state(context, false, "TrackingLog", "No hay trackingLogs");
		else {

			if (trackingLog.getResolutionPercentage() == 100.0)
				super.state(context, !trackingLog.getStatus().equals(ClaimStatus.PENDING), "Status", "El estado no puede ser PENDING");
			else
				super.state(context, trackingLog.getStatus().equals(ClaimStatus.PENDING), "Status", "El estado debe ser PENDING");

			if (trackingLog.getStatus().equals(ClaimStatus.PENDING))
				super.state(context, trackingLog.getResolution() == null, "Resolution", "El campo resolucion es incorrecto");
			else
				super.state(context, trackingLog.getResolution() != null, "Resolution", "El campo resolucion es incorrecto");

			List<TrackingLog> trackingLogs;
			trackingLogs = this.repository.findOrderTrackingLog(trackingLog.getClaim().getId());
			Integer pos = trackingLogs.indexOf(trackingLog);
			if (trackingLogs.size() > 1 && pos < trackingLogs.size() - 1)
				super.state(context, trackingLogs.get(pos + 1).getResolutionPercentage() < trackingLog.getResolutionPercentage(), "ResolutionPercentage", "Error el porcentaje debe ser mayor a:" + trackingLogs.get(pos + 1).getResolutionPercentage());

		}
		result = !super.hasErrors(context);
		return result;
	}
}
