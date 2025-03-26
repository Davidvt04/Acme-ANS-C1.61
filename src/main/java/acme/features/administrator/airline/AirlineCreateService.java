
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineType;

@GuiService
public class AirlineCreateService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AirlineRepository airlineRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airline airline = new Airline();
		airline.setName("");
		airline.setIataCode("");
		airline.setWebsite("");
		airline.setType(AirlineType.STANDARD);
		airline.setFoundationMoment(MomentHelper.getCurrentMoment());
		airline.setEmail("");
		airline.setPhoneNumber("");

		super.getBuffer().addData(airline);
	}

	@Override
	public void bind(final Airline airline) {
		super.bindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "email", "phoneNumber");
	}

	@Override
	public void validate(final Airline airline) {
	}

	@Override
	public void perform(final Airline airline) {
		this.airlineRepository.save(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset;
		dataset = super.unbindObject(airline, "name", "iataCode", "website");

		Object foundationMoment = airline.getFoundationMoment();
		dataset.put("foundationMoment", new Object[] {
			foundationMoment
		});

		super.addPayload(dataset, airline, "type", "email", "phoneNumber");

		SelectChoices choices = SelectChoices.from(AirlineType.class, airline.getType());
		dataset.put("airlineTypes", choices);

		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}
}
