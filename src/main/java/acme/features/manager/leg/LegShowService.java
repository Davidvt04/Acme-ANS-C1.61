
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.features.administrator.aircraft.AircraftRepository;
import acme.features.administrator.airport.AdministratorAirportRepository;
import acme.realms.managers.Manager;

@GuiService
public class LegShowService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository			repository;

	@Autowired
	private AircraftRepository				aircraftRepository;

	@Autowired
	private AdministratorAirportRepository	airportRepository;


	@Override
	public void authorise() {
		// For simplicity, we authorize all managers. Adjust as needed.
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(id);

		// Store the leg into the buffer.
		super.getBuffer().addData(leg);

		// Build departure airport choices.
		Collection<Airport> airports = this.airportRepository.getAllAirports();
		SelectChoices departureChoices = new SelectChoices();
		if (leg.getDepartureAirport() == null)
			departureChoices.add("0", "----", true);
		else
			departureChoices.add("0", "----", false);
		for (Airport airport : airports) {
			String iata = airport.getIataCode();
			boolean isSelected = leg.getDepartureAirport() != null && iata.equals(leg.getDepartureAirport().getIataCode());
			departureChoices.add(iata, iata, isSelected);
		}

		// Build arrival airport choices.
		SelectChoices arrivalChoices = new SelectChoices();
		if (leg.getArrivalAirport() == null)
			arrivalChoices.add("0", "----", true);
		else
			arrivalChoices.add("0", "----", false);
		for (Airport airport : airports) {
			String iata = airport.getIataCode();
			boolean isSelected = leg.getArrivalAirport() != null && iata.equals(leg.getArrivalAirport().getIataCode());
			arrivalChoices.add(iata, iata, isSelected);
		}

		// Build aircraft choices.
		Collection<acme.entities.aircraft.Aircraft> aircrafts = this.aircraftRepository.findAllAircrafts();
		SelectChoices aircraftChoices = new SelectChoices();
		if (leg.getAircraft() == null)
			aircraftChoices.add("0", "----", true);
		else
			aircraftChoices.add("0", "----", false);
		for (acme.entities.aircraft.Aircraft ac : aircrafts) {
			String key = Integer.toString(ac.getId());
			String label = ac.getRegistrationNumber();
			boolean isSelected = leg.getAircraft() != null && key.equals(Integer.toString(leg.getAircraft().getId()));
			aircraftChoices.add(key, label, isSelected);
		}

		// Add the three choice collections as globals.
		super.getResponse().addGlobal("departureAirports", departureChoices);
		super.getResponse().addGlobal("arrivalAirports", arrivalChoices);
		super.getResponse().addGlobal("aircraftChoices", aircraftChoices);
	}

	@Override
	public void unbind(final Leg leg) {
		// Basic unbind.
		Dataset dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "durationInHours", "draftMode");

		// Add airport info.
		if (leg.getDepartureAirport() != null) {
			dataset.put("departureAirport", leg.getDepartureAirport().getIataCode());
			dataset.put("originCity", leg.getDepartureAirport().getCity());
		}
		if (leg.getArrivalAirport() != null) {
			dataset.put("arrivalAirport", leg.getArrivalAirport().getIataCode());
			dataset.put("destinationCity", leg.getArrivalAirport().getCity());
		}

		// Add aircraft info.
		if (leg.getAircraft() != null) {
			dataset.put("aircraft", leg.getAircraft().getId());
			dataset.put("aircraftRegistration", leg.getAircraft().getRegistrationNumber());
		}

		// Wrap date fields.
		dataset.put("scheduledDeparture", new Object[] {
			leg.getScheduledDeparture()
		});
		dataset.put("scheduledArrival", new Object[] {
			leg.getScheduledArrival()
		});

		// Leg status and additional data.
		dataset.put("status", leg.getStatus());
		SelectChoices choices = SelectChoices.from(LegStatus.class, leg.getStatus());
		dataset.put("legStatuses", choices);
		dataset.put("flightId", leg.getFlight().getId());
		dataset.put("draftMode", leg.isDraftMode());

		super.getResponse().addData(dataset);
	}

}
