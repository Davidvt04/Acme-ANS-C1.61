
package acme.features.administrator.airport;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;

@GuiService
public class AdministratorAirportListService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	private AdministratorAirportRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Airport> airports;
		airports = this.repository.getAllAirports();
		super.getBuffer().addData(airports);
	}

	@Override
	public void unbind(final Airport airport) {
		assert airport != null;

		Dataset dataset;

		dataset = super.unbindObject(airport, "name", "iataCode", "city", "country");

		super.getResponse().addData(dataset);
	}

}
