
package acme.features.authenticated.aircraft;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;

@GuiService
public class AircraftCreateService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AircraftRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Aircraft aircraft = new Aircraft();

		super.getBuffer().addData(aircraft);

	}

	@Override
	public void bind(final Aircraft aircraft) {
		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "status", "cargoWeight", "details", "airline");
	}

	@Override
	public void validate(final Aircraft aircraft) {
		;
	}

	@Override
	public void perform(final Aircraft aircraft) {

		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset;

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "status", "cargoWeight", "details", "airline");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
