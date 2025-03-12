
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.flightAssignament.CurrentStatus;
import acme.entities.flightAssignament.FlightAssignament;
import acme.realms.flightCrewMembers.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long				serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	List<String>							lastFiveDestinations;
	List<Integer>							legsWithActivityLogDifferentSeverityRanging; //En el 1ro, las de 0 a 3, en el 2do las de 4 a 7 y en el 3ro de 8 a 10
	List<FlightCrewMember>					lastMembersAssignedWith;
	Map<CurrentStatus, FlightAssignament>	flightAssignamentByStatus;

	//All these are considering only the last month
	Double									averageNumberOfFlightAssignaments;
	Integer									minumumNumberOfFlightAssignaments;
	Integer									maximumNumberOfFlightAssignaments;
	Double									standardDeviationNumberOfFlightAssignaments;
}
