
package acme.features.flightCrewMember.activityLogRecords;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
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
		boolean status;
		int activityLogId;
		ActivityLog activityLog;

		activityLogId = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(activityLogId);
		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean authorised = this.repository.thatActivityLogIsOf(activityLogId, flightCrewMemberId);

		status = authorised && activityLog != null && activityLog.isDraftMode();

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
		super.bindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		int activityLogId = activityLog.getId();

		if (activityLog == null)
			return;
		FlightAssignament flightAssignament = activityLog.getFlightAssignament();
		if (activityLog.getRegistrationMoment() == null || flightAssignament == null)
			return;
		Leg leg = flightAssignament.getLeg();
		if (leg == null || leg.getScheduledArrival() == null)
			return;
		Date activityLogMoment = activityLog.getRegistrationMoment();
		boolean activityLogMomentIsAfterscheduledArrival = this.repository.associatedWithCompletedLeg(activityLogId, activityLogMoment);
		super.state(activityLogMomentIsAfterscheduledArrival, "WrongActivityLogDate", "acme.validation.activityLog.wrongMoment.message");
		System.out.println("El moment está despues de la fecha de llegada (el flightAssignament se completo)?");
		boolean flightAssignamentIsNotPublished = !this.repository.isFlightAssignamentAlreadyPublishedByActivityLogId(activityLogId);
		System.out.println("Se publico el flightAssignament? " + flightAssignamentIsNotPublished);
		super.state(flightAssignamentIsNotPublished, "activityLog", "acme.validation.ActivityLog.FlightAssignamentNotPublished.message");
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		activityLog.setDraftMode(false);
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;
		FlightAssignament flightAssignament = activityLog.getFlightAssignament();
		if (activityLog.getFlightAssignament() == null)
			flightAssignament = this.repository.findFlightAssignamentByActivityLogId(activityLog.getId());

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("masterId", flightAssignament.getId());
		dataset.put("draftMode", flightAssignament.isDraftMode());

		super.getResponse().addData(dataset);
	}

}
