
package acme.features.technician.maintenanceRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.realms.Technician;

@GuiController
public class TechnicianMaintenanceRecordController extends AbstractGuiController<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordListService		listService;

	@Autowired
	private TechnicianMaintenanceRecordShowService		showService;

	@Autowired
	private TechnicianMaintenanceRecordUpdateService	updateService;

	@Autowired
	private TechnicianMaintenanceRecordCreateService	createService;

	@Autowired
	private TechnicianMaintenanceRecordDeleteService	deleteService;

	@Autowired
	private TechnicianMaintenanceRecordPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}
