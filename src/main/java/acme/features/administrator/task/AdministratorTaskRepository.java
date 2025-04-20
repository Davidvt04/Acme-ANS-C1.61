
package acme.features.administrator.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;

@Repository
public interface AdministratorTaskRepository extends AbstractRepository {

	@Query("select i.task from Involves i where i.maintenanceRecord.id = :masterId")
	Collection<Task> findTasksInvolvedByMasterId(int masterId);

	@Query("select m from MaintenanceRecord m where m.id = :masterId")
	MaintenanceRecord findMaintenanceRecordById(int masterId);

	@Query("select t from Task t where t.id = :masterId")
	Task findTaskById(int masterId);
}
