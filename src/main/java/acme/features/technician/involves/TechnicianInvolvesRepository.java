
package acme.features.technician.involves;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Involves;
import acme.entities.task.Task;

@Repository
public interface TechnicianInvolvesRepository extends AbstractRepository {

	@Query("select i from Involves i where i.maintenanceRecord.id = :masterId")
	Collection<Involves> findInvolvesByMasterId(int masterId);

	@Query("select i from Involves i where i.id = :id")
	Involves findInvolvesById(int id);

	@Query("select t from Task t where t.id = :id")
	Task findTaskById(int id);

	@Query("select m from MaintenanceRecord m where m.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select t from Task t where t.ticker = :taskTicker")
	Task findTaskByTicker(String taskTicker);
}
