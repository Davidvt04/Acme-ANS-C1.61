
package acme.features.authenticated.leg;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	// Number of layovers is simply (count of legs - 1) for the given flight
	@Query("SELECT COUNT(l) - 1 FROM Leg l WHERE l.flight.id = :flightId")
	Integer numberOfLayovers(@Param("flightId") int flightId);

	// First scheduled departure is the MIN(scheduledDeparture) among all legs of the flight
	@Query("SELECT MIN(l.scheduledDeparture) FROM Leg l WHERE l.flight.id = :flightId")
	Optional<Date> findFirstScheduledDeparture(@Param("flightId") int flightId);

	// Last scheduled arrival is the MAX(scheduledArrival) among all legs of the flight
	@Query("SELECT MAX(l.scheduledArrival) FROM Leg l WHERE l.flight.id = :flightId")
	Optional<Date> findLastScheduledArrival(@Param("flightId") int flightId);

	// The city of the departure airport that has the earliest scheduled departure
	@Query("""
		SELECT l.departureAirport.city
		FROM Leg l
		WHERE l.flight.id = :flightId
		  AND l.scheduledDeparture = (
		      SELECT MIN(l2.scheduledDeparture)
		      FROM Leg l2
		      WHERE l2.flight.id = :flightId
		  )
		""")
	Optional<String> findFirstOriginCity(@Param("flightId") int flightId);

	// The city of the arrival airport that has the latest scheduled arrival
	@Query("""
		SELECT l.arrivalAirport.city
		FROM Leg l
		WHERE l.flight.id = :flightId
		  AND l.scheduledArrival = (
		      SELECT MAX(l2.scheduledArrival)
		      FROM Leg l2
		      WHERE l2.flight.id = :flightId
		  )
		""")
	Optional<String> findLastDestinationCity(@Param("flightId") int flightId);

}
