
package acme.features.flightCrewMember.activityLogRecords;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignament.FlightAssignament;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogShowService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		int activityLogId;

		ActivityLog activityLog;
		boolean authorised = false;
		boolean isHis = false;
		activityLogId = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(activityLogId);
		boolean authorised3 = this.repository.existsActivityLog(activityLogId);
		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightAssignament flightAssignament = this.repository.findFlightAssignamentByActivityLogId(activityLogId);
		if (flightAssignament != null) {
			boolean authorised2 = this.repository.existsFlightAssignament(flightAssignament.getId());

			boolean authorised1 = authorised3 && authorised2 && this.repository.existsFlightCrewMember(flightCrewMemberId);
			authorised = authorised1 && this.repository.thatActivityLogIsOf(activityLogId, flightCrewMemberId);
			isHis = flightAssignament.getFlightCrewMember().getId() == flightCrewMemberId;
		}

		super.getResponse().setAuthorised(authorised && activityLog != null && isHis);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(id);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;
		FlightAssignament flightAssignament = this.repository.findFlightAssignamentByActivityLogId(activityLog.getId());

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("id", activityLog.getId());
		dataset.put("masterId", flightAssignament.getId());
		dataset.put("draftMode", activityLog.isDraftMode());
		dataset.put("masterDraftMode", flightAssignament.isDraftMode());
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}

}
