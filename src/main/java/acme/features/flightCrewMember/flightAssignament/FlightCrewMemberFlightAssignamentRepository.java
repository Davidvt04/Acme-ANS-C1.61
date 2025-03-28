
package acme.features.flightCrewMember.flightAssignament;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignament.Duty;
import acme.entities.flightAssignament.FlightAssignament;
import acme.entities.leg.Leg;
import acme.realms.flightCrewMembers.AvailabilityStatus;
import acme.realms.flightCrewMembers.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignamentRepository extends AbstractRepository {

	@Query("select fa from FlightAssignament where fa.id = :id")
	FlightAssignament findFlightAssignamentById(int id);

	@Query("select fa from FlightAssignament fa where fa.leg.scheduledArrival < acme.helpers.MomentHelper.getCurrentMoment()")
	Collection<FlightAssignament> findAllFlightAssignamentByCompletedLeg();

	@Query("select fa from FlightAssignament fa where fa.leg.scheduledArrival >= acme.helpers.MomentHelper.getCurrentMoment()")
	Collection<FlightAssignament> findAllFlightAssignamentByPlannedLeg();

	@Query("select fa.leg from FlightAssignament fa where fa.id = :id")
	Collection<Leg> findLegsByFlightAssignamentId(int id);

	@Query("select fa.flightCrewMember from FlightAssignament fa where fa.id = :id")
	Collection<FlightCrewMember> findFlightCrewMembersByFlightAssignamentId(int id);

	@Query("select fcm from FlightCrewMember fcm where fcm.availabilityStatus = :availabilityStatus")
	Collection<FlightCrewMember> findFlightCrewMembersByAvailability(AvailabilityStatus availabilityStatus);

	@Query("select fa.flightCrewMember from FlightAssignament fa where fa.leg.id = :legId")
	Collection<FlightCrewMember> findFlightCrewMembersAssignedToLeg(int legId);

	@Query("select count(fa) > 0 from FlightAssignament fa where fa.leg.id = :legId and fa.duty = :duty")
	boolean existsFlightCrewMemberWithDutyInLeg(int legId, Duty duty);

	@Query("""
		    select count(fa) > 0
		    from FlightAssignament fa
		    where fa.flightCrewMember.id = :crewMemberId
		    and fa.flightCrewMember.availabilityStatus = 'AVAILABLE'
		    and fa.leg.scheduledArrival >= acme.helpers.MomentHelper.getCurrentMoment()
		""")
	boolean isFlightCrewMemberAvailableAndAssignedToActiveLeg(int crewMemberId);

	@Query("select fa.duty from FlightAssignament fa where fa.flightCrewMember.id = :crewMemberId and fa.leg.id = :legId")
	Duty findDutyByCrewMemberAndLeg(int crewMemberId, int legId);

	@Query("select case when count(fa) > 0 then true else false end " + "from FlightAssignament fa " + "where fa.id = :flightAssignamentId " + "and fa.leg.scheduledArrival < acme.helpers.MomentHelper.getCurrentMoment()")
	boolean areLegsCompletedByFlightAssignament(int flightAssignamentId);

}
