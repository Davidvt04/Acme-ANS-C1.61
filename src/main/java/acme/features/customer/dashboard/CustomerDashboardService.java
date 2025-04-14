
package acme.features.customer.dashboard;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.booking.TravelClass;
import acme.forms.CustomerDashboard;
import acme.realms.Customer;

@GuiService
public class CustomerDashboardService extends AbstractGuiService<Customer, CustomerDashboard> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Booking> bookings = this.repository.findAllBookingsOf(customerId);
		Collection<BookingRecord> bookingRecords = this.repository.findAllBookingRecordsOd(customerId);

		CustomerDashboard dashboard = new CustomerDashboard();
		//Initial setting
		Money noMoney = new Money();
		noMoney.setAmount(0.0);
		noMoney.setCurrency("EUR");
		dashboard.setLastFiveDestinations(List.of());
		dashboard.setSpentMoney(noMoney);
		dashboard.setEconomyBookings(0);
		dashboard.setBusinessBookings(0);
		dashboard.setBookingTotalCost(noMoney);
		dashboard.setBookingAverageCost(noMoney);
		dashboard.setBookingMinimumCost(noMoney);
		dashboard.setBookingMaximumCost(noMoney);
		dashboard.setBookingDeviationCost(noMoney);
		dashboard.setBookingTotalPassengers(0);
		dashboard.setBookingAveragePassengers(0.0);
		dashboard.setBookingMinimumPassengers(0);
		dashboard.setBookingMaximumPassengers(0);
		dashboard.setBookingDeviationPassengers(0.0);

		if (bookings.size() > 0 && bookingRecords.size() > 0) {
			String currency = bookings.stream().findFirst().get().getPrice().getCurrency();
			int thisYear = MomentHelper.getCurrentMoment().getYear();
			List<Booking> lastFiveYearsBookings = bookings.stream().filter(booking -> booking.getPurchaseMoment().getYear() > thisYear - 5).toList();
			long total5YearsBookings = lastFiveYearsBookings.size() > 1 ? lastFiveYearsBookings.size() : 1;
			//Last 5 destinations
			Collection<String> last5destinations = bookings.stream().sorted(Comparator.comparing(Booking::getPurchaseMoment).reversed()).map(b -> b.getFlight().getDestinationCity()).distinct().limit(5).toList();
			dashboard.setLastFiveDestinations(last5destinations);
			System.out.println(last5destinations.size());
			last5destinations.stream().forEach(d -> System.out.println(d));
			//--------------
			Double totalMoney = bookings.stream().filter(booking -> booking.getPurchaseMoment().getYear() > thisYear - 1).map(Booking::getPrice).map(Money::getAmount).reduce(0.0, Double::sum);
			Money spentMoney = new Money();
			spentMoney.setAmount(totalMoney);

			spentMoney.setCurrency(currency);
			dashboard.setSpentMoney(spentMoney);
			//---------------------
			long economyBookings = bookings.stream().filter(b -> b.getTravelClass().equals(TravelClass.ECONOMY)).count();
			dashboard.setEconomyBookings(economyBookings);
			//--------------------
			long businessBookings = bookings.stream().filter(b -> b.getTravelClass().equals(TravelClass.BUSINESS)).count();
			dashboard.setBusinessBookings(businessBookings);
			//-----------------
			Money bookingTotalCost = new Money();
			bookingTotalCost.setAmount(lastFiveYearsBookings.stream().map(Booking::getPrice).map(Money::getAmount).reduce(0.0, Double::sum));
			bookingTotalCost.setCurrency(currency);
			dashboard.setBookingTotalCost(bookingTotalCost);
			//---------------------------------------------------
			Money bookingAverageCost = new Money();
			bookingAverageCost.setAmount(bookingTotalCost.getAmount() / total5YearsBookings);
			bookingAverageCost.setCurrency(currency);
			dashboard.setBookingAverageCost(bookingAverageCost);
			//-----------------------------------
			Money bookingMinimumCost = new Money();
			bookingMinimumCost.setAmount(lastFiveYearsBookings.stream().map(Booking::getPrice).map(Money::getAmount).min(Double::compare).orElse(0.0));
			bookingMinimumCost.setCurrency(currency);
			dashboard.setBookingMinimumCost(bookingMinimumCost);
			//--------------------------------------
			Money bookingMaximumCost = new Money();
			bookingMaximumCost.setAmount(lastFiveYearsBookings.stream().map(Booking::getPrice).map(Money::getAmount).max(Double::compare).orElse(0.0));
			bookingMaximumCost.setCurrency(currency);
			dashboard.setBookingMaximumCost(bookingMaximumCost);
			//---------------------------------------
			Money bookingDeviationCost = new Money();
			double varianza = lastFiveYearsBookings.stream().map(Booking::getPrice).map(Money::getAmount).map(price -> Math.pow(price - bookingAverageCost.getAmount(), 2)).reduce(0.0, Double::sum) / total5YearsBookings;
			double deviation = Math.sqrt(varianza);
			bookingDeviationCost.setAmount(deviation);
			bookingDeviationCost.setCurrency(currency);
			dashboard.setBookingDeviationCost(bookingDeviationCost);
			//---------------------------------------
			long passengerCount = bookingRecords.stream().map(BookingRecord::getPassenger).count();
			dashboard.setBookingTotalPassengers(passengerCount);
			//---------------------------------------
			int numBookings = bookings.size();
			double passengerAverage = (double) passengerCount / numBookings;
			dashboard.setBookingAveragePassengers(passengerAverage);
			//--------------------------------------
			Map<Booking, Long> bookingPassengers = bookingRecords.stream().collect(Collectors.groupingBy(BookingRecord::getBooking, Collectors.counting()));
			int minimumPassengers = bookingPassengers.isEmpty() ? 0 : Collections.min(bookingPassengers.values()).intValue();
			dashboard.setBookingMinimumPassengers(minimumPassengers);
			//-----------------------------------------
			int maximumPassengers = bookingPassengers.isEmpty() ? 0 : Collections.max(bookingPassengers.values()).intValue();
			dashboard.setBookingMaximumPassengers(maximumPassengers);
			//-----------------------------------------
			double variancePassengers = bookingPassengers.values().stream().mapToDouble(count -> Math.pow(count - passengerAverage, 2)).sum() / (numBookings - 1);
			double standardDeviationPassengers = Math.sqrt(variancePassengers);
			dashboard.setBookingDeviationPassengers(standardDeviationPassengers);
			//--------------------------------------------
		}

		super.getBuffer().addData(dashboard);

	}

	@Override
	public void unbind(final CustomerDashboard object) {
		Dataset dataset;

		dataset = super.unbindObject(object, //
			"lastFiveDestinations", "spentMoney", // 
			"economyBookings", "businessBookings", //
			"bookingTotalCost", "bookingAverageCost", //
			"bookingMinimumCost", "bookingMaximumCost", //
			"bookingDeviationCost", "bookingTotalPassengers", //
			"bookingAveragePassengers", "bookingMinimumPassengers", //
			"bookingMaximumPassengers", "bookingDeviationPassengers");

		super.getResponse().addData(dataset);
	}

}
