
package acme.features.authenticated.airport;

import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirportRepository extends AbstractRepository {

	//@Query("SELECT a FROM Airport WHERE a.id= :airportId")
	//Airport findAirportById(Integer airportId);

}
