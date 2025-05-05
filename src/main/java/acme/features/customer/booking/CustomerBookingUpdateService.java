
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
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		try {
			status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

			super.getResponse().setAuthorised(status);

			int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int bookingId = super.getRequest().getData("id", int.class);
			Booking booking = this.repository.getBookingById(bookingId);
			Integer flightId = super.getRequest().getData("flight", Integer.class);
			if (flightId == null)
				status = false;
			else if (flightId != 0) {
				Flight flight = this.repository.getFlightById(flightId);
				status = status && flight != null && !flight.isDraftMode();
			}

			status = status && customerId == booking.getCustomer().getId();
		} catch (Exception e) {
			status = false;
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		int id = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "flight", "locatorCode", "travelClass", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {
		Booking existing = this.repository.findBookingByLocator(booking.getLocatorCode());
		boolean valid = existing == null || existing.getId() == booking.getId();
		super.state(valid, "locatorCode", "customer.booking.form.error.duplicateLocatorCode");

		valid = booking.getFlight() != null;
		super.state(valid, "flight", "customer.booking.form.error.invalidFlight");
		if (booking.getFlight() != null && booking.getFlight().isDraftMode())
			throw new IllegalArgumentException("The selected flight is not available");
	}

	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(true);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices travelClasses;

		travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		Collection<Flight> flights = this.repository.findAllPublishedFlights();

		dataset = super.unbindObject(booking, "flight", "locatorCode", "travelClass", "price", "lastNibble", "draftMode", "id");
		dataset.put("travelClasses", travelClasses);
		SelectChoices flightChoices;
		try {
			flightChoices = SelectChoices.from(flights, "flightSummary", booking.getFlight());
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Selected flight is not available");
		}
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);
	}

}
