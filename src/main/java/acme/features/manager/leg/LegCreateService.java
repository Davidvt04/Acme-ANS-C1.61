
package acme.features.manager.leg;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.features.manager.flight.FlightRepository;
import acme.realms.managers.Manager;

@GuiService
public class LegCreateService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository	repository;

	// Assumes a FlightRepository exists for manager flight operations.
	@Autowired
	private FlightRepository		flightRepository;


	@Override
	public void authorise() {
		Integer flightId = super.getRequest().getData("flightId", Integer.class);
		System.out.println("Authorise: flightId from request = " + flightId);
		System.out.flush();
		if (flightId == null) {
			System.out.println("Authorise: flightId is null, denying authorisation.");
			System.out.flush();
			super.getResponse().setAuthorised(false);
			return;
		}
		Flight flight = this.flightRepository.findFlightById(flightId);
		System.out.println("Authorise: retrieved flight = " + flight);
		System.out.flush();
		Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();
		boolean status = flight != null && flight.isDraftMode() && flight.getManager().getId() == manager.getId();
		System.out.println(
			"Authorise: flight draft mode = " + (flight != null ? flight.isDraftMode() : "N/A") + ", flight manager id = " + (flight != null ? flight.getManager().getId() : "N/A") + ", current manager id = " + manager.getId() + ", authorised = " + status);
		System.out.flush();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer flightId = super.getRequest().getData("flightId", Integer.class);
		System.out.println("Load: flightId from request = " + flightId);
		System.out.flush();
		Flight flight = this.flightRepository.findFlightById(flightId);
		System.out.println("Load: retrieved flight = " + flight);
		System.out.flush();
		Leg leg = new Leg();
		leg.setFlight(flight);
		leg.setAirline(null);
		leg.setDraftMode(true);
		leg.setFlightNumber("");  // default empty flight number
		leg.setScheduledDeparture(MomentHelper.getCurrentMoment());
		// Ensure arrival is after departure (e.g., one minute later)
		leg.setScheduledArrival(new Date(MomentHelper.getCurrentMoment().getTime() + 60000));
		leg.setDurationInHours(0.0);  // default duration
		leg.setStatus(LegStatus.ON_TIME);
		super.getBuffer().addData(leg);
		System.out.println("Load: new Leg created: " + leg);
		System.out.flush();

	}

	@Override
	public void bind(final Leg leg) {
		System.out.println("Bind: before binding leg: " + leg);
		System.out.flush();
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "durationInHours");
		System.out.println("Bind: after binding leg: " + leg);
		System.out.flush();
	}

	@Override
	public void validate(final Leg leg) {
		boolean valid = leg.getScheduledDeparture().before(leg.getScheduledArrival());
		System.out.println("Validate: scheduledDeparture = " + leg.getScheduledDeparture() + ", scheduledArrival = " + leg.getScheduledArrival() + ", valid = " + valid);
		System.out.flush();
		super.state(valid, "scheduledDeparture", "manager.leg.error.departureBeforeArrival");
	}

	@Override
	public void perform(final Leg leg) {
		System.out.println("FLUSH PLEASE IT DOES ENTER THE PERFORM");
		System.out.flush();
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "durationInHours", "draftMode");
		dataset.put("status", leg.getStatus());
		dataset.put("draftMode", leg.getFlight().isDraftMode());
		SelectChoices choices = SelectChoices.from(LegStatus.class, leg.getStatus());
		dataset.put("legStatuses", choices);
		dataset.put("flightId", super.getRequest().getData("flightId", int.class));
		super.getResponse().addData(dataset);
		System.out.println("Unbind: dataset = " + dataset);
		System.out.flush();
	}
}
