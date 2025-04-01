
package acme.features.flightCrewMember.activityLogRecords;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.activityLog.ActivityLog;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiController
public class FlightCrewMemberActivityLogController extends AbstractGuiController<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogListService		listService;

	@Autowired
	private FlightCrewMemberActivityLogShowService		showService;

	@Autowired
	private FlightCrewMemberActivityLogCreateService	createService;

	@Autowired
	private FlightCrewMemberActivityLogDeleteService	deleteService;

	@Autowired
	private FlightCrewMemberActivityLogUpdateService	updateService;

	@Autowired
	private FlightCrewMemberActivityLogPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}
