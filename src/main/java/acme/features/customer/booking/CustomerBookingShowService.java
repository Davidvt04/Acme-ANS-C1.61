
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(bookingId);

		super.getResponse().setAuthorised(customerId == booking.getCustomer().getId());
	}

	@Override
	public void load() {
		Booking booking;
		int id = super.getRequest().getData("id", int.class);

		booking = this.repository.findBookingById(id);
		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		assert booking != null;
		Dataset dataset;
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		Collection<Flight> flights = this.repository.findAllPublishedFlights();

		dataset = super.unbindObject(booking, "flight", "locatorCode", "travelClass", "price", "lastNibble", "draftMode", "id");

		dataset.put("travelClasses", travelClasses);

		SelectChoices flightChoices = SelectChoices.from(flights, "flightSummary", booking.getFlight());
		dataset.put("flights", flightChoices);
		dataset.put("city", booking.getFlight().getDestinationAirport().getCity());
		dataset.put("country", this.repository.findDestinationAirportByFlightId(booking.getFlight().getId()).getCountry());

		super.getResponse().addData(dataset);
	}

}
