
package acme.features.administrator.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackingLog.ClaimStatus;

@GuiService
public class AdministratorClaimListService extends AbstractGuiService<Administrator, Claim> {

	@Autowired
	private AdministratorClaimRepository repository;


	@Override
	public void authorise() {

		if (!super.getRequest().getMethod().equals("GET"))
			super.getResponse().setAuthorised(false);
		else
			super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Claim> claims = this.repository.findAllPublishedClaims();
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
