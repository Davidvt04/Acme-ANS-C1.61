
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
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
		/*
		 * if (!super.getRequest().getData().isEmpty()) {
		 * int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		 * int claimId = super.getRequest().getData("claimId", int.class);
		 * Claim claim = this.repository.getClaimById(claimId);
		 * 
		 * super.getResponse().setAuthorised(agentId == claim.getAssistanceAgent().getId());
		 * }
		 */
	}

	@Override

	public void load() {
		Collection<TrackingLog> trackingLogs;
		int assistanceAgentId;
		Integer masterId = null;

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (!super.getRequest().getData().isEmpty()) {
			masterId = super.getRequest().getData("masterId", int.class);
			trackingLogs = this.repository.findTrackingLogsByMasterId(masterId);
		} else
			trackingLogs = this.repository.findAllTrackingLogs(assistanceAgentId);

		super.getBuffer().addData(trackingLogs);

		if (masterId != null)
			super.getResponse().addGlobal("masterId", masterId);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		Integer masterId;

		masterId = super.getRequest().getData("masterId", Integer.class);
		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "resolutionPercentage", "status", "step", "resolution");

		if (masterId != null)
			super.getResponse().addGlobal("masterId", masterId);

		super.getResponse().addData(dataset);
	}

}
