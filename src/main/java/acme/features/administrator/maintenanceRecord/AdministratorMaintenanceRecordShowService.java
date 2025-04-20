
package acme.features.administrator.maintenanceRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;

@GuiService
public class AdministratorMaintenanceRecordShowService extends AbstractGuiService<Administrator, MaintenanceRecord> {

	@Autowired
	private AdministratorMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		MaintenanceRecord maintenanceRecord;

		masterId = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);
		status = maintenanceRecord != null && super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);

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
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;
		String aircraftNumber;

		aircraftNumber = this.repository.findAircraftByMaintenanceRecordId(maintenanceRecord.getId());

		dataset = super.unbindObject(maintenanceRecord, "ticker", "moment", "status", "nextInspectionDueTime", "estimatedCost", "notes", "draftMode");
		dataset.put("aircraft", aircraftNumber);

		super.getResponse().addData(dataset);
	}
}
