
package acme.features.customer.dashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;

@Repository
public interface CustomerDashboardRepository extends AbstractRepository {

	@Query("select b from Booking b where b.customer.id =:customerId and b.draftMode= false")
	Collection<Booking> findAllPublishedBookingsOf(int customerId);

	@Query("select br from BookingRecord br where br.booking.customer.id =:customerId and br.booking.draftMode= false")
	Collection<BookingRecord> findAllBookingRecordsOf(int customerId);

}
