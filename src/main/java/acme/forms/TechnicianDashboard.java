
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Integer						numberOfMaintenanceRecordsPerStatus;
	List<MaintenanceRecord>		nearestMaintenanceRecord;				// podria haber mas de un record con esa fecha
	List<String>				topFiveAircraftsWithMoreTasks;
	Double						averageNumberOfEstimatedCost;
	Double						minimumNumberOfEstimatedCost;
	Double						maximumNumberOfEstimatedCost;
	Double						standardDeviationOfEstimatedCost;
	Double						averageNumberOfEstimatedDuration;
	Double						minimumNumberOfEstimatedDuration;
	Double						maximumNumberOfEstimatedDuration;
	Double						standardDeviationOfEstimatedDuration;
}
