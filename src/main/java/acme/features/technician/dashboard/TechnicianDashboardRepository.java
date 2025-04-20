
package acme.features.technician.dashboard;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TechnicianDashboardRepository extends AbstractRepository {

	@Query("select count(m) from MaintenanceRecord m where m.status = acme.entities.maintenanceRecord.MaintenanceRecordStatus.PENDING" //
		+ " and m.technician.id = :technicianId")
	Integer numberOfPendingMaintenanceRecords(int technicianId);

	@Query("select count(m) from MaintenanceRecord m where m.status = acme.entities.maintenanceRecord.MaintenanceRecordStatus.IN_PROGRESS" //
		+ " and m.technician.id = :technicianId")
	Integer numberOfInProgressMaintenanceRecords(int technicianId);

	@Query("select count(m) from MaintenanceRecord m where m.status = acme.entities.maintenanceRecord.MaintenanceRecordStatus.COMPLETED " //
		+ "and m.technician.id = :technicianId")
	Integer numberOfCompletedMaintenanceRecords(int technicianId);

	@Query("select m.ticker from MaintenanceRecord m where m.technician.id = :technicianId and m.nextInspectionDueTime = " //
		+ "(select min(m2.nextInspectionDueTime) from MaintenanceRecord m2 where m2.technician.id = :technicianId)")
	Collection<String> nearestMaintenanceRecord(int technicianId);

	@Query("select m.aircraft.registrationNumber from Involves i join i.maintenanceRecord m where i.task.technician.id = :technicianId " //
		+ "group by m.aircraft.registrationNumber order by count(i.task) desc")
	List<String> topFiveAircraftsWithMoreTasks(int technicianId);

	@Query("select m.estimatedCost.currency, avg(m.estimatedCost.amount), min(m.estimatedCost.amount), max(m.estimatedCost.amount), " //
		+ "stddev(m.estimatedCost.amount) from MaintenanceRecord m where m.technician.id = :technicianId group by m.estimatedCost.currency")
	Collection<Object[]> averageMinMaxStdDevOfEstimatedCost(int technicianId);

	@Query("select avg(t.estimatedDuration) from Task t where t.technician.id = :technicianId")
	Double averageNumberOfEstimatedDuration(int technicianId);

	@Query("select min(t.estimatedDuration) from Task t where t.technician.id = :technicianId")
	Integer minimumNumberOfEstimatedDuration(int technicianId);

	@Query("select max(t.estimatedDuration) from Task t where t.technician.id = :technicianId")
	Integer maximumNumberOfEstimatedDuration(int technicianId);

	@Query("select stddev(t.estimatedDuration) from Task t where t.technician.id = :technicianId")
	Double standardDeviationOfEstimatedDuration(int technicianId);

}
