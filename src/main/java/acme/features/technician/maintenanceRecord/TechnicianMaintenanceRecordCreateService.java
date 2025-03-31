
package acme.features.technician.maintenanceRecord;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.maintenanceRecord.MaintenanceRecordStatus;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		Technician technician;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		maintenanceRecord = new MaintenanceRecord();
		maintenanceRecord.setDraftMode(true);
		maintenanceRecord.setTechnician(technician);

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

		super.bindObject(maintenanceRecord, "ticker", "nextInspectionDueTime", "estimatedCost", "notes");
		maintenanceRecord.setMoment(currentMoment);
		maintenanceRecord.setStatus(MaintenanceRecordStatus.PENDING);
		maintenanceRecord.setAircraft(aircraft);
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		;
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;
		//	SelectChoices choices;

		//	choices = SelectChoices.from(MaintenanceRecordStatus.class, maintenanceRecord.getStatus());

		dataset = super.unbindObject(maintenanceRecord, "ticker", "nextInspectionDueTime", "estimatedCost", "notes", "draftMode");
		dataset.put("aircraft", "");

		super.getResponse().addData(dataset);
	}
}
