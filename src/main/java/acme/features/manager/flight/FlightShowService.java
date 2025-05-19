
package acme.features.manager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.managers.Manager;

@GuiService
public class FlightShowService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private FlightRepository repository;


	@Override
	public void authorise() {
		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findById(flightId);
		boolean status = flight != null && flight.getManager().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findById(id);
		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode");

		// Transient dates
		if (flight.getScheduledDeparture() != null)
			dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		if (flight.getScheduledArrival() != null)
			dataset.put("scheduledArrival", flight.getScheduledArrival());

		// Origin and destination cities
		if (flight.getOriginAirport() != null)
			dataset.put("originCity", flight.getOriginAirport().getCity());
		else
			dataset.put("originCity", "");
		if (flight.getDestinationAirport() != null)
			dataset.put("destinationCity", flight.getDestinationAirport().getCity());
		else
			dataset.put("destinationCity", "");

		// Layovers and draft
		if (flight.getNumberOfLayovers() == -1)
			dataset.put("numberOfLayovers", 0);
		else
			dataset.put("numberOfLayovers", flight.getNumberOfLayovers());
		dataset.put("draftMode", flight.isDraftMode());

		super.getResponse().addData(dataset);

	}
}
