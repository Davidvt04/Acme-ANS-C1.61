
package acme.features.customer.dashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
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
		List<String> currencies = bookings.stream().map(b -> b.getPrice().getCurrency()).distinct().toList();

		CustomerDashboard dashboard = new CustomerDashboard();

		dashboard.setLastFiveDestinations(List.of());
		dashboard.setSpentMoney(new LinkedList<>());
		dashboard.setEconomyBookings(0);
		dashboard.setBusinessBookings(0);
		dashboard.setBookingTotalCost(new LinkedList<>());
		dashboard.setBookingAverageCost(new LinkedList<>());
		dashboard.setBookingMinimumCost(new LinkedList<>());
		dashboard.setBookingMaximumCost(new LinkedList<>());
		dashboard.setBookingDeviationCost(new LinkedList<>());
		dashboard.setBookingTotalPassengers(0);
		dashboard.setBookingAveragePassengers(Double.NaN);
		dashboard.setBookingMinimumPassengers(0);
		dashboard.setBookingMaximumPassengers(0);
		dashboard.setBookingDeviationPassengers(Double.NaN);

		if (!bookings.isEmpty() && !bookingRecords.isEmpty()) {
			int thisYear = MomentHelper.getCurrentMoment().getYear();
			List<Booking> lastFiveYearsBookings = bookings.stream().filter(booking -> booking.getPurchaseMoment().getYear() > thisYear - 5).toList();

			Collection<String> last5destinations = bookings.stream().sorted(Comparator.comparing(Booking::getPurchaseMoment).reversed()).map(b -> b.getFlight().getDestinationAirport().getCity()).distinct().limit(5).toList();
			dashboard.setLastFiveDestinations(last5destinations);

			for (String currency : currencies) {
				Double totalMoney = bookings.stream().filter(booking -> booking.getPurchaseMoment().getYear() > thisYear - 1).filter(booking -> booking.getPrice().getCurrency().equals(currency)).map(Booking::getPrice).map(Money::getAmount).reduce(0.0,
					Double::sum);

				Money spentMoney = new Money();
				spentMoney.setAmount(totalMoney);
				spentMoney.setCurrency(currency);

				List<Money> spentMoneys = new ArrayList<>(dashboard.getSpentMoney());
				spentMoneys.add(spentMoney);
				dashboard.setSpentMoney(spentMoneys);
			}

			long economyBookings = bookings.stream().filter(b -> b.getTravelClass().equals(TravelClass.ECONOMY)).count();
			dashboard.setEconomyBookings(economyBookings);

			long businessBookings = bookings.stream().filter(b -> b.getTravelClass().equals(TravelClass.BUSINESS)).count();
			dashboard.setBusinessBookings(businessBookings);

			for (String currency : currencies) {
				Money bookingTotalCost = new Money();
				bookingTotalCost.setAmount(lastFiveYearsBookings.stream().filter(booking -> booking.getPrice().getCurrency().equals(currency)).map(Booking::getPrice).map(Money::getAmount).reduce(0.0, Double::sum));
				bookingTotalCost.setCurrency(currency);

				List<Money> bookingTotalCosts = new ArrayList<>(dashboard.getBookingTotalCost());
				bookingTotalCosts.add(bookingTotalCost);
				dashboard.setBookingTotalCost(bookingTotalCosts);
			}

			for (String currency : currencies) {
				Money bookingAverageCost = new Money();
				Money bookingTotalCost = dashboard.getBookingTotalCost().stream().filter(b -> b.getCurrency().equals(currency)).findFirst().get();
				long total5YearsBookingCurrency = lastFiveYearsBookings.stream().filter(b -> b.getPrice().getCurrency().equals(currency)).count();

				bookingAverageCost.setAmount(Double.NaN);
				if (total5YearsBookingCurrency > 0)
					bookingAverageCost.setAmount(bookingTotalCost.getAmount() / total5YearsBookingCurrency);

				bookingAverageCost.setCurrency(currency);

				List<Money> bookingAverageCosts = new ArrayList<>(dashboard.getBookingAverageCost());
				bookingAverageCosts.add(bookingAverageCost);
				dashboard.setBookingAverageCost(bookingAverageCosts);
			}

			for (String currency : currencies) {
				Money bookingMinimumCost = new Money();
				bookingMinimumCost.setAmount(lastFiveYearsBookings.stream().map(Booking::getPrice).filter(p -> p.getCurrency().equals(currency)).map(Money::getAmount).min(Double::compare).orElse(0.0));
				bookingMinimumCost.setCurrency(currency);

				List<Money> bookingMinimumCosts = new ArrayList<>(dashboard.getBookingMinimumCost());
				bookingMinimumCosts.add(bookingMinimumCost);
				dashboard.setBookingMinimumCost(bookingMinimumCosts);
			}

			for (String currency : currencies) {
				Money bookingMaximumCost = new Money();
				bookingMaximumCost.setAmount(lastFiveYearsBookings.stream().map(Booking::getPrice).filter(p -> p.getCurrency().equals(currency)).map(Money::getAmount).max(Double::compare).orElse(0.0));
				bookingMaximumCost.setCurrency(currency);

				List<Money> bookingMaximumCosts = new ArrayList<>(dashboard.getBookingMaximumCost());
				bookingMaximumCosts.add(bookingMaximumCost);
				dashboard.setBookingMaximumCost(bookingMaximumCosts);
			}

			for (String currency : currencies) {
				Money bookingDeviationCost = new Money();
				Money bookingAverageCost = dashboard.getBookingAverageCost().stream().filter(p -> p.getCurrency().equals(currency)).findFirst().get();
				long total5YearsBookingCurrency = lastFiveYearsBookings.stream().filter(b -> b.getPrice().getCurrency().equals(currency)).count();

				bookingDeviationCost.setAmount(Double.NaN);
				if (total5YearsBookingCurrency > 0) {
					double varianza = lastFiveYearsBookings.stream().map(Booking::getPrice).filter(p -> p.getCurrency().equals(currency)).map(Money::getAmount).map(price -> Math.pow(price - bookingAverageCost.getAmount(), 2)).reduce(0.0, Double::sum)
						/ total5YearsBookingCurrency;
					double deviation = Math.sqrt(varianza);
					bookingDeviationCost.setAmount(deviation);
				}
				bookingDeviationCost.setCurrency(currency);

				List<Money> bookingDeviationCosts = new ArrayList<>(dashboard.getBookingDeviationCost());
				bookingDeviationCosts.add(bookingDeviationCost);
				dashboard.setBookingDeviationCost(bookingDeviationCosts);
			}

			long passengerCount = bookingRecords.stream().map(BookingRecord::getPassenger).count();
			dashboard.setBookingTotalPassengers(passengerCount);

			int numBookings = bookings.size();
			double passengerAverage = (double) passengerCount / numBookings;
			dashboard.setBookingAveragePassengers(passengerAverage);

			Map<Booking, Long> bookingPassengers = bookingRecords.stream().collect(Collectors.groupingBy(BookingRecord::getBooking, Collectors.counting()));

			int minimumPassengers = bookingPassengers.isEmpty() ? 0 : Collections.min(bookingPassengers.values()).intValue();
			dashboard.setBookingMinimumPassengers(minimumPassengers);

			int maximumPassengers = bookingPassengers.isEmpty() ? 0 : Collections.max(bookingPassengers.values()).intValue();
			dashboard.setBookingMaximumPassengers(maximumPassengers);

			double variancePassengers = bookingPassengers.values().stream().mapToDouble(count -> Math.pow(count - passengerAverage, 2)).sum() / (numBookings - 1);
			double standardDeviationPassengers = Math.sqrt(variancePassengers);
			dashboard.setBookingDeviationPassengers(standardDeviationPassengers);
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
