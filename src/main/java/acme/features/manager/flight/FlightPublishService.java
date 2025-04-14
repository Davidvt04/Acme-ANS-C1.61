
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.features.manager.leg.ManagerLegRepository;
import acme.realms.managers.Manager;

@GuiService
public class FlightPublishService extends AbstractGuiService<Manager, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightRepository		repository;

	@Autowired
	private ManagerLegRepository	managerLegRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(flightId);
		Manager manager = flight == null ? null : flight.getManager();
		// Authorise if the flight exists, is in draft mode and the current principal is the flight manager.
		boolean status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(id);
		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		// No additional binding is required for publishing.
	}

	@Override
	public void validate(final Flight flight) {
		// Retrieve all legs associated with this flight.
		Collection<Leg> legs = this.managerLegRepository.findLegsByflightId(flight.getId());
		// Validate that the flight has at least one leg.
		boolean hasLeg = !legs.isEmpty();
		super.state(hasLeg, "*", "flight.publish.error.noLegs");
		// If there is at least one leg, check that all legs are published (i.e. not in draft mode).
		if (hasLeg) {
			boolean allPublished = legs.stream().allMatch(leg -> !leg.isDraftMode());
			super.state(allPublished, "*", "flight.publish.error.unpublishedLegs");
		}
	}

	@Override
	public void perform(final Flight flight) {
		// Publish the flight by setting its draft mode to false.
		flight.setDraftMode(false);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode");
		// Include transient properties that might be of interest.
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("originCity", flight.getOriginAirport().getCity());
		dataset.put("destinationCity", flight.getDestinationAirport().getCity());
		dataset.put("numberOfLayovers", flight.getNumberOfLayovers());
		super.getResponse().addData(dataset);
	}
}
