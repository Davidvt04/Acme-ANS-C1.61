
package acme.features.manager.flight;

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
		// Only the manager may list their own flights
		boolean isManager = super.getRequest().getPrincipal().hasRealmOfType(Manager.class);
		super.getResponse().setAuthorised(isManager);
	}

	@Override
	public void load() {
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Flight> flights = this.repository.findFlightsByManagerId(managerId);
		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");

		// Safely add transient fields
		if (flight.getScheduledDeparture() != null)
			dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		if (flight.getScheduledArrival() != null)
			dataset.put("scheduledArrival", flight.getScheduledArrival());

		// Handle possible null airports
		if (flight.getOriginAirport() != null)
			dataset.put("originCity", flight.getOriginAirport().getCity());
		else
			dataset.put("originCity", "");
		if (flight.getDestinationAirport() != null)
			dataset.put("destinationCity", flight.getDestinationAirport().getCity());
		else
			dataset.put("destinationCity", "");

		dataset.put("numberOfLayovers", flight.getNumberOfLayovers());
		super.getResponse().addData(dataset);
	}
}
