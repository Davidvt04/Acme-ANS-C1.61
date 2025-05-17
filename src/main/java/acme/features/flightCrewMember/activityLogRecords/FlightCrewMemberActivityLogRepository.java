
package acme.features.flightCrewMember.activityLogRecords;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignament.FlightAssignament;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select fa from FlightAssignament fa where fa.id = :id")
	FlightAssignament findFlightAssignamentById(int id);

	@Query("select al.flightAssignament from ActivityLog al where al.id = :id")
	FlightAssignament findFlightAssignamentByActivityLogId(int id);

	@Query("select case when count(al) > 0 then true else false end from ActivityLog al where al.id = :id and al.flightAssignament.draftMode = false")
	boolean isFlightAssignamentAlreadyPublishedByActivityLogId(int id);

	@Query("select case when count(fa) > 0 then true else false end from FlightAssignament fa where fa.id = :id and fa.draftMode = false")
	boolean isFlightAssignamentAlreadyPublishedById(int id);

	@Query("select al from ActivityLog al where al.id = :id")
	ActivityLog findActivityLogById(int id);

	@Query("select al from ActivityLog al where al.flightAssignament.id = :masterId")
	Collection<ActivityLog> findActivityLogsByMasterId(int masterId);

	@Query("select count(al) > 0 from ActivityLog al where al.id = :activityLogId and al.flightAssignament.flightCrewMember.id = :flightCrewMemberId")
	boolean thatActivityLogIsOf(int activityLogId, int flightCrewMemberId);
	@Query("SELECT CASE WHEN COUNT(fcm) > 0 THEN true ELSE false END FROM FlightCrewMember fcm WHERE fcm.id = :id")
	boolean existsFlightCrewMember(int id);

	@Query("select case when count(al) > 0 then true else false end from ActivityLog al where al.id = :id and al.flightAssignament.leg.scheduledArrival < :currentMoment")
	boolean associatedWithCompletedLeg(int id, Date currentMoment);

	@Query("select case when count(fa) > 0 then true else false end from FlightAssignament fa where fa.id = :id and fa.leg.scheduledArrival < :currentMoment")
	boolean flightAssignamentAssociatedWithCompletedLeg(int id, Date currentMoment);

	@Query("SELECT CASE WHEN COUNT(fa) > 0 THEN true ELSE false END FROM FlightAssignament fa WHERE fa.id = :id")
	boolean existsFlightAssignament(int id);
	@Query("SELECT CASE WHEN COUNT(al) > 0 THEN true ELSE false END FROM ActivityLog al WHERE al.id = :id")
	boolean existsActivityLog(int id);

	@Query("select case when count(fa) > 0 then true else false end from FlightAssignament fa where fa.leg.scheduledArrival < :currentMoment and fa.id = :flightAssignamentId")
	boolean isFlightAssignamentCompleted(Date currentMoment, int flightAssignamentId);
}
