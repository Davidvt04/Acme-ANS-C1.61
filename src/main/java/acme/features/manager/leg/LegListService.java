
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.features.manager.flight.FlightRepository;
import acme.realms.managers.Manager;

@GuiService
public class LegListService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository	repository;

	@Autowired
	private FlightRepository		flightRepository;


	@Override
	public void authorise() {
		int flightId = super.getRequest().getData("flightId", int.class);
		var flight = this.flightRepository.findFlightById(flightId);
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean status = flight != null && flight.getManager().getId() == managerId;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		// Retrieve the masterId from the request parameters
		int flightId = super.getRequest().getData("flightId", int.class);
		boolean flightDraftMode = this.flightRepository.findFlightById(flightId).isDraftMode();

		// Retrieve the legs associated with the given flight, ordered by scheduledDeparture
		Collection<Leg> legs = this.repository.findLegsByflightIdOrderByMoment(flightId);
		super.getBuffer().addData(legs);
		// Add the flightDraftMode flag to the response so the JSP can access it.
		super.getResponse().addGlobal("flightDraftMode", flightDraftMode);
	}

	@Override
	public void unbind(final Leg leg) {
		// Unbind key fields from Leg.
		Dataset dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "durationInHours", "status");
		// Add computed origin and destination cities from the associated Airports.
		dataset.put("originCity", leg.getDepartureAirport().getCity());
		dataset.put("destinationCity", leg.getArrivalAirport().getCity());
		dataset.put("flightId", super.getRequest().getData("flightId", int.class));
		super.getResponse().addData(dataset);
	}

}
