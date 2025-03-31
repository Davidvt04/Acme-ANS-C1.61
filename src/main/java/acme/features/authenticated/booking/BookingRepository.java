
package acme.features.authenticated.booking;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface BookingRepository extends AbstractRepository {

	@Query("SELECT COUNT(br) FROM BookingRecord br WHERE br.booking.id = :bookingId")
	Integer getNumberPassengersOfBooking(Integer bookingId);

}
