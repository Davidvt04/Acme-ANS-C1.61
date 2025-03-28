
package acme.features.authenticated.manager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.managers.Manager;

@GuiService
public class FlightShowService extends AbstractGuiService<Manager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;

		// 1. Retrieve the flight id from the request
		flightId = super.getRequest().getData("id", int.class);

		// 2. Retrieve the flight from the repository
		flight = this.repository.findById(flightId);

		// 3. Check that the flight exists and belongs to the current manager
		if (flight != null) {
			// Get the active realm from the principal and cast to Manager
			int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			status = flight.getManager().getId() == managerId;
		} else
			status = false;

		// 4. Set authorised flag accordingly
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		// 1. Retrieve the flight id from the request
		int id = super.getRequest().getData("id", int.class);

		// 2. Retrieve the flight from the repository
		Flight flight = this.repository.findById(id);

		// 3. Add the flight to the buffer
		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		// 1. Convert the flight fields into a Dataset
		Dataset dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");

		// 2. Add transient fields (scheduled departure/arrival, origin/destination, layovers)
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("numberOfLayovers", flight.getNumberOfLayovers());

		// 3. Add the dataset to the response
		super.getResponse().addData(dataset);
	}
}
