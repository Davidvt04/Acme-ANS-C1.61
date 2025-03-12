
package acme.entities.trackingLog;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.claim.id = :claimId order by t.lastUpdateMoment desc")
	Optional<List<TrackingLog>> findOrderTrackingLog(Integer claimId);
}
