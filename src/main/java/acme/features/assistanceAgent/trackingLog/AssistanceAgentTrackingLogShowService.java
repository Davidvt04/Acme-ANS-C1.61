
package acme.features.assistanceAgent.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackingLog.ClaimStatus;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		try {
			boolean status;
			TrackingLog trackingLog;
			Integer id;
			AssistanceAgent assistanceAgent;
			if (!super.getRequest().getMethod().equals("GET"))
				super.getResponse().setAuthorised(false);
			else {
				id = super.getRequest().getData("id", Integer.class);
				trackingLog = null;
				if (id != null)
					trackingLog = this.repository.findTrackingLogById(id);
				assistanceAgent = trackingLog == null ? null : trackingLog.getClaim().getAssistanceAgent();
				status = super.getRequest().getPrincipal().hasRealm(assistanceAgent);
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
		//System.out.println(trackingLog.getStep());
		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {

		SelectChoices statusChoices;
		Claim claim = this.repository.findClaimByTrackingLogId(trackingLog.getId());

		Dataset dataset;

		statusChoices = SelectChoices.from(ClaimStatus.class, trackingLog.getStatus());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "status", "resolution", "draftMode");
		dataset.put("statusChoices", statusChoices);

		dataset.put("masterId", claim.getId());

		super.getResponse().addData(dataset);

	}

}
