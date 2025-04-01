
package acme.features.flightCrewMember.flightAssignament;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightAssignament.FlightAssignament;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiController
public class FlightCrewMemberFlightAssignamentController extends AbstractGuiController<FlightCrewMember, FlightAssignament> {

	@Autowired
	private FlightCrewMemberFlightAssignamentCompletedListService	completedListService;

	@Autowired
	private FlightCrewMemberFlightAssignamentPlannedListService		plannedListService;

	@Autowired
	private FlightCrewMemberMemberFlightAssignamentShowService		showService;

	@Autowired
	private FlightCrewMemberFlightAssignamentCreateService			createService;

	@Autowired
	private FlightCrewMemberFlightAssignamentDeleteService			deleteService;

	@Autowired
	private FlightCrewMemberFlightAssignamentUpdateService			updateService;

	@Autowired
	private FlightCrewMemberFlightAssignamentPublishService			publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("completed-list", "list", this.completedListService);
		super.addCustomCommand("planned-list", "list", this.plannedListService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
