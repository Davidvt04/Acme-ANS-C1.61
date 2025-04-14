
package acme.features.customer.recommendationDahboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.recommendations.RecommendationDashboard;
import acme.realms.Customer;

@GuiService
public class CustomerRecommendationDashboardShowService extends AbstractGuiService<Customer, RecommendationDashboard> {

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
		RecommendationDashboard recommendationDashboard = new RecommendationDashboard();
		super.getBuffer().addData(List.of(recommendationDashboard));
	}

	@Override
	public void unbind(final RecommendationDashboard recommendationDashboard) {
		Dataset dataset = super.unbindObject(recommendationDashboard, "id", "city", "country", "name", "type");
		super.getResponse().addData(dataset);
	}
}
