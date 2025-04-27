
package acme.features.customer.recommendationDahboard;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.recommendation.Recommendation;
import acme.forms.recommendations.RecommendationDashboard;
import acme.realms.Customer;

@GuiService
public class CustomerRecommendationDashboardListService extends AbstractGuiService<Customer, RecommendationDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerRecommendationDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		List<RecommendationDashboard> recommendationDashboards = new LinkedList<>();

		String city = super.getRequest().getData("city", String.class);
		String country = super.getRequest().getData("country", String.class);

		Collection<Recommendation> recommendations = this.repository.getRecommendationsOf(city, country);

		for (Recommendation recommendation : recommendations) {
			RecommendationDashboard dashboard = new RecommendationDashboard();
			dashboard.setCity(recommendation.getCity());
			dashboard.setCountry(recommendation.getCountry());
			dashboard.setName(recommendation.getName());
			dashboard.setRating(recommendation.getRating());
			recommendationDashboards.add(dashboard);
		}

		super.getBuffer().addData(recommendationDashboards);
	}

	@Override
	public void unbind(final RecommendationDashboard recommendationsDashboard) {
		Dataset dataset = super.unbindObject(recommendationsDashboard, "city", "country", "name", "rating", "type");
		super.getResponse().addData(dataset);
	}

}
