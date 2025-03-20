
package acme.features.authenticated.airport;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;

@GuiService
public class AirportListService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	private AirportRepository airportRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Airport> airports;
		airports = this.airportRepository.findAll().stream().map(e -> (Airport) e).collect(Collectors.toList());
		super.getBuffer().addData(airports);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;

		dataset = super.unbindObject(airport, "name", "iataCode", "city", "country");
		super.addPayload(dataset, airport, "operationalScope", "website", "email", "phoneNumber");

		super.getResponse().addData(dataset);
	}

}
