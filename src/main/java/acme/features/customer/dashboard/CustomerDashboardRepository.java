
package acme.features.customer.dashboard;

import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface CustomerDashboardRepository extends AbstractRepository {
	/*
	 * @Query("SELECT d.destination FROM Booking d WHERE d.customer.id = ?1 ORDER BY d.creationMoment DESC")
	 * List<String> lastFiveDestinations(int customerId);
	 * 
	 * @Query("SELECT SUM(d.totalCost) FROM Booking d WHERE d.customer.id = ?1")
	 * Money spentMoney(int customerId);
	 * 
	 * @Query("SELECT COUNT(d) FROM Booking d WHERE d.customer.id = ?1 AND d.bookingType = 'ECONOMY'")
	 * Integer economyBookings(int customerId);
	 * 
	 * @Query("SELECT COUNT(d) FROM Booking d WHERE d.customer.id = ?1 AND d.bookingType = 'BUSINESS'")
	 * Integer businessBookings(int customerId);
	 * 
	 * @Query("SELECT SUM(d.totalCost) FROM Booking d WHERE d.customer.id = ?1")
	 * Money bookingTotalCost(int customerId);
	 * 
	 * @Query("SELECT AVG(d.totalCost) FROM Booking d WHERE d.customer.id = ?1")
	 * Money bookingAverageCost(int customerId);
	 * 
	 * @Query("SELECT MIN(d.totalCost) FROM Booking d WHERE d.customer.id = ?1")
	 * Money bookingMinimumCost(int customerId);
	 * 
	 * @Query("SELECT MAX(d.totalCost) FROM Booking d WHERE d.customer.id = ?1")
	 * Money bookingMaximumCost(int customerId);
	 * 
	 * @Query("SELECT STDDEV(d.totalCost) FROM Booking d WHERE d.customer.id = ?1")
	 * Money bookingDeviationCost(int customerId);
	 * 
	 * @Query("SELECT SUM(d.passengers) FROM Booking d WHERE d.customer.id = ?1")
	 * Integer bookingTotalPassengers(int customerId);
	 * 
	 * @Query("SELECT AVG(d.passengers) FROM Booking d WHERE d.customer.id = ?1")
	 * Double bookingAveragePassengers(int customerId);
	 * 
	 * @Query("SELECT MIN(d.passengers) FROM Booking d WHERE d.customer.id = ?1")
	 * Integer bookingMinimumPassengers(int customerId);
	 * 
	 * @Query("SELECT MAX(d.passengers) FROM Booking d WHERE d.customer.id = ?1")
	 * Integer bookingMaximumPassengers(int customerId);
	 * 
	 * @Query("SELECT STDDEV(d.passengers) FROM Booking d WHERE d.customer.id = ?1")
	 * Double bookingDeviationPassengers(int customerId);
	 */
}
