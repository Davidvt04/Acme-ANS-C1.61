
package acme.forms;

import java.util.Collection;
import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Integer						numberOfPendingMaintenanceRecords;
	Integer						numberOfInProgressMaintenanceRecords;
	Integer						numberOfCompletedMaintenanceRecords;
	String						nearestMaintenanceRecord;
	List<String>				topFiveAircraftsWithMoreTasks;
	Collection<Object[]>		averageMinMaxStdDevOfEstimatedCost;
	Double						averageNumberOfEstimatedDuration;
	Integer						minimumNumberOfEstimatedDuration;
	Integer						maximumNumberOfEstimatedDuration;
	Double						standardDeviationOfEstimatedDuration;
}
