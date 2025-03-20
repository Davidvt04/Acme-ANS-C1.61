
package acme.features.authenticated.aircraft;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;

@GuiService
public class AircraftListService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AircraftRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	@Override
	public void load() {
		Collection<Aircraft> aircrafts;
		aircrafts = this.repository.findAll().stream().map(e -> (Aircraft) e).collect(Collectors.toList());
		super.getBuffer().addData(aircrafts);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset;
		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "status", "airline");
		super.addPayload(dataset, aircraft, "cargoWeight", "details");

		super.getResponse().addData(dataset);
	}

}
