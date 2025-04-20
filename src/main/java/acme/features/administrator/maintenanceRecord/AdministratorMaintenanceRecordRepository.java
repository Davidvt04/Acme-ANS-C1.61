
package acme.features.administrator.maintenanceRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;

@Repository
public interface AdministratorMaintenanceRecordRepository extends AbstractRepository {

	@Query("select m from MaintenanceRecord m where m.draftMode = false")
	Collection<MaintenanceRecord> findPublishedMainteanceRecords();

	@Query("select m from MaintenanceRecord m where m.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("select m.aircraft.registrationNumber from MaintenanceRecord m where m.id = :maintenanceRecordId")
	String findAircraftByMaintenanceRecordId(int maintenanceRecordId);

}
