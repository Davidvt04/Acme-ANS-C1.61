
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.leg.Leg;

@Repository
public interface ManagerLegRepository extends AbstractRepository {

	@Query("SELECT l FROM Leg l WHERE l.id = :id")
	Leg findLegById(int id);

	@Query("SELECT l FROM Leg l WHERE l.flight.manager.id = :managerId ORDER BY l.scheduledDeparture ASC")
	Collection<Leg> findLegsByManagerId(int managerId);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture ASC")
	Collection<Leg> findLegsByflightIdOrderByMoment(int flightId);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture ASC")
	Collection<Leg> findLegsByflightId(int flightId);

	@Query("SELECT l FROM Leg l WHERE l.flight.manager.id = :managerId ORDER BY l.scheduledDeparture ASC")
	Collection<Leg> findLegsByManagerIdOrderByMoment(int managerId);
}
