
package acme.features.administrator.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;

@Repository
public interface AdministratorBookingRepository extends AbstractRepository {

	//	@Query("select b from Booking b where b.customer.id = :customerId")
	//	Collection<Booking> getAllBookingOf(int customerId);

	@Query("select b from Booking b where b.id = :id")
	Booking getBookingById(int id);
	//
	@Query("select f from Flight f")
	Collection<Flight> findAllFlights();
	//
	//	@Query("select b from Booking b where b.locatorCode= :locatorCode")
	//	Booking findBookingByLocator(String locatorCode);

	@Query("select b from Booking b where b.draftMode = false")
	Collection<Booking> findAllPublishedBookings();

}
