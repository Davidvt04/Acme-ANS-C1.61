
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	List<String>				lastFiveDestinations;
	Money						spentMoney;
	Integer						economyBookings;
	Integer						businessBookings;
	Money						bookingTotalCost;
	Money						bookingAverageCost;
	Money						bookingMinimumCost;
	Money						bookingMaximumCost;
	Money						bookingDeviationCost;
	Integer						bookingTotalPassengers;
	Double						bookingAveragePassengers;
	Integer						bookingMinimumPassengers;
	Integer						bookingMaximumPassengers;
	Double						bookingDeviationPassengers;
}
