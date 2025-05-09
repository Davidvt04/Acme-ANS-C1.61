
package acme.constraints;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.trackingLog.ClaimStatus;
import acme.entities.trackingLog.TrackingLog;
import acme.features.assistanceAgent.trackingLog.AssistanceAgentTrackingLogRepository;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


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
		else if (trackingLog.getStatus() != null && trackingLog.getResolution() != null && trackingLog.getClaim() != null) {

			if (trackingLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() == 100.0)
				super.state(context, !trackingLog.getStatus().equals(ClaimStatus.PENDING), "Status", "El estado no puede ser PENDING");
			else
				super.state(context, trackingLog.getStatus().equals(ClaimStatus.PENDING), "Status", "El estado debe ser PENDING");

			if (trackingLog.getStatus().equals(ClaimStatus.PENDING))
				super.state(context, trackingLog.getResolution() == null || trackingLog.getResolution().isBlank(), "Resolution", "El campo resolution debe quedar vacío hasta la finalización del tracking log");
			else
				super.state(context, trackingLog.getResolution() != null && !trackingLog.getResolution().isBlank(), "Resolution", "El campo resolucion es incorrecto");

			Optional<List<TrackingLog>> trackingLogsOpt = this.repository.findOrderTrackingLog(trackingLog.getClaim().getId());

		}
		result = !super.hasErrors(context);
		return result;
	}
}
