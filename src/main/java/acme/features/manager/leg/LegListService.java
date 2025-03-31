
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.realms.managers.Manager;

@GuiService
public class LegListService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		// All managers are authorised to list the legs of a flight.
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		// Retrieve the masterId from the request parameters
		int flightId = super.getRequest().getData("flightId", int.class);
		// Retrieve the legs associated with the given flight, ordered by scheduledDeparture
		Collection<Leg> legs = this.repository.findLegsByflightIdOrderByMoment(flightId);
		super.getBuffer().addData(legs);
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
