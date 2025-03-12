
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	List<Integer>				numberOfMaintenanceRecordsPerStatus;
	List<String>				statusOfMaintenanceRecordsOrderByNumber;
	List<MaintenanceRecord>		nearestMaintenanceRecord;				// podria haber mas de un record con esa fecha
	List<String>				topFiveAircraftsWithMoreTasks;
	Money						averageNumberOfEstimatedCost;
	Money						minimumNumberOfEstimatedCost;
	Money						maximumNumberOfEstimatedCost;
	Money						standardDeviationOfEstimatedCost;
	Double						averageNumberOfEstimatedDuration;
	Integer						minimumNumberOfEstimatedDuration;
	Integer						maximumNumberOfEstimatedDuration;
	Double						standardDeviationOfEstimatedDuration;
}
