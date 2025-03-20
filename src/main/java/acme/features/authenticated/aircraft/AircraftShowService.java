
package acme.features.authenticated.aircraft;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;

@GuiService
public class AircraftShowService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AircraftRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	@Override
	public void load() {
		Aircraft aircraft;
		int id = super.getRequest().getData("id", int.class);
		aircraft = (Aircraft) this.repository.findById(id).get();
		super.getBuffer().addData(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset;

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "status", "cargoWeight", "details", "airline");

		super.getResponse().addData(dataset);
	}
}
