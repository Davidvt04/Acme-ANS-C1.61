
package acme.features.administrator.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;

@GuiService
public class AdministratorMaintenanceRecordListService extends AbstractGuiService<Administrator, MaintenanceRecord> {

	@Autowired
	private AdministratorMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<MaintenanceRecord> object;

		object = this.repository.findPublishedMainteanceRecords();

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;

		dataset = super.unbindObject(maintenanceRecord, "ticker", "moment", "status", "nextInspectionDueTime");
		dataset.put("aircraft", maintenanceRecord.getAircraft().getRegistrationNumber());
		super.addPayload(dataset, maintenanceRecord);

		super.getResponse().addData(dataset);
	}
}
