
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
import acme.features.administrator.aircraft.AircraftRepository;
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
		Leg leg = this.repository.findOneLegByIdAndManager(legId, super.getRequest().getPrincipal().getActiveRealm().getId());
		boolean status = leg != null && leg.isDraftMode();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findOneLegByIdAndManager(legId, super.getRequest().getPrincipal().getActiveRealm().getId());
		super.getBuffer().addData(leg);

		Collection<Airport> airports = this.airportRepository.getAllAirports();
		SelectChoices departureChoices = new SelectChoices();
		SelectChoices arrivalChoices = new SelectChoices();
		departureChoices.add(leg.getDepartureAirport() == null ? "0" : "0", "----", leg.getDepartureAirport() == null);
		arrivalChoices.add(leg.getArrivalAirport() == null ? "0" : "0", "----", leg.getArrivalAirport() == null);
		for (Airport ap : airports) {
			String iata = ap.getIataCode();
			departureChoices.add(iata, iata, leg.getDepartureAirport() != null && iata.equals(leg.getDepartureAirport().getIataCode()));
			arrivalChoices.add(iata, iata, leg.getArrivalAirport() != null && iata.equals(leg.getArrivalAirport().getIataCode()));
		}
		super.getResponse().addGlobal("departureAirports", departureChoices);
		super.getResponse().addGlobal("arrivalAirports", arrivalChoices);

		Collection<Aircraft> aircrafts = this.aircraftRepository.findAllAircrafts();
		SelectChoices aircraftChoices = new SelectChoices();
		aircraftChoices.add(leg.getAircraft() == null ? "0" : "0", "----", leg.getAircraft() == null);
		for (Aircraft ac : aircrafts) {
			String key = String.valueOf(ac.getId());
			aircraftChoices.add(key, ac.getRegistrationNumber(), leg.getAircraft() != null && key.equals(String.valueOf(leg.getAircraft().getId())));
		}
		super.getResponse().addGlobal("aircraftChoices", aircraftChoices);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "durationInHours", "status");

		String dep = super.getRequest().getData("departureAirport", String.class);
		String arr = super.getRequest().getData("arrivalAirport", String.class);
		leg.setDepartureAirport("0".equals(dep) ? null : this.airportRepository.findByIataCode(dep));
		leg.setArrivalAirport("0".equals(arr) ? null : this.airportRepository.findByIataCode(arr));

		Integer acId = super.getRequest().getData("aircraft", Integer.class);
		leg.setAircraft(acId == null || acId == 0 ? null : this.aircraftRepository.findAircraftById(acId));

		String statusStr = super.getRequest().getData("status", String.class);
		if (statusStr != null && !statusStr.isBlank())
			try {
				leg.setStatus(LegStatus.valueOf(statusStr));
			} catch (Exception e) {
			}
	}

	@Override
	public void validate(final Leg leg) {
		// Required dates
		super.state(leg.getScheduledDeparture() != null, "scheduledDeparture", "manager.leg.error.required.date");
		super.state(leg.getScheduledArrival() != null, "scheduledArrival", "manager.leg.error.required.date");

		// Chronology
		if (leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null)
			super.state(leg.getScheduledDeparture().before(leg.getScheduledArrival()), "scheduledDeparture", "manager.leg.error.departureBeforeArrival");

		// Unique flight number
		Leg existing = this.repository.findLegByFlightNumber(leg.getFlightNumber());
		boolean ok = existing == null || existing.getId() == leg.getId();
		super.state(ok, "flightNumber", "manager.leg.error.duplicateFlightNumber");
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset ds = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "durationInHours", "draftMode");

		if (leg.getDepartureAirport() != null) {
			ds.put("departureAirport", leg.getDepartureAirport().getIataCode());
			ds.put("originCity", leg.getDepartureAirport().getCity());
		}
		if (leg.getArrivalAirport() != null) {
			ds.put("arrivalAirport", leg.getArrivalAirport().getIataCode());
			ds.put("destinationCity", leg.getArrivalAirport().getCity());
		}

		if (leg.getAircraft() != null) {
			ds.put("aircraft", leg.getAircraft().getId());
			ds.put("aircraftRegistration", leg.getAircraft().getRegistrationNumber());
		}

		ds.put("scheduledDeparture", new Object[] {
			leg.getScheduledDeparture()
		});
		ds.put("scheduledArrival", new Object[] {
			leg.getScheduledArrival()
		});

		ds.put("status", leg.getStatus());
		ds.put("legStatuses", SelectChoices.from(LegStatus.class, leg.getStatus()));
		ds.put("flightId", leg.getFlight().getId());

		super.getResponse().addData(ds);
	}
}
