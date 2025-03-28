
package acme.features.authenticated.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.managers.Manager;

@GuiService
public class FlightListService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private FlightRepository repository;


	@Override
	public void authorise() {
		// All managers are authorised to list their own flights.
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		// Get the manager's id from the current principal
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Flight> flights = this.repository.findFlightsByManagerId(managerId);
		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Flight flight) {
		// Unbind the main attributes of the flight. These keys will be used in the view.
		Dataset dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
		// Add transient fields for the flight details
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("numberOfLayovers", flight.getNumberOfLayovers());
		super.getResponse().addData(dataset);
	}
}
