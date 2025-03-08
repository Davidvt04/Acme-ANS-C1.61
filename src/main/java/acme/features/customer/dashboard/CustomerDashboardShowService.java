
package acme.features.customer.dashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.CustomerDashboard;
import acme.realms.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		CustomerDashboard dashboard;
		List<String> lastFiveDestinations;
		Money spentMoney;
		Integer economyBookings;
		Integer businessBookings;
		Money bookingTotalCost;
		Money bookingAverageCost;
		Money bookingMinimumCost;
		Money bookingMaximumCost;
		Money bookingDeviationCost;
		Integer bookingTotalPassengers;
		Double bookingAveragePassengers;
		Integer bookingMinimumPassengers;
		Integer bookingMaximumPassengers;
		Double bookingDeviationPassengers;

		Integer userAccountId = this.getRequest().getPrincipal().getAccountId();

		/*
		 * lastFiveDestinations = this.repository.lastFiveDestinations(userAccountId);
		 * spentMoney = this.repository.spentMoney(userAccountId);
		 * economyBookings = this.repository.economyBookings(userAccountId);
		 * businessBookings = this.repository.businessBookings(userAccountId);
		 * bookingTotalCost = this.repository.bookingTotalCost(userAccountId);
		 * bookingAverageCost = this.repository.bookingAverageCost(userAccountId);
		 * bookingMinimumCost = this.repository.bookingMinimumCost(userAccountId);
		 * bookingMaximumCost = this.repository.bookingMaximumCost(userAccountId);
		 * bookingDeviationCost = this.repository.bookingDeviationCost(userAccountId);
		 * bookingTotalPassengers = this.repository.bookingTotalPassengers(userAccountId);
		 * bookingAveragePassengers = this.repository.bookingAveragePassengers(userAccountId);
		 * bookingMinimumPassengers = this.repository.bookingMinimumPassengers(userAccountId);
		 * bookingMaximumPassengers = this.repository.bookingMaximumPassengers(userAccountId);
		 * bookingDeviationPassengers = this.repository.bookingDeviationPassengers(userAccountId);
		 */

		dashboard = new CustomerDashboard();
		/*
		 * dashboard.setLastFiveDestinations(lastFiveDestinations);
		 * dashboard.setSpentMoney(spentMoney);
		 * dashboard.setEconomyBookings(economyBookings);
		 * dashboard.setBusinessBookings(businessBookings);
		 * dashboard.setBookingTotalCost(bookingTotalCost);
		 * dashboard.setBookingAverageCost(bookingAverageCost);
		 * dashboard.setBookingMinimumCost(bookingMinimumCost);
		 * dashboard.setBookingMaximumCost(bookingMaximumCost);
		 * dashboard.setBookingDeviationCost(bookingDeviationCost);
		 * dashboard.setBookingTotalPassengers(bookingTotalPassengers);
		 * dashboard.setBookingAveragePassengers(bookingAveragePassengers);
		 * dashboard.setBookingMinimumPassengers(bookingMinimumPassengers);
		 * dashboard.setBookingMaximumPassengers(bookingMaximumPassengers);
		 * dashboard.setBookingDeviationPassengers(bookingDeviationPassengers);
		 */

		dashboard.setEconomyBookings(5);
		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard object) {
		Dataset dataset;

		dataset = super.unbindObject(object, //
			"lastFiveDestinations", "spentMoney", // 
			"economyBookings", "businessBookings", //
			"bookingTotalCost", "bookingAverageCost", "bookingMinimumCost", "bookingMaximumCost", "bookingDeviationCost", "bookingTotalPassengers", "bookingAveragePassengers", "bookingMinimumPassengers", "bookingMaximumPassengers",
			"bookingDeviationPassengers");

		super.getResponse().addData(dataset);
	}

}
