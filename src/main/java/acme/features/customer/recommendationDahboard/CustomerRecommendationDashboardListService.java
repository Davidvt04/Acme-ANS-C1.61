
package acme.features.customer.recommendationDahboard;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
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

	@SuppressWarnings("deprecation")
	@Override
	public void load() {

		List<RecommendationDashboard> recommendationDashboards = new ArrayList<>();

		String city = super.getRequest().getData("city", String.class);
		String country = super.getRequest().getData("country", String.class);
		String apiKey = "";
		String term = "tourist";
		Integer count = 5;

		try {
			String urlString = String.format("https://api.yelp.com/v3/businesses/search?location=%s&term=%s&limit=%d", city, term, count);
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + apiKey);

			int responseCode = conn.getResponseCode();

			if (responseCode == 200) {
				InputStream contStream = conn.getInputStream();
				byte[] contBytes = contStream.readAllBytes();
				String contString = "";

				for (byte bytte : contBytes)
					contString += (char) bytte;

				JSONObject contJSON = new JSONObject(contString);
				JSONArray businesses = (JSONArray) contJSON.get("businesses");

				for (int i = 0; i < count; i++) {
					JSONObject iObj = (JSONObject) businesses.get(i);
					RecommendationDashboard recommendationDashboard = new RecommendationDashboard();
					recommendationDashboard.setCity(city);
					recommendationDashboard.setCountry(country);
					recommendationDashboard.setName(iObj.get("name").toString());
					JSONArray categoryArray = (JSONArray) iObj.get("categories");
					recommendationDashboard.setType(((JSONObject) categoryArray.get(0)).get("title").toString());
					recommendationDashboards.add(recommendationDashboard);
				}

			} else
				System.out.println("Request Error: Code " + responseCode);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		super.getBuffer().addData(recommendationDashboards);
	}

	@Override
	public void unbind(final RecommendationDashboard recommendationsDashboard) {
		Dataset dataset = super.unbindObject(recommendationsDashboard, "city", "country", "name", "description", "type");
		super.getResponse().addData(dataset);
	}

}
