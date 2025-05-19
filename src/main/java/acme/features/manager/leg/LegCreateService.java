
package acme.features.manager.leg;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.features.administrator.aircraft.AircraftRepository;
import acme.features.airport.AirportRepository;
import acme.features.manager.flight.FlightRepository;
import acme.realms.managers.Manager;

@GuiService
public class LegCreateService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository	repository;

	@Autowired
	private FlightRepository		flightRepository;

	@Autowired
	private AirportRepository		airportRepository;

	@Autowired
	private AircraftRepository		aircraftRepository;


	@Override
	public void authorise() {
		boolean status = true;
		String method = super.getRequest().getMethod();
		if (method.equals("GET") && super.getRequest().hasData("id", int.class))
			status = false;
		else {
			Integer flightId = super.getRequest().getData("flightId", Integer.class);
			if (flightId == null) {
				super.getResponse().setAuthorised(false);
				return;
			}
			Flight flight = this.flightRepository.findFlightById(flightId);
			Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();
			status = flight != null && flight.isDraftMode() && flight.getManager().getId() == manager.getId();
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Integer flightId = super.getRequest().getData("flightId", Integer.class);
		Flight flight = this.flightRepository.findFlightById(flightId);

		Leg leg = new Leg();
		leg.setFlight(flight);
		leg.setDraftMode(true);
		leg.setFlightNumber(""); // default empty flight number
		leg.setScheduledDeparture(MomentHelper.getCurrentMoment());
		leg.setScheduledArrival(new Date(MomentHelper.getCurrentMoment().getTime() + 60000));
		leg.setDurationInHours(0.0); // default duration
		leg.setStatus(LegStatus.ON_TIME);

		// Build airport selection (departure and arrival).
		Collection<Airport> airports = this.airportRepository.getAllAirports();
		SelectChoices departureChoices = new SelectChoices();
		departureChoices.add("0", "----", true);
		for (Airport airport : airports) {
			String iata = airport.getIataCode();
			departureChoices.add(iata, iata, false);
		}
		super.getResponse().addGlobal("departureAirports", departureChoices);

		SelectChoices arrivalChoices = new SelectChoices();
		arrivalChoices.add("0", "----", true);
		for (Airport airport : airports) {
			String iata = airport.getIataCode();
			arrivalChoices.add(iata, iata, false);
		}
		super.getResponse().addGlobal("arrivalAirports", arrivalChoices);

		// Build aircraft selection.
		Collection<Aircraft> aircrafts = this.aircraftRepository.findAllAircrafts();
		SelectChoices aircraftChoices = new SelectChoices();
		aircraftChoices.add("0", "----", true);
		for (Aircraft ac : aircrafts) {
			String key = Integer.toString(ac.getId());
			String label = ac.getRegistrationNumber();
			aircraftChoices.add(key, label, false);
		}
		super.getResponse().addGlobal("aircraftChoices", aircraftChoices);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		// Bind basic properties, including status.
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "durationInHours", "status");

		// Explicitly bind status from the request.
		String statusStr = super.getRequest().getData("status", String.class);
		if (statusStr != null && !statusStr.isEmpty())
			try {
				LegStatus newStatus = LegStatus.valueOf(statusStr);
				leg.setStatus(newStatus);
			} catch (IllegalArgumentException ex) {
				// ignore
			}

		// Tamper‐proof airport binding (as before)…
		String departureIata = super.getRequest().getData("departureAirport", String.class);
		if ("0".equals(departureIata))
			leg.setDepartureAirport(null);
		else {
			Airport departureAirport = this.airportRepository.findByIataCode(departureIata);
			if (departureAirport == null)
				throw new IllegalStateException("Access not authorised");
			leg.setDepartureAirport(departureAirport);
		}

		String arrivalIata = super.getRequest().getData("arrivalAirport", String.class);
		if ("0".equals(arrivalIata))
			leg.setArrivalAirport(null);
		else {
			Airport arrivalAirport = this.airportRepository.findByIataCode(arrivalIata);
			if (arrivalAirport == null)
				throw new IllegalStateException("Access not authorised");
			leg.setArrivalAirport(arrivalAirport);
		}

		// --- Tamper‐proof aircraft binding below: ---
		Integer aircraftId = super.getRequest().getData("aircraft", Integer.class);
		if (aircraftId == null || aircraftId == 0)
			// user chose "----"
			leg.setAircraft(null);
		else {
			Aircraft aircraft = this.aircraftRepository.findAircraftById(aircraftId);
			if (aircraft == null)
				// invalid/tampered ID → 500
				throw new IllegalStateException("Access not authorised");
			leg.setAircraft(aircraft);
		}

	}

	@Override
	public void validate(final Leg leg) {
		if (leg.getDepartureAirport() != null && leg.getArrivalAirport() != null) {
			boolean valid = !(leg.getDepartureAirport().getId() == leg.getArrivalAirport().getId());
			super.state(valid, "arrivalAirport", "manager.leg.error.sameAirport");

			Leg existing = this.repository.findLegByFlightNumber(leg.getFlightNumber());
			boolean validFlightNumber = existing == null || existing.getId() == leg.getId();
			super.state(validFlightNumber, "flightNumber", "manager.leg.error.duplicateFlightNumber");

			super.state(leg.getScheduledDeparture() != null, "scheduledDeparture", "manager.leg.error.required.date");
			super.state(leg.getScheduledArrival() != null, "scheduledArrival", "manager.leg.error.required.date");

			// Chronology
			if (leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null)
				super.state(leg.getScheduledDeparture().before(leg.getScheduledArrival()), "scheduledDeparture", "manager.leg.error.departureBeforeArrival");
		}
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "durationInHours", "draftMode");
		if (leg.getDepartureAirport() != null) {
			dataset.put("departureAirport", leg.getDepartureAirport().getIataCode());
			dataset.put("originCity", leg.getDepartureAirport().getCity());
		}
		if (leg.getArrivalAirport() != null) {
			dataset.put("arrivalAirport", leg.getArrivalAirport().getIataCode());
			dataset.put("destinationCity", leg.getArrivalAirport().getCity());
		}
		if (leg.getAircraft() != null) {
			dataset.put("aircraft", leg.getAircraft().getId());
			dataset.put("aircraftRegistration", leg.getAircraft().getRegistrationNumber());
		}
		dataset.put("scheduledDeparture", new Object[] {
			leg.getScheduledDeparture()
		});
		dataset.put("scheduledArrival", new Object[] {
			leg.getScheduledArrival()
		});
		dataset.put("status", leg.getStatus());
		SelectChoices choices = SelectChoices.from(LegStatus.class, leg.getStatus());
		dataset.put("legStatuses", choices);
		dataset.put("flightId", leg.getFlight().getId());
		dataset.put("draftMode", leg.isDraftMode());
		super.getResponse().addData(dataset);
	}
}
