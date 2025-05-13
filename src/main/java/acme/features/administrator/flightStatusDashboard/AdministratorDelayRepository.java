
package acme.features.administrator.flightStatusDashboard;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface AdministratorDelayRepository extends AbstractRepository {

	@Query("DELETE FROM Delay")
	@Modifying
	@Transactional
	void deleteAllDelays();

}
