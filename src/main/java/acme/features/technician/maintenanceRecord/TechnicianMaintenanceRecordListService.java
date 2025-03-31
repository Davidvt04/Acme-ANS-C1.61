
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordListService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<MaintenanceRecord> object;
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		object = this.repository.findMainteanceRecordsByTechnicianId(technicianId);

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
