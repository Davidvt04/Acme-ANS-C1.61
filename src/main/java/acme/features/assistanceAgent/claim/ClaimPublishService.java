
package acme.features.assistanceAgent.claim;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class ClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Integer claimId;
		Claim claim;
		AssistanceAgent assistanceAgent;
		try {
			if (!super.getRequest().getMethod().equals("POST"))
				super.getResponse().setAuthorised(false);
			else {
				claimId = super.getRequest().getData("id", Integer.class);
				claim = this.repository.findClaimById(claimId);
				assistanceAgent = claim == null ? null : claim.getAssistanceAgent();
				status = super.getRequest().getPrincipal().hasRealm(assistanceAgent);

				if (super.getRequest().hasData("id")) {
					Integer legId = super.getRequest().getData("leg", Integer.class);
					if (legId == null || legId != 0) {
						Leg leg = this.repository.getLegById(legId);
						status = status && leg != null && !leg.isDraftMode();
					}
				}
				super.getResponse().setAuthorised(status);
			}
		} catch (Exception e) {
			super.getResponse().setAuthorised(false);
		}

	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "leg", "id");
	}

	@Override
	public void validate(final Claim claim) {
		boolean valid;
		if (claim.getLeg() != null && claim.getRegistrationMoment() != null) {
			valid = claim.getRegistrationMoment().after(claim.getLeg().getScheduledArrival());
			super.state(valid, "leg", "assistanceAgent.claim.form.error.badLeg");
		}
	}

	@Override
	public void perform(final Claim claim) {
		claim.setDraftMode(false);
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		List<Leg> legs = new ArrayList<>();
		SelectChoices choices;
		SelectChoices choices2;
		Dataset dataset;

		choices = SelectChoices.from(ClaimType.class, claim.getType());
		for (Leg leg : this.repository.findAllLegPublish())
			if (leg.getScheduledArrival().before(claim.getRegistrationMoment()))
				legs.add(leg);
		choices2 = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "leg", "draftMode", "id");
		dataset.put("types", choices);
		dataset.put("legs", choices2);

		super.getResponse().addData(dataset);
	}

}
