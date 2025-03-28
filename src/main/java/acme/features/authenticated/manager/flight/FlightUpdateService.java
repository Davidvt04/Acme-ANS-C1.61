
package acme.features.authenticated.manager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.managers.Manager;

@GuiService
public class FlightUpdateService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private FlightRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;
		Manager manager;

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findById(flightId);
		manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();

		// Allow update only if:
		// - the flight exists,
		// - the flight is still in draft mode (i.e. not published),
		// - and the flight belongs to the current manager.
		status = flight != null && flight.isDraftMode() && flight.getManager().getId() == manager.getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findById(id);
		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		// Bind the editable fields from the request.
		super.bindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		// You can add additional validation here if needed.
	}

	@Override
	public void perform(final Flight flight) {
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		// Leave unbind as per your desired structure.
		Dataset dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
		// Optionally, add transient fields (to be enhanced with leg services later)
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("numberOfLayovers", flight.getNumberOfLayovers());
		super.getResponse().addData(dataset);
	}
}
