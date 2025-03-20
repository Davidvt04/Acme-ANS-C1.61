
package acme.features.authenticated.airport;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;

@GuiService
public class AirportUpdateService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	private AirportRepository airportRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airport airport;
		int id;

		id = super.getRequest().getData("id", int.class);
		airport = (Airport) this.airportRepository.findById(id).get();

		super.getBuffer().addData(airport);
	}

	@Override
	public void bind(final Airport airport) {
		super.bindObject(airport, "name", "iataCode", "city", "country", "operationalScope", "website", "email", "phoneNumber");
	}

	@Override
	public void validate(final Airport airport) {
		;
	}

	@Override
	public void perform(final Airport airport) {
		this.airportRepository.save(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;

		dataset = super.unbindObject(airport, "name", "iataCode", "city", "country", "operationalScope", "website", "email", "phoneNumber");

		super.getResponse().addData(dataset);
	}

}
