
package acme.features.assistanceAgent.dashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentDashbordRepository extends AbstractRepository {

	@Query("select c from Claim c")
	Collection<Claim> findAllClaims();

	@Query("SELECT COUNT(t) FROM TrackingLog t WHERE t.claim.id = :claimId")
	long countLogsByClaimId(int claimId);

	@Query("select a from AssistanceAgent a")
	Collection<AssistanceAgent> findAllAssistanceAgent();

}
