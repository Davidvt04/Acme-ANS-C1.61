
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.realms.Customer;

@GuiService
public class CustomerBookingListService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Booking> bookings;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		bookings = this.repository.getAllBookingOf(customerId);

		super.getBuffer().addData(bookings);
	}

	@Override
	public void unbind(final Booking booking) {
		assert booking != null;

		Dataset dataset;

		dataset = super.unbindObject(booking, "flight", "purchaseMoment", "price", "draftMode", "locatorCode");

		super.getResponse().addData(dataset);
	}

}
