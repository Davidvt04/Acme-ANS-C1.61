
package acme.features.technician.maintenanceRecord;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.maintenanceRecord.MaintenanceRecordStatus;
import acme.entities.task.Task;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordPublishService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		MaintenanceRecord maintenanceRecord;
		Technician technician;
		List<Task> publishedTasks;

		masterId = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);
		publishedTasks = this.repository.findTasksAssociatedWithMaintenanceRecordById(masterId).stream().filter(Task::isDraftMode).toList();

		technician = maintenanceRecord == null ? null : maintenanceRecord.getTechnician();
		status = maintenanceRecord != null && maintenanceRecord.isDraftMode() //
			&& super.getRequest().getPrincipal().hasRealm(technician) && !publishedTasks.isEmpty();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		String aircraftRegistrationNumber;
		Aircraft aircraft;
		Date currentMoment;

		aircraftRegistrationNumber = super.getRequest().getData("aircraft", String.class);
		aircraft = this.repository.findAircraftByRegistrationNumber(aircraftRegistrationNumber);
		currentMoment = MomentHelper.getCurrentMoment();

		super.bindObject(maintenanceRecord, "ticker", "status", "nextInspectionDueTime", "estimatedCost", "notes");
		maintenanceRecord.setMoment(currentMoment);
		maintenanceRecord.setAircraft(aircraft);
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		;
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		maintenanceRecord.setDraftMode(false);
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(MaintenanceRecordStatus.class, maintenanceRecord.getStatus());

		dataset = super.unbindObject(maintenanceRecord, "ticker", "moment", "nextInspectionDueTime", "estimatedCost", "notes", "draftMode");
		dataset.put("status", choices.getSelected().getKey());
		dataset.put("statuses", choices);
		dataset.put("aircraft", maintenanceRecord.getAircraft().getRegistrationNumber());

		super.getResponse().addData(dataset);
	}
}
