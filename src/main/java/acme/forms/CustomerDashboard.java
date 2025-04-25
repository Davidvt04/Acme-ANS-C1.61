
package acme.forms;

import java.util.Collection;
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
	Collection<String>			lastFiveDestinations;
	List<Money>					spentMoney;
	long						economyBookings;
	long						businessBookings;
	List<Money>					bookingTotalCost;
	List<Money>					bookingAverageCost;
	List<Money>					bookingMinimumCost;
	List<Money>					bookingMaximumCost;
	List<Money>					bookingDeviationCost;
	long						bookingTotalPassengers;
	Double						bookingAveragePassengers;
	long						bookingMinimumPassengers;
	long						bookingMaximumPassengers;
	Double						bookingDeviationPassengers;
}
