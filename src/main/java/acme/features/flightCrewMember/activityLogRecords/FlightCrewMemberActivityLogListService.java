
package acme.features.flightCrewMember.activityLogRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignament.FlightAssignament;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status = false;
		int masterId;
		FlightAssignament flightAssignament;
		if (super.getRequest().hasData("masterId", int.class)) {

			masterId = super.getRequest().getData("masterId", int.class);
			flightAssignament = this.repository.findFlightAssignamentById(masterId);
			if (flightAssignament != null) {

				int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
				boolean authorised = this.repository.existsFlightCrewMember(flightCrewMemberId);

				status = authorised && flightAssignament != null;
				boolean isHis = flightAssignament.getFlightCrewMember().getId() == flightCrewMemberId;
				status = status && isHis && this.repository.isFlightAssignamentCompleted(MomentHelper.getCurrentMoment(), masterId);
			}
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Collection<ActivityLog> activityLog;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);

		activityLog = this.repository.findActivityLogsByMasterId(masterId);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		super.addPayload(dataset, activityLog, "registrationMoment", "typeOfIncident");

		int masterId;

		boolean showCreate;

		masterId = super.getRequest().getData("masterId", int.class);

		showCreate = this.repository.flightAssignamentAssociatedWithCompletedLeg(masterId, MomentHelper.getCurrentMoment());

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("showCreate", showCreate);
		super.getResponse().addData(dataset);

	}

}
