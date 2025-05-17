
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingDeleteService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		try {
			Boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
			if (!super.getRequest().getMethod().equals("POST"))
				super.getResponse().setAuthorised(false);
			else {

				int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
				int bookingId = super.getRequest().getData("bookingId", int.class);
				Booking booking = this.repository.findBookingById(bookingId);

				status = status && booking != null && customerId == booking.getCustomer().getId() && booking.isDraftMode();

				super.getResponse().setAuthorised(status);
			}
		} catch (Throwable t) {
			super.getResponse().setAuthorised(false);
		}

	}

	@Override
	public void load() {
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.repository.findBookingById(bookingId);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "flight", "locatorCode", "travelClass", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {

		Collection<BookingRecord> bookingRecords = this.repository.findAllBookingRecordsByBookingId(booking.getId());
		boolean valid = bookingRecords.isEmpty();
		super.state(valid, "*", "customer.booking.form.error.stillPassengers");
	}

	@Override
	public void perform(final Booking booking) {

		this.repository.delete(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		Collection<Flight> flights = this.repository.findAllPublishedFlights();

		dataset = super.unbindObject(booking, "flight", "locatorCode", "travelClass", "lastNibble", "draftMode", "id", "price");
		dataset.put("travelClasses", travelClasses);
		SelectChoices flightChoices;
		Flight selectedFlight = this.repository.findBookingById(booking.getId()).getFlight();
		flightChoices = SelectChoices.from(flights, "flightSummary", selectedFlight);

		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);

	}

}
