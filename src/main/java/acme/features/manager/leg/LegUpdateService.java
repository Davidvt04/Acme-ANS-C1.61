
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.features.airport.AirportRepository;
import acme.realms.managers.Manager;

@GuiService
public class LegUpdateService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository	repository;

	@Autowired
	private AirportRepository		airportRepository;

	@Autowired
	private AircraftRepository		aircraftRepository;


	@Override
	public void authorise() {
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);
		Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();
		// Allow update only if the leg exists, is in draft mode, and belongs to the current manager.
		boolean status = leg != null && leg.isDraftMode() && leg.getFlight().getManager().getId() == manager.getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);
		super.getBuffer().addData(leg);

		// Fetch all airports.
		Collection<Airport> airports = this.airportRepository.getAllAirports();

		// Build departure airport choices.
		SelectChoices departureChoices = new SelectChoices();
		if (leg.getDepartureAirport() == null)
			departureChoices.add("0", "----", true);
		else
			departureChoices.add("0", "----", false);
		for (Airport airport : airports) {
			String iata = airport.getIataCode();
			boolean selected = leg.getDepartureAirport() != null && iata.equals(leg.getDepartureAirport().getIataCode());
			departureChoices.add(iata, iata, selected);
		}
		super.getResponse().addGlobal("departureAirports", departureChoices);

		// Build arrival airport choices.
		SelectChoices arrivalChoices = new SelectChoices();
		if (leg.getArrivalAirport() == null)
			arrivalChoices.add("0", "----", true);
		else
			arrivalChoices.add("0", "----", false);
		for (Airport airport : airports) {
			String iata = airport.getIataCode();
			boolean selected = leg.getArrivalAirport() != null && iata.equals(leg.getArrivalAirport().getIataCode());
			arrivalChoices.add(iata, iata, selected);
		}
		super.getResponse().addGlobal("arrivalAirports", arrivalChoices);

		// Build aircraft choices.
		Collection<Aircraft> aircrafts = this.aircraftRepository.findAllAircrafts();
		SelectChoices aircraftChoices = new SelectChoices();
		if (leg.getAircraft() == null)
			aircraftChoices.add("0", "----", true);
		else
			aircraftChoices.add("0", "----", false);
		for (Aircraft ac : aircrafts) {
			String key = Integer.toString(ac.getId());
			String label = ac.getRegistrationNumber();
			boolean selected = leg.getAircraft() != null && key.equals(Integer.toString(leg.getAircraft().getId()));
			aircraftChoices.add(key, label, selected);
		}
		super.getResponse().addGlobal("aircraftChoices", aircraftChoices);

		// Optionally, add the leg again to the buffer.
		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		// Bind the basic properties.
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "durationInHours", "status");

		// Explicitly bind the status using a conversion from String to LegStatus.
		String statusStr = super.getRequest().getData("status", String.class);
		if (statusStr != null && !statusStr.isEmpty())
			try {
				LegStatus newStatus = LegStatus.valueOf(statusStr);
				leg.setStatus(newStatus);
			} catch (IllegalArgumentException ex) {
				// Optionally, handle conversion error
			}

		// Bind the departure and arrival airports using their IATA codes.
		String departureIata = super.getRequest().getData("departureAirport", String.class);
		String arrivalIata = super.getRequest().getData("arrivalAirport", String.class);
		Airport departureAirport = this.airportRepository.findByIataCode(departureIata);
		Airport arrivalAirport = this.airportRepository.findByIataCode(arrivalIata);
		leg.setDepartureAirport(departureAirport);
		leg.setArrivalAirport(arrivalAirport);

		// Bind the aircraft using its id.
		Integer aircraftId = super.getRequest().getData("aircraft", Integer.class);
		if (aircraftId != null && aircraftId != 0) {
			Aircraft aircraft = this.aircraftRepository.findAircraftById(aircraftId);
			leg.setAircraft(aircraft);
		} else
			leg.setAircraft(null);
	}

	@Override
	public void validate(final Leg leg) {
		// Validate that scheduledDeparture is before scheduledArrival.
		super.state(leg.getScheduledDeparture().before(leg.getScheduledArrival()), "scheduledDeparture", "manager.leg.error.departureBeforeArrival");

		// Validate that no other leg has the same flight number.
		Leg existing = this.repository.findLegByFlightNumber(leg.getFlightNumber());
		boolean validFlightNumber = existing == null || existing.getId() == leg.getId();
		super.state(validFlightNumber, "flightNumber", "manager.leg.error.duplicateFlightNumber");
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "durationInHours", "draftMode");

		// Include selected departure and arrival airports by IATA codes and cities.
		if (leg.getDepartureAirport() != null) {
			dataset.put("departureAirport", leg.getDepartureAirport().getIataCode());
			dataset.put("originCity", leg.getDepartureAirport().getCity());
		}
		if (leg.getArrivalAirport() != null) {
			dataset.put("arrivalAirport", leg.getArrivalAirport().getIataCode());
			dataset.put("destinationCity", leg.getArrivalAirport().getCity());
		}

		// Include aircraft information.
		if (leg.getAircraft() != null) {
			dataset.put("aircraft", leg.getAircraft().getId());
			dataset.put("aircraftRegistration", leg.getAircraft().getRegistrationNumber());
		}

		// Wrap date fields for proper formatting.
		dataset.put("scheduledDeparture", new Object[] {
			leg.getScheduledDeparture()
		});
		dataset.put("scheduledArrival", new Object[] {
			leg.getScheduledArrival()
		});

		// Add leg status and create select choices for LegStatus.
		dataset.put("status", leg.getStatus());
		SelectChoices choices = SelectChoices.from(LegStatus.class, leg.getStatus());
		dataset.put("legStatuses", choices);

		// Set flightId using the flight's own id.
		dataset.put("flightId", leg.getFlight().getId());
		// Ensure draftMode is correctly set.
		dataset.put("draftMode", leg.isDraftMode());

		super.getResponse().addData(dataset);
	}
}
