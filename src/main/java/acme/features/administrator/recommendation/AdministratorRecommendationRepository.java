
package acme.features.administrator.recommendation;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;

@Repository
public interface AdministratorRecommendationRepository extends AbstractRepository {

	@Query("SELECT f FROM Flight f")
	List<Flight> findAllFlights();

	@Query("DELETE FROM Recommendation")
	@Modifying
	@Transactional
	void deleteAllRecommendations();

}
