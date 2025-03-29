
package acme.features.technician;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select m from MaintenanceRecord m where m.technician.id = :id")
	Collection<MaintenanceRecord> findMainteanceRecordsByTechnicianId(int id);

	@Query("select m from MaintenanceRecord m where m.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select a from Aircraft a where a.registrationNumber = :aircraftRegistrationNumber")
	Aircraft findAircraftByRegistrationNumber(String aircraftRegistrationNumber);
}
