
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackingLog.ClaimStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class CompletedClaimListService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private ClaimRepository repository;


	@Override
	public void authorise() {
		try {
			boolean status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

			super.getResponse().setAuthorised(status);
			if (!super.getRequest().getMethod().equals("GET"))
				super.getResponse().setAuthorised(false);
		} catch (Throwable t) {
			super.getResponse().setAuthorised(false);
		}
	}

	@Override
	public void load() {
		Collection<Claim> claims;
		int assistanceAgentId;

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claims = this.repository.findClaimsByAssistanceAgent(assistanceAgentId).stream().filter(x -> x.getStatus() != ClaimStatus.PENDING).toList();
		super.getBuffer().addData(claims);
	}

	@Override
	public void unbind(final Claim claim) {

		Dataset dataset;
		ClaimStatus indicator = claim.getStatus();

		dataset = super.unbindObject(claim, "passengerEmail", "type");
		dataset.put("indicator", indicator);
		super.addPayload(dataset, claim, "registrationMoment", "description", "leg.flightNumber");

		super.getResponse().addData(dataset);
	}
}
