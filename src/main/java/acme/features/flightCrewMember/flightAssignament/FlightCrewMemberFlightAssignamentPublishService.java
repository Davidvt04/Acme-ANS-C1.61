
package acme.features.flightCrewMember.flightAssignament;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignament.CurrentStatus;
import acme.entities.flightAssignament.Duty;
import acme.entities.flightAssignament.FlightAssignament;
import acme.entities.leg.Leg;
import acme.realms.flightCrewMembers.AvailabilityStatus;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignamentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignament> {

	@Autowired
	private FlightCrewMemberFlightAssignamentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		FlightAssignament flightAssignament;
		masterId = super.getRequest().getData("id", int.class);
		flightAssignament = this.repository.findFlightAssignamentById(masterId);
		status = flightAssignament.isDraftMode() && MomentHelper.isFuture(flightAssignament.getLeg().getScheduledArrival());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignament flightAssignament;
		flightAssignament = new FlightAssignament();

		super.getBuffer().addData(flightAssignament);
	}

	@Override
	public void bind(final FlightAssignament flightAssignament) {
		Integer legId;
		Leg leg;
		Integer flightCrewMemberId;
		FlightCrewMember flightCrewMember;
		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		flightCrewMemberId = super.getRequest().getData("flightCrewMember", int.class);
		flightCrewMember = this.repository.findFlightCrewMemberById(flightCrewMemberId);

		super.bindObject(flightAssignament, "duty", "moment", "currentStatus", "remarks");
		flightAssignament.setLeg(leg);
		flightAssignament.setFlightCrewMember(flightCrewMember);
	}

	@Override
	public void validate(final FlightAssignament flightAssignament) {
		FlightAssignament original = this.repository.findFlightAssignamentById(flightAssignament.getId());
		FlightCrewMember flightCrewMember = flightAssignament.getFlightCrewMember();
		Leg leg = flightAssignament.getLeg();
		boolean cambioDuty = !original.getDuty().equals(flightAssignament.getDuty());
		boolean cambioLeg = !original.getLeg().equals(flightAssignament.getLeg());
		boolean cambioMoment = !original.getMoment().equals(flightAssignament.getMoment());
		boolean cambioStatus = !original.getCurrentStatus().equals(flightAssignament.getCurrentStatus());

		if (!(cambioDuty || cambioLeg || cambioMoment || cambioStatus))
			return;

		if (flightCrewMember != null && leg != null && cambioLeg && !this.isLegCompatible(flightAssignament))
			super.state(false, "flightCrewMember", "acme.validation.FlightAssignament.FlightCrewMemberIncompatibleLegs.message");

		if (leg != null && (cambioDuty || cambioLeg))
			this.checkPilotAndCopilotAssignment(flightAssignament);

		boolean legCompleted = this.repository.areLegsCompletedByFlightAssignament(flightAssignament.getId(), MomentHelper.getCurrentMoment());

		if (legCompleted)
			super.state(false, "leg", "acme.validation.FlightAssignament.LegAlreadyCompleted.message");
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
		flightAssignament.setDraftMode(false);
		this.repository.save(flightAssignament);
	}

	@Override
	public void unbind(final FlightAssignament flightAssignament) {

		SelectChoices currentStatus;
		SelectChoices duty;

		Collection<Leg> legs;
		SelectChoices legChoices;

		Collection<FlightCrewMember> flightCrewMembers;
		SelectChoices flightCrewMemberChoices;
		Dataset dataset;

		legs = this.repository.findAllLegs();
		flightCrewMembers = this.repository.findFlightCrewMembersByAvailability(AvailabilityStatus.AVAILABLE);

		legChoices = SelectChoices.from(legs, "flightNumber", flightAssignament.getLeg());
		flightCrewMemberChoices = SelectChoices.from(flightCrewMembers, "employeeCode", flightAssignament.getFlightCrewMember());

		currentStatus = SelectChoices.from(CurrentStatus.class, flightAssignament.getCurrentStatus());
		duty = SelectChoices.from(Duty.class, flightAssignament.getDuty());

		dataset = super.unbindObject(flightAssignament, "duty", "moment", "CurrentStatus", "remarks", "draftMode");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("moment", MomentHelper.getBaseMoment());
		dataset.put("currentStatus", currentStatus);
		dataset.put("duty", duty);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("flightCrewMember", flightCrewMemberChoices.getSelected().getKey());
		dataset.put("flightCrewMembers", flightCrewMemberChoices);

		super.getResponse().addData(dataset);
	}
}
