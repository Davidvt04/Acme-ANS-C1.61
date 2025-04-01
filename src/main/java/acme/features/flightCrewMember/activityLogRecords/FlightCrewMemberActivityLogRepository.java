
package acme.features.flightCrewMember.activityLogRecords;

import java.util.Collection;

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

	@Query("select al from ActivityLog al where al.id = :id")
	ActivityLog findActivityLogById(int id);

	@Query("select al from ActivityLog al where al.flightAssignament.id = :masterId")
	Collection<ActivityLog> findActivityLogsByMasterId(int masterId);

}
