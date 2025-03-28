
package acme.features.authenticated.manager.leg;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.managers.Manager;

@GuiService
public class LegCreateService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository										repository;

	// Assumes a FlightRepository exists for manager flight operations.
	@Autowired
	private acme.features.authenticated.manager.flight.FlightRepository	flightRepository;


	@Override
	public void authorise() {
		int flightId = super.getRequest().getData("flightId", int.class);
		Flight flight = this.flightRepository.findById(flightId);
		Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();
		// Only authorise creation if the flight exists and belongs to the current manager.
		boolean status = flight != null && flight.getManager().getId() == manager.getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int flightId = super.getRequest().getData("flightId", int.class);
		Flight flight = this.flightRepository.findById(flightId);
		// Create a new Leg associated with the flight.
		Leg leg = new Leg();
		leg.setFlight(flight);
		// Set default values.
		leg.setDraftMode(true);
		leg.setFlightNumber("");  // default empty flight number
		leg.setScheduledDeparture(MomentHelper.getCurrentMoment());
		leg.setScheduledArrival(MomentHelper.getCurrentMoment());
		leg.setDurationInHours(0.0);  // default duration
		// Set default leg status. We choose ON_TIME as default.
		leg.setStatus(LegStatus.ON_TIME);
		// (Other fields like departureAirport, arrivalAirport, aircraft, airline can be bound from the form.)
		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		// Bind user input for the leg. Adjust field names as needed.
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "durationInHours");
		// If your form includes airport/aircraft selections, bind them as well.
	}

	@Override
	public void validate(final Leg leg) {
		// Example validation: scheduledDeparture must be before scheduledArrival.
		super.state(leg.getScheduledDeparture().before(leg.getScheduledArrival()), "scheduledDeparture", "manager.leg.error.departureBeforeArrival");
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		// Unbind key fields into a Dataset.
		Dataset dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "durationInHours", "draftMode");
		// Add the current leg status.
		dataset.put("status", leg.getStatus());
		// Create select choices for the LegStatus enum.
		SelectChoices choices = SelectChoices.from(LegStatus.class, leg.getStatus());
		dataset.put("legStatuses", choices);
		super.getResponse().addData(dataset);
	}
}
