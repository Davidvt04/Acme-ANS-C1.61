
package acme.features.technician.dashboard;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.TechnicianDashboard;
import acme.realms.Technician;

@GuiService
public class TechnicianDashboardShowService extends AbstractGuiService<Technician, TechnicianDashboard> {

	@Autowired
	private TechnicianDashboardRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Technician.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TechnicianDashboard dashboard;
		int technicianId;
		Integer numberOfPendingMaintenanceRecords;
		Integer numberOfInProgressMaintenanceRecords;
		Integer numberOfCompletedMaintenanceRecords;
		Collection<String> nearestMaintenanceRecordsCollection;
		String nearestMaintenanceRecord;
		List<String> topFiveAircraftsWithMoreTasks;
		Collection<Object[]> averageMinMaxStdDevOfEstimatedCost;
		Double averageNumberOfEstimatedDuration;
		Integer minimumNumberOfEstimatedDuration;
		Integer maximumNumberOfEstimatedDuration;
		Double standardDeviationOfEstimatedDuration;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		numberOfPendingMaintenanceRecords = this.repository.numberOfPendingMaintenanceRecords(technicianId);
		numberOfInProgressMaintenanceRecords = this.repository.numberOfInProgressMaintenanceRecords(technicianId);
		numberOfCompletedMaintenanceRecords = this.repository.numberOfCompletedMaintenanceRecords(technicianId);
		nearestMaintenanceRecordsCollection = this.repository.nearestMaintenanceRecord(technicianId);
		nearestMaintenanceRecord = nearestMaintenanceRecordsCollection.isEmpty() ? "" : nearestMaintenanceRecordsCollection.iterator().next();
		topFiveAircraftsWithMoreTasks = this.repository.topFiveAircraftsWithMoreTasks(technicianId);
		averageMinMaxStdDevOfEstimatedCost = this.repository.averageMinMaxStdDevOfEstimatedCost(technicianId);
		averageNumberOfEstimatedDuration = this.repository.averageNumberOfEstimatedDuration(technicianId);
		minimumNumberOfEstimatedDuration = this.repository.minimumNumberOfEstimatedDuration(technicianId);
		maximumNumberOfEstimatedDuration = this.repository.maximumNumberOfEstimatedDuration(technicianId);
		standardDeviationOfEstimatedDuration = this.repository.standardDeviationOfEstimatedDuration(technicianId);

		dashboard = new TechnicianDashboard();
		dashboard.setNumberOfPendingMaintenanceRecords(numberOfPendingMaintenanceRecords);
		dashboard.setNumberOfInProgressMaintenanceRecords(numberOfInProgressMaintenanceRecords);
		dashboard.setNumberOfCompletedMaintenanceRecords(numberOfCompletedMaintenanceRecords);
		dashboard.setNearestMaintenanceRecord(nearestMaintenanceRecord);
		dashboard.setTopFiveAircraftsWithMoreTasks(topFiveAircraftsWithMoreTasks);
		dashboard.setAverageMinMaxStdDevOfEstimatedCost(averageMinMaxStdDevOfEstimatedCost);
		dashboard.setAverageNumberOfEstimatedDuration(averageNumberOfEstimatedDuration);
		dashboard.setMinimumNumberOfEstimatedDuration(minimumNumberOfEstimatedDuration);
		dashboard.setMaximumNumberOfEstimatedDuration(maximumNumberOfEstimatedDuration);
		dashboard.setStandardDeviationOfEstimatedDuration(standardDeviationOfEstimatedDuration);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final TechnicianDashboard dashboard) {
		Dataset dataset;

		dataset = super.unbindObject(dashboard, //
			"numberOfPendingMaintenanceRecords", "numberOfInProgressMaintenanceRecords", "numberOfCompletedMaintenanceRecords", // 
			"nearestMaintenanceRecord", "averageMinMaxStdDevOfEstimatedCost", //
			"averageNumberOfEstimatedDuration", "minimumNumberOfEstimatedDuration", "maximumNumberOfEstimatedDuration", //
			"standardDeviationOfEstimatedDuration");
		dataset.put("topFiveAircraftsWithMoreTasksFirst", dashboard.getTopFiveAircraftsWithMoreTasks().isEmpty() //
			? "-"
			: dashboard.getTopFiveAircraftsWithMoreTasks().get(0));
		dataset.put("topFiveAircraftsWithMoreTasksSecond", dashboard.getTopFiveAircraftsWithMoreTasks().size() < 2 //
			? "-"
			: dashboard.getTopFiveAircraftsWithMoreTasks().get(1));
		dataset.put("topFiveAircraftsWithMoreTasksThird", dashboard.getTopFiveAircraftsWithMoreTasks().size() < 3 //
			? "-"
			: dashboard.getTopFiveAircraftsWithMoreTasks().get(2));
		dataset.put("topFiveAircraftsWithMoreTasksFourth", dashboard.getTopFiveAircraftsWithMoreTasks().size() < 4 //
			? "-"
			: dashboard.getTopFiveAircraftsWithMoreTasks().get(3));
		dataset.put("topFiveAircraftsWithMoreTasksFifth", dashboard.getTopFiveAircraftsWithMoreTasks().size() < 5 //
			? "-"
			: dashboard.getTopFiveAircraftsWithMoreTasks().get(4));

		super.getResponse().addData(dataset);
	}
}
