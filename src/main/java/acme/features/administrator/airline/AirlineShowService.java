
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
public class AirlineShowService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AirlineRepository airlineRepository;


	@Override
	public void authorise() {
		if (!super.getRequest().getMethod().equals("GET"))
			super.getResponse().setAuthorised(false);
		else
			super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airline airline;
		int id = super.getRequest().getData("id", int.class);
		airline = this.airlineRepository.findById(id);
		super.getBuffer().addData(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		SelectChoices choices = SelectChoices.from(AirlineType.class, airline.getType());

		Dataset dataset = super.unbindObject(airline, "name", "iataCode", "website", "foundationMoment", "email", "phoneNumber");

		dataset.put("type", airline.getType());
		dataset.put("airlineTypes", choices);

		super.getResponse().addData(dataset);
	}
}
