
package acme.features.administrator.airline;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;

@Repository
public interface AirlineRepository extends AbstractRepository {

	@Query("SELECT a FROM Airline a WHERE a.iataCode = :iataCode")
	Airline findByIataCode(String iataCode);

	@Query("SELECT a FROM Airline a")
	Collection<Airline> findAllAirlines();

	@Query("SELECT a FROM Airline a WHERE a.id = :id")
	Airline findById(int id);
}
