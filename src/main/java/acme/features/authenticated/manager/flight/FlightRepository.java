
package acme.features.authenticated.manager.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("SELECT f FROM Flight f WHERE f.manager.id = :managerId")
	Collection<Flight> findFlightsByManagerId(int managerId);
	@Query("SELECT f FROM Flight f WHERE f.id = :id")
	Flight findById(int id);

}
