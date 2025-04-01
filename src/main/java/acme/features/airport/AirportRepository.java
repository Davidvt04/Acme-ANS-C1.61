
package acme.features.airport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airport.Airport;

@Repository
public interface AirportRepository extends AbstractRepository {

	@Query("select a from Airport a")
	Collection<Airport> getAllAirports();

	@Query("select a from Airport a where a.id = :id")
	Airport getAirportById(int id);

	@Query("SELECT a FROM Airport a WHERE a.iataCode = :iataCode")
	Airport findByIataCode(String iataCode);

}
