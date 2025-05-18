
package acme.features.assistanceAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackingLog.ClaimStatus;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogDeleteService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		try {
			boolean status;
			TrackingLog trackingLog;
			Integer id;
			AssistanceAgent assistanceAgent;
			if (!super.getRequest().getMethod().equals("POST"))
				super.getResponse().setAuthorised(false);
			else {
				id = super.getRequest().getData("id", Integer.class);
				trackingLog = null;
				if (id != null)
					trackingLog = this.repository.findTrackingLogById(id);
				assistanceAgent = trackingLog == null ? null : trackingLog.getClaim().getAssistanceAgent();
				status = super.getRequest().getPrincipal().hasRealm(assistanceAgent) && (trackingLog == null || trackingLog.isDraftMode());
				super.getResponse().setAuthorised(status);
			}
		} catch (Throwable t) {
			super.getResponse().setAuthorised(false);
		}

	}
	@Override
	public void load() {
		TrackingLog trackingLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}
	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "status", "resolution");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		;
	}

	@Override
	public void perform(final TrackingLog trackingLog) {

		this.repository.delete(trackingLog);
	}
	@Override
	public void unbind(final TrackingLog trackingLog) {

		SelectChoices choices;

		Dataset dataset;

		choices = SelectChoices.from(ClaimStatus.class, trackingLog.getStatus());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "status", "resolution");
		dataset.put("status", choices);

	}
}
