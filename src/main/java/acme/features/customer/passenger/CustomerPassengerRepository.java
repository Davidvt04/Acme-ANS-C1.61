
package acme.features.customer.passenger;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("select p from Passenger p where p.customer.id=:customerId")
	Collection<Passenger> getAllPassengersOf(int customerId);

	@Query("select p from Passenger p where p.id=:passengerId")
	Passenger getPassengerById(int passengerId);

	@Query("select br.passenger from BookingRecord br where br.booking.id=:bookingId")
	Collection<Passenger> getPassengersFromBooking(int bookingId);

	@Query("select b from Booking b where b.id=:bookingId")
	Booking getBookingById(int bookingId);

	@Query("select br from BookingRecord br where br.passenger.id =:passengerId")
	List<BookingRecord> getAllBookingsRecordsWith(int passengerId);

}
