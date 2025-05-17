
package acme.features.flightCrewMember.flightAssignament;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignament.FlightAssignament;
import acme.entities.leg.Leg;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignamentDeleteService extends AbstractGuiService<FlightCrewMember, FlightAssignament> {

	@Autowired
	private FlightCrewMemberFlightAssignamentRepository repository;


	@Override
	public void authorise() {
		boolean status = false;
		String method = super.getRequest().getMethod();
		if (method.equals("GET"))
			status = false;
		else {
			int flightAssignamentId = super.getRequest().getData("id", int.class);
			FlightAssignament flightAssignament = this.repository.findFlightAssignamentById(flightAssignamentId);
			int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
			if (flightAssignament != null) {

				boolean authorised1 = this.repository.existsFlightCrewMember(flightCrewMemberId);
				boolean authorised = authorised1 && this.repository.thatFlightAssignamentIsOf(flightAssignamentId, flightCrewMemberId);
				boolean isHis = flightAssignament.getFlightCrewMember().getId() == flightCrewMemberId;
				status = flightAssignament.isDraftMode() && authorised && isHis;
			}
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignament flightAssignament = new FlightAssignament();
		flightAssignament.setDraftMode(true);
		super.getBuffer().addData(flightAssignament);
	}

	@Override
	public void bind(final FlightAssignament flightAssignament) {
		int legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId);

		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember flightCrewMember = this.repository.findFlightCrewMemberById(flightCrewMemberId);

		super.bindObject(flightAssignament, "duty", "currentStatus", "remarks");
		flightAssignament.setLeg(leg);
		flightAssignament.setFlightCrewMember(flightCrewMember);
	}

	@Override
	public void validate(final FlightAssignament flightAssignament) {
	}

	@Override
	public void perform(final FlightAssignament flightAssignament) {
		Collection<ActivityLog> activityLogs = this.repository.findActivityLogsByFlightAssignamentId(flightAssignament.getId());
		this.repository.deleteAll(activityLogs);
		this.repository.delete(flightAssignament);
	}

	@Override
	public void unbind(final FlightAssignament flightAssignament) {

	}
}
