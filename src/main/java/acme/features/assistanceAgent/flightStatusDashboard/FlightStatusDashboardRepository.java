
package acme.features.assistanceAgent.flightStatusDashboard;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.delay.Delay;

@Repository
public interface FlightStatusDashboardRepository extends AbstractRepository {

	@Query("select d from Delay d")
	Collection<Delay> getDelays();
	@Query("select d from Delay d where d.id =:id")
	Optional<Delay> findDelayById(int id);

}
