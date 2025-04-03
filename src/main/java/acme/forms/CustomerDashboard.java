
package acme.forms;

import java.util.Collection;

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
	Collection<String>			lastFiveDestinations;
	Money						spentMoney;
	long						economyBookings;
	long						businessBookings;
	Money						bookingTotalCost;
	Money						bookingAverageCost;
	Money						bookingMinimumCost;
	Money						bookingMaximumCost;
	Money						bookingDeviationCost;
	long						bookingTotalPassengers;
	Double						bookingAveragePassengers;
	long						bookingMinimumPassengers;
	long						bookingMaximumPassengers;
	Double						bookingDeviationPassengers;
}
