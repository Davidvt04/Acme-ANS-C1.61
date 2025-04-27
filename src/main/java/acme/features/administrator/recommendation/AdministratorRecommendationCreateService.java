
package acme.features.administrator.recommendation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import acme.client.components.principals.Administrator;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.recommendation.Recommendation;

@GuiService
public class AdministratorRecommendationCreateService extends AbstractGuiService<Administrator, Recommendation> {

	@Autowired
	private AdministratorRecommendationRepository repository;


	public Collection<Recommendation> getAllRecommendations() {

		this.repository.deleteAllRecommendations();

		List<Recommendation> recommendations = new LinkedList<>();

		List<Flight> flights = this.repository.findAllFlights();

		List<Airport> destinationAirports = flights.stream().map(Flight::getDestinationAirport).distinct().toList();

		for (Airport airport : destinationAirports)
			try {
				String url = "https://api.yelp.com/v3/businesses/search?location=" + airport.getCity() + "&term=tourist&limit=5";

				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + "sGqp41nq1GURFR3yOm1m6crqmPIGGddSjVOkisOpl2y2hOfSNfTPa9b-mxeW_Sq4uSN6bvexh3hpXom_ZpMXm3Z0F-hASob2E0xkCN1KNv3U5PiBInosNcUZ10_9Z3Yx");
				HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
				MomentHelper.sleep(1000);

				if (response != null && response.getBody() != null) {

					JSONObject contJSON = new JSONObject(response.getBody());
					JSONArray businesses = contJSON.getJSONArray("businesses");

					for (int i = 0; i < 5 && i < businesses.length(); i++) {
						JSONObject iObj = businesses.getJSONObject(i);
						Recommendation recommendation = new Recommendation();
						recommendation.setCity(airport.getCity());
						recommendation.setCountry(airport.getCountry());
						recommendation.setName(iObj.getString("name"));
						recommendation.setRating(iObj.getDouble("rating"));
						JSONArray types = iObj.getJSONArray("categories");
						recommendation.setType(types.getJSONObject(0).getString("title"));
						this.repository.save(recommendation);
						recommendations.add(recommendation);
					}
				}

			} catch (final Exception e) {
				System.out.println(e.getMessage());
			}

		return recommendations;
	}

}
