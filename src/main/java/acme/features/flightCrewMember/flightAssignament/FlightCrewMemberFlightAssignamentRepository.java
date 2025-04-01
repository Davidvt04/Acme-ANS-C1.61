
package acme.features.flightCrewMember.flightAssignament;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignament.Duty;
import acme.entities.flightAssignament.FlightAssignament;
import acme.entities.leg.Leg;
import acme.realms.flightCrewMembers.AvailabilityStatus;
import acme.realms.flightCrewMembers.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignamentRepository extends AbstractRepository {

	@Query("select fa from FlightAssignament fa where fa.id = :id")
	FlightAssignament findFlightAssignamentById(int id);

	@Query("select fa from FlightAssignament fa where fa.leg.scheduledArrival < :currentMoment and fa.flightCrewMember.id = :flighCrewMemberId")
	Collection<FlightAssignament> findAllFlightAssignamentByCompletedLeg(Date currentMoment, int flighCrewMemberId);

	@Query("select fa from FlightAssignament fa where fa.leg.scheduledArrival >= :currentMoment and fa.flightCrewMember.id = :flighCrewMemberId")
	Collection<FlightAssignament> findAllFlightAssignamentByPlannedLeg(Date currentMoment, int flighCrewMemberId);

	@Query("select fa.leg from FlightAssignament fa where fa.id = :id")
	Collection<Leg> findLegsByFlightAssignamentId(int id);

	@Query("select l from Leg l where l.id  = :legId")
	Leg findLegById(int legId);

	@Query("select fa.flightCrewMember from FlightAssignament fa where fa.id = :id")
	Collection<FlightCrewMember> findFlightCrewMembersByFlightAssignamentId(int id);

	@Query("select fcm from FlightCrewMember fcm where fcm.id = :id")
	FlightCrewMember findFlightCrewMemberById(int id);

	@Query("select fa.leg from FlightAssignament fa where fa.flightCrewMember.id = :id")
	Collection<Leg> findLegsByFlightCrewMember(int id);

	@Query("select fcm from FlightCrewMember fcm where fcm.availabilityStatus = :availabilityStatus")
	Collection<FlightCrewMember> findFlightCrewMembersByAvailability(AvailabilityStatus availabilityStatus);

	@Query("select fa.flightCrewMember from FlightAssignament fa where fa.leg.id = :legId")
	Collection<FlightCrewMember> findFlightCrewMembersAssignedToLeg(int legId);

	@Query("select al from ActivityLog al where al.flightAssignament.id = :flightAssignamentId")
	Collection<ActivityLog> findActivityLogsByFlightAssignamentId(int flightAssignamentId);

	@Query("select count(fa) > 0 from FlightAssignament fa where fa.leg.id = :legId and fa.duty = :duty")
	boolean existsFlightCrewMemberWithDutyInLeg(int legId, Duty duty);

	@Query("select fa from FlightAssignament fa where fa.leg.id=:id")
	Collection<FlightAssignament> findFlightAssignamentByLegId(int id);

	@Query("select case when count(fa) > 0 then true else false end " + "from FlightAssignament fa " + "where fa.id = :flightAssignamentId " + "and fa.leg.scheduledArrival < :currentMoment")
	boolean areLegsCompletedByFlightAssignament(int flightAssignamentId, Date currentMoment);

	@Query("select count(fa) > 0 from FlightAssignament fa where fa.id = :flightAssignamentId and fa.flightCrewMember.id = :flightCrewMemberId")
	boolean thatFlightAssignamentIsOf(int flightAssignamentId, int flightCrewMemberId);

	@Query("select l from Leg l")
	Collection<Leg> findAllLegs();

}
