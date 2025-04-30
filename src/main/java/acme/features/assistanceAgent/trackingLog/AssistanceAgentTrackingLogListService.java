
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);

		if (!super.getRequest().getData().isEmpty()) {
			int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int claimId = super.getRequest().getData("claimId", int.class);
			Claim claim = this.repository.getClaimById(claimId);

			super.getResponse().setAuthorised(agentId == claim.getAssistanceAgent().getId());
		}

	}

	@Override
	public void load() {
		Collection<TrackingLog> trackingLogs;
		int assistanceAgentId;
		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		if (super.getRequest().getData().isEmpty())
			trackingLogs = this.repository.findAllTrackingLogs(assistanceAgentId);
		else {
			int claimId = super.getRequest().getData("claimId", int.class);
			trackingLogs = this.repository.findTrackingLogsByClaimId(claimId);
		}

		super.getBuffer().addData(trackingLogs);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "resolutionPercentage", "status", "step", "resolution");

		super.getResponse().addData(dataset);
	}

}
