
package acme.features.flightCrewMember.flightAssignament;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignament.Duty;
import acme.entities.flightAssignament.FlightAssignament;
import acme.entities.leg.Leg;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignamentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignament> {

	@Autowired
	private FlightCrewMemberFlightAssignamentRepository repository;


	@Override
	public void authorise() {
		String method = super.getRequest().getMethod();
		boolean authorised;
		FlightAssignament flightAssignament = null;
		boolean isHis = false;
		if (method.equals("GET"))
			authorised = false;
		else {
			int flightAssignamentId = super.getRequest().getData("id", int.class);
			flightAssignament = this.repository.findFlightAssignamentById(flightAssignamentId);
			int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int legId = super.getRequest().getData("leg", int.class);
			boolean authorised3 = true;
			if (legId != 0)
				authorised3 = this.repository.existsLeg(legId);
			boolean authorised1 = this.repository.existsFlightCrewMember(flightCrewMemberId);
			authorised = authorised3 && authorised1 && this.repository.thatFlightAssignamentIsOf(flightAssignamentId, flightCrewMemberId);
			isHis = flightAssignament.getFlightCrewMember().getId() == flightCrewMemberId;
		}
		super.getResponse().setAuthorised(authorised && flightAssignament != null && flightAssignament.isDraftMode() && isHis);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignament flightAssignament = this.repository.findFlightAssignamentById(id);
		super.getBuffer().addData(flightAssignament);
	}

	@Override
	public void bind(final FlightAssignament flightAssignament) {
		Integer legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId);
		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember flightCrewMember = this.repository.findFlightCrewMemberById(flightCrewMemberId);

		super.bindObject(flightAssignament, "duty", "currentStatus", "remarks");
		flightAssignament.setLeg(leg);
		flightAssignament.setFlightCrewMember(flightCrewMember);
	}

	@Override
	public void validate(final FlightAssignament flightAssignament) {
		FlightAssignament original = this.repository.findFlightAssignamentById(flightAssignament.getId());
		Leg leg = flightAssignament.getLeg();
		boolean cambioDuty = !original.getDuty().equals(flightAssignament.getDuty());
		boolean cambioLeg = !original.getLeg().equals(flightAssignament.getLeg());
		boolean cambioMoment = !original.getMoment().equals(flightAssignament.getMoment());
		boolean cambioStatus = !original.getCurrentStatus().equals(flightAssignament.getCurrentStatus());

		if (!(cambioDuty || cambioLeg || cambioMoment || cambioStatus))
			return;

		if (leg != null && cambioLeg && !this.isLegCompatible(flightAssignament))
			super.state(false, "flightCrewMember", "acme.validation.FlightAssignament.FlightCrewMemberIncompatibleLegs.message");

		if (leg != null && (cambioDuty || cambioLeg))
			this.checkPilotAndCopilotAssignment(flightAssignament);
	}

	private boolean isLegCompatible(final FlightAssignament flightAssignament) {
		Collection<Leg> legsByMember = this.repository.findLegsByFlightCrewMember(flightAssignament.getFlightCrewMember().getId());
		Leg newLeg = flightAssignament.getLeg();

		return legsByMember.stream().allMatch(existingLeg -> this.areLegsCompatible(newLeg, existingLeg));
	}

	private boolean areLegsCompatible(final Leg newLeg, final Leg oldLeg) {
		return !(MomentHelper.isInRange(newLeg.getScheduledDeparture(), oldLeg.getScheduledDeparture(), oldLeg.getScheduledArrival()) || MomentHelper.isInRange(newLeg.getScheduledArrival(), oldLeg.getScheduledDeparture(), oldLeg.getScheduledArrival()));
	}

	private void checkPilotAndCopilotAssignment(final FlightAssignament flightAssignament) {
		boolean havePilot = this.repository.existsFlightCrewMemberWithDutyInLeg(flightAssignament.getLeg().getId(), Duty.PILOT);
		boolean haveCopilot = this.repository.existsFlightCrewMemberWithDutyInLeg(flightAssignament.getLeg().getId(), Duty.COPILOT);

		if (Duty.PILOT.equals(flightAssignament.getDuty()))
			super.state(!havePilot, "duty", "acme.validation.FlightAssignament.havePilot.message");
		if (Duty.COPILOT.equals(flightAssignament.getDuty()))
			super.state(!haveCopilot, "duty", "acme.validation.FlightAssignament.haveCopilot.message");
	}

	@Override
	public void perform(final FlightAssignament flightAssignament) {
		flightAssignament.setMoment(MomentHelper.getCurrentMoment());
		this.repository.save(flightAssignament);
	}

	@Override
	public void unbind(final FlightAssignament flightAssignament) {
		Collection<Leg> legs = this.repository.findAllLegs();
		boolean isCompleted;
		int flightAssignamentId;

		flightAssignamentId = super.getRequest().getData("id", int.class);

		Date currentMoment;
		currentMoment = MomentHelper.getCurrentMoment();
		isCompleted = this.repository.areLegsCompletedByFlightAssignament(flightAssignamentId, currentMoment);
		SelectChoices legChoices = SelectChoices.from(legs, "flightNumber", flightAssignament.getLeg());
		SelectChoices currentStatus = SelectChoices.from(acme.entities.flightAssignament.CurrentStatus.class, flightAssignament.getCurrentStatus());
		SelectChoices duty = SelectChoices.from(Duty.class, flightAssignament.getDuty());
		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMember flightCrewMember = this.repository.findFlightCrewMemberById(flightCrewMemberId);

		Dataset dataset = super.unbindObject(flightAssignament, "duty", "moment", "currentStatus", "remarks", "draftMode");
		dataset.put("readonly", false);
		dataset.put("moment", MomentHelper.getCurrentMoment());
		dataset.put("currentStatus", currentStatus);
		dataset.put("duty", duty);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("flightCrewMember", flightCrewMember.getEmployeeCode());
		dataset.put("isCompleted", isCompleted);
		dataset.put("draftMode", flightAssignament.isDraftMode());

		super.getResponse().addData(dataset);
	}
}
