
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordCreateService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	private CustomerBookingRecordRepository repository;


	@Override
	public void authorise() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.repository.getBookingById(bookingId);

		super.getResponse().setAuthorised(customerId == booking.getCustomer().getId());

	}

	@Override
	public void load() {
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.repository.getBookingById(bookingId);
		BookingRecord bookingRecord = new BookingRecord();
		bookingRecord.setBooking(booking);
		super.getBuffer().addData(bookingRecord);
	}

	@Override
	public void bind(final BookingRecord bookingRecord) {
		super.bindObject(bookingRecord, "passenger", "booking");
	}

	@Override
	public void validate(final BookingRecord bookingRecord) {

	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		this.repository.save(bookingRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		assert bookingRecord != null;
		Dataset dataset;

		dataset = super.unbindObject(bookingRecord, "passenger", "booking");
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		int bookingId = super.getRequest().getData("bookingId", int.class);
		Collection<Passenger> addedPassengers = this.repository.getPassengersInBooking(bookingId);

		Collection<Passenger> passengers = this.repository.getAllPassengersOf(customerId).stream().filter(p -> !addedPassengers.contains(p)).toList();
		SelectChoices passengerChoices = SelectChoices.from(passengers, "fullName", bookingRecord.getPassenger());
		dataset.put("passengers", passengerChoices);

		super.getResponse().addData(dataset);

	}

}
