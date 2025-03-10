
package acme.forms;

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

	Double						numberOfMaintenanceRecordsPerStatus;
	MaintenanceRecord			nearestMaintenanceRecord;
	String						topFiveAircraftsWithMoreTasks;
	Double						averageNumberOfEstimatedCost;
	Double						minimumNumberOfEstimatedCost;
	Double						maximumNumberOfEstimatedCost;
	Double						standardDeviationOfEstimatedCost;
}
