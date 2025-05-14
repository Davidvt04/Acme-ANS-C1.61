
package acme.features.administrator.flightStatusDashboard;

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

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.delay.Delay;
import acme.realms.AssistanceAgent;

@GuiService
public class AdministratorDelayCreateService extends AbstractGuiService<AssistanceAgent, Delay> {

	@Autowired
	private AdministratorDelayRepository repository;


	public Collection<Delay> getAllDelays() {

		this.repository.deleteAllDelays();

		List<Delay> delays = new LinkedList<>();

		try {
			String accessKey = "";
			String url = "https://api.aviationstack.com/v1/flights?access_key=" + accessKey + "&limit=20";

			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			MomentHelper.sleep(1000);

			if (response != null && response.getBody() != null) {

				JSONObject contJSON = new JSONObject(response.getBody());
				JSONArray flights = contJSON.getJSONArray("data");

				for (int i = 0; i < 20 && i < flights.length(); i++) {
					JSONObject iObj = flights.getJSONObject(i);
					Delay delay = new Delay();
					JSONObject airline = iObj.getJSONObject("airline");
					delay.setAirline(airline.getString("name"));

					JSONObject arrival = iObj.getJSONObject("arrival");
					delay.setArrivalActualDateTime(null);
					if (arrival.getString("actual") != "null")
						delay.setArrivalActualDateTime(MomentHelper.parse(arrival.getString("actual"), "yyyy-MM-dd'T'HH:mm:ssXXX"));
					delay.setArrivalAirport(arrival.getString("airport"));

					delay.setArrivalScheduledDateTime(null);
					if (arrival.getString("scheduled") != "null")
						delay.setArrivalScheduledDateTime(MomentHelper.parse(arrival.getString("scheduled"), "yyyy-MM-dd'T'HH:mm:ssXXX"));
					JSONObject departure = iObj.getJSONObject("departure");
					delay.setDepartureActualDateTime(null);
					if (departure.getString("actual") != "null")
						delay.setDepartureActualDateTime(MomentHelper.parse(departure.getString("actual"), "yyyy-MM-dd'T'HH:mm:ssXXX"));
					delay.setDepartureAirport(departure.getString("airport"));
					delay.setDepartureScheduledDateTime(null);
					if (departure.getString("scheduled") != "null")
						delay.setDepartureScheduledDateTime(MomentHelper.parse(departure.getString("scheduled"), "yyyy-MM-dd'T'HH:mm:ssXXX"));

					delay.setStatus(iObj.getString("flight_status"));

					this.repository.save(delay);
					delays.add(delay);
				}
			}

		} catch (final Exception e) {
			System.out.println(e.getMessage());
		}

		return delays;
	}

}
