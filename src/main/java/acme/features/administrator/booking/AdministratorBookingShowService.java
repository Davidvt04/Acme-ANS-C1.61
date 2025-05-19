
package acme.features.administrator.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;

@GuiService
public class AdministratorBookingShowService extends AbstractGuiService<Administrator, Booking> {

	@Autowired
	private AdministratorBookingRepository repository;


	@Override
	public void authorise() {
		if (!super.getRequest().getMethod().equals("GET"))
			super.getResponse().setAuthorised(false);
		else
			super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Booking booking;
		int id = super.getRequest().getData("id", int.class);

		booking = this.repository.getBookingById(id);
		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		assert booking != null;
		Dataset dataset;
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		Collection<Flight> flights = this.repository.findAllFlights();
		SelectChoices flightChoices = SelectChoices.from(flights, "id", booking.getFlight());

		dataset = super.unbindObject(booking, "locatorCode", "travelClass", "price", "lastNibble", "draftMode", "id");
		dataset.put("travelClasses", travelClasses);
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}

}
