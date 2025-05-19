
package acme.features.manager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.managers.Manager;

@GuiService
public class FlightCreateService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private FlightRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		String method = super.getRequest().getMethod();
		if (method.equals("GET") && super.getRequest().hasData("id", int.class))
			status = false;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		// Retrieve the current manager from the active realm.
		Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();

		// Create a new flight and set default values.
		Flight flight = new Flight();
		flight.setTag("");
		flight.setRequiresSelfTransfer(false);
		// Create a default Money value, e.g. 0.0 USD (adjust as needed)
		flight.setCost(new Money());
		flight.getCost().setAmount(0.0);
		flight.getCost().setCurrency("USD");
		flight.setDescription("");
		flight.setDraftMode(true);
		// Associate the flight with the current manager.
		flight.setManager(manager);

		// Add the new flight to the buffer.
		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		// Add any flight-specific validation here, e.g., check that required fields are not blank.
		// For now, we leave it empty.
	}

	@Override
	public void perform(final Flight flight) {

		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
		super.getResponse().addData(dataset);
	}
}
