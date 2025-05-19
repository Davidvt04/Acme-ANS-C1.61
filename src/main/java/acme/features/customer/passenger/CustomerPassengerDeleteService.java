
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerDeleteService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		try {
			boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

			super.getResponse().setAuthorised(status);

			if (!super.getRequest().getMethod().equals("POST"))
				super.getResponse().setAuthorised(false);
			else {
				int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
				int passengerId = super.getRequest().getData("id", int.class);
				Passenger passenger = this.repository.findPassengerById(passengerId);

				super.getResponse().setAuthorised(customerId == passenger.getCustomer().getId() && passenger.isDraftMode());
			}

		} catch (Throwable t) {
			super.getResponse().setAuthorised(false);
		}

	}

	@Override
	public void load() {
		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		Collection<BookingRecord> bookingRecords = this.repository.findAllBookingRecordsByPassengerId(passenger.getId());
		super.state(bookingRecords.isEmpty(), "*", "customer.passenger.form.error.associatedBookings");
	}

	@Override
	public void perform(final Passenger passenger) {

		Passenger deletedPassenger = this.repository.findPassengerById(passenger.getId());
		this.repository.delete(deletedPassenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");

		super.getResponse().addData(dataset);
	}

}
