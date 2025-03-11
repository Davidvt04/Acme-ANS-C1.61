
package acme.features.technician.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecord.MaintenanceRecord;

@Repository
public interface TechnicianDashboardRepository extends AbstractRepository {

	@Query("select m.status, count(m) from MaintenanceRecord m where m.technician.id = :technicianId group by m.status")
	Iterable<String> numberOfMaintenanceRecordsPerStatus(int technicianId); // StatusCountDTO en lugar de String

	@Query("select m from MaintenanceRecord m where m.technician.id = :technicianId and m.nextInspectionDueTime = (select min(mr.nextInspectionDueTime) from MaintenanceRecord mr)")
	Iterable<MaintenanceRecord> nearestMaintenanceRecord(int technicianId);

	@Query("SELECT a.registrationNumber FROM Aircraft a JOIN MaintenanceRecord m ON m.aircraft.id = a.id " + "JOIN Involves i ON i.maintenanceRecord.id = m.id JOIN Task t ON i.task.id = t.id WHERE t.technician.id = :technicianId "
		+ "GROUP BY a.id, a.registrationNumber ORDER BY COUNT(t.id) DESC LIMIT 5")
	Iterable<String> topFiveAircraftsWithMoreTasks(int technicianId);

	@Query("select avg(m.estimatedCost) from MaintenanceRecord m where m.technician.id = :technicianId")
	Double averageNumberOfEstimatedCost(int technicianId);

	@Query("select min(m.estimatedCost) from MaintenanceRecord m where m.technician.id = :technicianId")
	Double minimumNumberOfEstimatedCost(int technicianId);

	@Query("select max(m.estimatedCost) from MaintenanceRecord m where m.technician.id = :technicianId")
	Double maximumNumberOfEstimatedCost(int technicianId);

	@Query("select stddev(m.estimatedCost) from MaintenanceRecord m where m.technician.id = :technicianId")
	Double standardDeviationOfEstimatedCost(int technicianId);

	@Query("select avg(m.estimatedDuration) from MaintenanceRecord m where m.technician.id = :technicianId")
	Double averageNumberOfEstimatedDuration(int technicianId);

	@Query("select min(m.estimatedDuration) from MaintenanceRecord m where m.technician.id = :technicianId")
	Double minimumNumberOfEstimatedDuration(int technicianId);

	@Query("select max(m.estimatedDuration) from MaintenanceRecord m where m.technician.id = :technicianId")
	Double maximumNumberOfEstimatedDuration(int technicianId);

	@Query("select stddev(m.estimatedDuration) from MaintenanceRecord m where m.technician.id = :technicianId")
	Double standardDeviationOfEstimatedDuration(int technicianId);
}
