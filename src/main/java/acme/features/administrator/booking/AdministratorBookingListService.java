
package acme.features.administrator.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;

@GuiService
public class AdministratorBookingListService extends AbstractGuiService<Administrator, Booking> {

	@Autowired
	private AdministratorBookingRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Collection<Booking> bookings = this.repository.findAllPublishedBookings();

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
