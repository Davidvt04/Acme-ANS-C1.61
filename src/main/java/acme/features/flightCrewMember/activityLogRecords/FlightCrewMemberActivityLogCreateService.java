
package acme.features.flightCrewMember.activityLogRecords;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignament.FlightAssignament;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		FlightAssignament flightAssignament;

		masterId = super.getRequest().getData("masterId", int.class);

		flightAssignament = this.repository.findFlightAssignamentById(masterId);
		status = flightAssignament != null;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int masterId;
		FlightAssignament flightAssignament;

		masterId = super.getRequest().getData("masterId", int.class);
		flightAssignament = this.repository.findFlightAssignamentById(masterId);

		activityLog = new ActivityLog();
		activityLog.setFlightAssignament(flightAssignament);
		activityLog.setDraftMode(true);

		super.getBuffer().addData(activityLog);

	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel");

	}

	@Override
	public void validate(final ActivityLog activityLog) {

	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		super.getResponse().addData(dataset);

	}

}
