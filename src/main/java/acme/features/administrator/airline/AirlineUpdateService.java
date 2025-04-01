
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineType;

@GuiService
public class AirlineUpdateService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AirlineRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Airline airline;
		int id = super.getRequest().getData("id", int.class);
		airline = this.repository.findById(id);
		super.getBuffer().addData(airline);
	}

	@Override
	public void bind(final Airline airline) {
		super.bindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "email", "phoneNumber");
	}

	@Override
	public void validate(final Airline airline) {
		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		Airline existing = this.repository.findByIataCode(airline.getIataCode());
		boolean valid = existing == null || existing.getId() == airline.getId();
		super.state(valid, "iataCode", "administrator.airline.form.error.duplicateIata");
	}

	@Override
	public void perform(final Airline airline) {
		this.repository.save(airline);
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
