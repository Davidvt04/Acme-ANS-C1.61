
package acme.features.flightCrewMember.activityLogRecords;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignament.FlightAssignament;
import acme.entities.leg.Leg;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogPublishService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status = false;
		String method = super.getRequest().getMethod();
		if (method.equals("GET"))
			status = false;
		else {
			int activityLogId;
			ActivityLog activityLog;

			activityLogId = super.getRequest().getData("id", int.class);
			activityLog = this.repository.findActivityLogById(activityLogId);
			if (activityLog != null) {
				int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
				boolean authorised = this.repository.thatActivityLogIsOf(activityLogId, flightCrewMemberId);
				boolean authorised1 = this.repository.existsFlightCrewMember(flightCrewMemberId) && authorised;
				status = authorised1 && activityLog != null && activityLog.isDraftMode();
			}
		}
		super.getResponse().setAuthorised(status);
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
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		int activityLogId = activityLog.getId();

		FlightAssignament flightAssignament = this.repository.findFlightAssignamentByActivityLogId(activityLog.getId());
		if (activityLog.getRegistrationMoment() == null || flightAssignament == null)
			return;
		Leg leg = flightAssignament.getLeg();
		if (leg == null || leg.getScheduledArrival() == null)
			return;
		Date activityLogMoment = activityLog.getRegistrationMoment();
		boolean activityLogMomentIsAfterscheduledArrival = this.repository.associatedWithCompletedLeg(activityLogId, activityLogMoment);
		super.state(activityLogMomentIsAfterscheduledArrival, "WrongActivityLogDate", "acme.validation.activityLog.wrongMoment.message");
		boolean flightAssignamentIsPublished = this.repository.isFlightAssignamentAlreadyPublishedByActivityLogId(activityLogId);
		super.state(flightAssignamentIsPublished, "activityLog", "acme.validation.ActivityLog.FlightAssignamentNotPublished.message");
	}

	@Override
	public void perform(final ActivityLog activityLog) {

		activityLog.setRegistrationMoment(MomentHelper.getCurrentMoment());
		activityLog.setDraftMode(false);
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");

		dataset.put("draftMode", activityLog.isDraftMode());

		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}

}
