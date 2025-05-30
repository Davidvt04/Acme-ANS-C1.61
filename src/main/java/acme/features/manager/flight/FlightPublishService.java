
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

	@Autowired
	private FlightRepository		repository;

	@Autowired
	private ManagerLegRepository	managerLegRepository;


	@Override
	public void authorise() {
		boolean status = true;
		String method = super.getRequest().getMethod();
		if (method.equals("GET"))
			status = false;
		else {
			int flightId = super.getRequest().getData("id", int.class);
			Flight flight = this.repository.findById(flightId);
			Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();

			status = flight != null && flight.isDraftMode() && flight.getManager().getId() == manager.getId();
		}
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
		// now also bind the date fields so they survive validation errors
		super.bindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "scheduledDeparture",    // ← added
			"scheduledArrival"       // ← added
		);
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
		flight.setDraftMode(false);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode");

		// Transient dates (only put if non-null)
		if (flight.getScheduledDeparture() != null)
			dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		if (flight.getScheduledArrival() != null)
			dataset.put("scheduledArrival", flight.getScheduledArrival());

		// Origin / destination
		if (flight.getOriginAirport() != null)
			dataset.put("originCity", flight.getOriginAirport().getCity());
		else
			dataset.put("originCity", "");
		if (flight.getDestinationAirport() != null)
			dataset.put("destinationCity", flight.getDestinationAirport().getCity());
		else
			dataset.put("destinationCity", "");
		// Layovers
		Integer layovers = flight.getNumberOfLayovers();
		if (layovers == -1)
			dataset.put("numberOfLayovers", 0);
		else
			dataset.put("numberOfLayovers", layovers != null ? layovers : 0);
		super.getResponse().addData(dataset);
	}
}
