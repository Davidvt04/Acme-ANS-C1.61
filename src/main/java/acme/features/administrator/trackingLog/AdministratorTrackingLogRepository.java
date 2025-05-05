
package acme.features.administrator.trackingLog;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.trackingLog.TrackingLog;

@Repository
public interface AdministratorTrackingLogRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId and t.draftMode=false")
	Collection<TrackingLog> findTrackingLogsPublishedByClaimId(int claimId);

	@Query("SELECT t FROM TrackingLog t WHERE t.id = :trackingLogId")
	TrackingLog findTrackingLogById(int trackingLogId);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.assistanceAgent.id = :assistanceAgentId")
	Collection<TrackingLog> findAllTrackingLogs(int assistanceAgentId);

	@Query("Select c from Claim c where c.assistanceAgent.id=:agentId")
	List<Claim> findClaimsByAssistanceAgent(int agentId);

	@Query("select t from TrackingLog t where t.claim.id = :claimId order by t.resolutionPercentage desc")
	Optional<List<TrackingLog>> findOrderTrackingLog(Integer claimId);
}
