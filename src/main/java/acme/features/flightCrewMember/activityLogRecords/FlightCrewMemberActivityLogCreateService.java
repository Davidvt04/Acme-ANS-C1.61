
package acme.features.flightCrewMember.activityLogRecords;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
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
		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean authorised = this.repository.existsFlightCrewMember(flightCrewMemberId);

		flightAssignament = this.repository.findFlightAssignamentById(masterId);
		status = authorised && flightAssignament != null;
		boolean isHis = flightAssignament.getFlightCrewMember().getId() == flightCrewMemberId;

		super.getResponse().setAuthorised(status && isHis);

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
		activityLog.setDescription("");
		activityLog.setRegistrationMoment(MomentHelper.getCurrentMoment());
		activityLog.setSeverityLevel(0);
		activityLog.setTypeOfIncident("");

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
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("masterId", masterId);

		dataset.put("draftMode", activityLog.isDraftMode());
		dataset.put("readonly", false);
		dataset.put("masterDraftMode", !this.repository.isFlightAssignamentAlreadyPublishedById(masterId));
		super.getResponse().addData(dataset);

	}

}
